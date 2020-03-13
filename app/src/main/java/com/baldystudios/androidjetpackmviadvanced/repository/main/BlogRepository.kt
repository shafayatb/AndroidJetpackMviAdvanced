package com.baldystudios.androidjetpackmviadvanced.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.baldystudios.androidjetpackmviadvanced.api.GenericResponse
import com.baldystudios.androidjetpackmviadvanced.api.main.OpenApiMainService
import com.baldystudios.androidjetpackmviadvanced.api.main.responses.BlogCreateUpdateResponse
import com.baldystudios.androidjetpackmviadvanced.api.main.responses.BlogListSearchResponse
import com.baldystudios.androidjetpackmviadvanced.di.main.MainScope
import com.baldystudios.androidjetpackmviadvanced.models.AuthToken
import com.baldystudios.androidjetpackmviadvanced.models.BlogPost
import com.baldystudios.androidjetpackmviadvanced.persistence.BlogPostDao
import com.baldystudios.androidjetpackmviadvanced.persistence.returnOrderedBlogQuery
import com.baldystudios.androidjetpackmviadvanced.repository.JobManager
import com.baldystudios.androidjetpackmviadvanced.repository.NetworkBoundResource
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.ui.DataState
import com.baldystudios.androidjetpackmviadvanced.ui.Response
import com.baldystudios.androidjetpackmviadvanced.ui.ResponseType
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogViewState
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogViewState.BlogFields
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogViewState.ViewBlogFields
import com.baldystudios.androidjetpackmviadvanced.util.AbsentLiveData
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.PAGINATION_PAGE_SIZE
import com.baldystudios.androidjetpackmviadvanced.util.DateUtils
import com.baldystudios.androidjetpackmviadvanced.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse
import com.baldystudios.androidjetpackmviadvanced.util.SuccessHandling.Companion.RESPONSE_NO_PERMISSION_TO_EDIT
import com.baldystudios.androidjetpackmviadvanced.util.SuccessHandling.Companion.SUCCESS_BLOG_DELETED
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@MainScope
class BlogRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
) : JobManager("BlogRepository") {

    private val TAG: String = "AppDebug"

    fun searchBlogPost(
        authToken: AuthToken,
        query: String,
        filerAndOrder: String,
        page: Int
    ): LiveData<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BlogListSearchResponse, List<BlogPost>, BlogViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                false,
                true
            ) {
            override suspend fun createCacheRequestAndReturn() {
                withContext(Main) {

                    // finish by viewing the db cache
                    result.addSource(loadFromCache()) { viewState ->
                        viewState.blogFields.isQueryInProgress = false
                        if (page * PAGINATION_PAGE_SIZE > viewState.blogFields.blogList.size) {
                            viewState.blogFields.isQueryExhausted = true
                        }
                        onCompleteJob(DataState.data(viewState, null))
                    }
                }

            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<BlogListSearchResponse>) {
                val blogPostList: ArrayList<BlogPost> = ArrayList()
                for (blogPostResponse in response.body.results) {
                    blogPostList.add(
                        BlogPost(
                            pk = blogPostResponse.pk,
                            title = blogPostResponse.title,
                            slug = blogPostResponse.slug,
                            body = blogPostResponse.body,
                            image = blogPostResponse.image,
                            date_updated = DateUtils.convertServerStringDateToLong(
                                blogPostResponse.date_updated
                            ),
                            username = blogPostResponse.username
                        )
                    )
                }

                updateLocalDb(blogPostList)

                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogListSearchResponse>> {
                return openApiMainService.searchListBlogPost(
                    "Token ${authToken.token!!}",
                    query = query,
                    ordering = filerAndOrder,
                    page = page
                )
            }

            override fun loadFromCache(): LiveData<BlogViewState> {

                return blogPostDao.returnOrderedBlogQuery(
                        query = query,
                        filterAndOrder = filerAndOrder,
                        page = page
                    )
                    .switchMap {
                        object : LiveData<BlogViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = BlogViewState(
                                    BlogFields(
                                        blogList = it,
                                        isQueryInProgress = true
                                    )
                                )
                            }
                        }
                    }

            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
                if (cacheObject != null) {
                    withContext(IO) {
                        for (blogPost in cacheObject) {
                            try {
                                // launch each insert as a separate job to be executed in parallel
                                launch {
                                    Log.d(TAG, "updateLocalDb: insert blog: $blogPost")
                                    blogPostDao.insert(blogPost)
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG,
                                    "updateLocalDb: error updating cache on blog post with slug: ${blogPost.slug}"
                                )
                            }
                        }
                    }
                }
            }

            override fun setJob(job: Job) {
                addJob("searchBlogPost", job)
            }

        }.asLiveData()
    }

    fun restoreBlogListFromCache(
        query: String,
        filerAndOrder: String,
        page: Int
    ): LiveData<DataState<BlogViewState>> {
        return object :
            NetworkBoundResource<BlogListSearchResponse, List<BlogPost>, BlogViewState>(
                sessionManager.isConnectedToTheInternet(),
                false,
                false,
                true
            ) {
            override suspend fun createCacheRequestAndReturn() {
                withContext(Main) {

                    // finish by viewing the db cache
                    result.addSource(loadFromCache()) { viewState ->
                        viewState.blogFields.isQueryInProgress = false
                        if (page * PAGINATION_PAGE_SIZE > viewState.blogFields.blogList.size) {
                            viewState.blogFields.isQueryExhausted = true
                        }
                        onCompleteJob(DataState.data(viewState, null))
                    }
                }

            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<BlogListSearchResponse>) {

            }

            override fun createCall(): LiveData<GenericApiResponse<BlogListSearchResponse>> {
                return AbsentLiveData.create()
            }

            override fun loadFromCache(): LiveData<BlogViewState> {

                return blogPostDao.returnOrderedBlogQuery(
                        query = query,
                        filterAndOrder = filerAndOrder,
                        page = page
                    )
                    .switchMap {
                        object : LiveData<BlogViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = BlogViewState(
                                    BlogFields(
                                        blogList = it,
                                        isQueryInProgress = true
                                    )
                                )
                            }
                        }
                    }

            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {

            }

            override fun setJob(job: Job) {
                addJob("restoreBlogListFromCache", job)
            }

        }.asLiveData()
    }

    fun isAuthorOfBlogPost(
        authToken: AuthToken,
        slug: String
    ): LiveData<DataState<BlogViewState>> {
        return object : NetworkBoundResource<GenericResponse, Any, BlogViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<GenericResponse>) {
                withContext(Main) {
                    Log.d(TAG, "handleApiSuccessResponse: ${response.body.response}")

                    onCompleteJob(
                        DataState.data(
                            data = BlogViewState(
                                viewBlogFields = ViewBlogFields(
                                    isAuthorOfBlogPost = response.body.response != RESPONSE_NO_PERMISSION_TO_EDIT
                                )
                            )
                        )
                    )

                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiMainService.isAuthorOfBlogPost(
                    "Token ${authToken.token}",
                    slug
                )
            }

            override fun loadFromCache(): LiveData<BlogViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: Any?) {
            }

            override fun setJob(job: Job) {
                addJob("isAuthorOfBlogPost", job)
            }

        }.asLiveData()
    }

    fun deleteBlogPost(
        authToken: AuthToken,
        blogPost: BlogPost
    ): LiveData<DataState<BlogViewState>> {

        return object : NetworkBoundResource<GenericResponse, BlogPost, BlogViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<GenericResponse>) {
                if (response.body.response == SUCCESS_BLOG_DELETED) {
                    updateLocalDb(blogPost)
                } else {
                    onCompleteJob(
                        DataState.error(
                            Response(
                                ERROR_UNKNOWN,
                                ResponseType.Dialog()
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiMainService.deleteBlogPost(
                    "Token ${authToken.token}",
                    blogPost.slug
                )
            }

            override fun loadFromCache(): LiveData<BlogViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: BlogPost?) {
                cacheObject?.let { blogPost ->
                    blogPostDao.deleteBlogPost(blogPost)
                    onCompleteJob(
                        DataState.data(
                            data = null,
                            response = Response(SUCCESS_BLOG_DELETED, ResponseType.Toast())
                        )
                    )
                }

            }

            override fun setJob(job: Job) {
                addJob("deleteBlogPost", job)
            }

        }.asLiveData()

    }

    fun updateBlogPost(
        authToken: AuthToken,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): LiveData<DataState<BlogViewState>> {

        return object : NetworkBoundResource<BlogCreateUpdateResponse, BlogPost, BlogViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<BlogCreateUpdateResponse>) {

                val updatedBlogPost = BlogPost(
                    response.body.pk,
                    response.body.title,
                    response.body.slug,
                    response.body.body,
                    response.body.image,
                    DateUtils.convertServerStringDateToLong(
                        response.body.date_updated
                    ),
                    response.body.username
                )

                updateLocalDb(updatedBlogPost)

                withContext(Main) {
                    onCompleteJob(
                        DataState.data(
                            data = BlogViewState(
                                viewBlogFields = ViewBlogFields(
                                    blogPost = updatedBlogPost
                                )
                            ),
                            response = Response(response.body.response, ResponseType.Toast())
                        )
                    )
                }

            }

            override fun createCall(): LiveData<GenericApiResponse<BlogCreateUpdateResponse>> {
                return openApiMainService.updateBlogPost(
                    "Token ${authToken.token}",
                    slug,
                    title,
                    body,
                    image
                )
            }

            override fun loadFromCache(): LiveData<BlogViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: BlogPost?) {
                cacheObject?.let { blogPost ->
                    blogPostDao.updateBlogPost(
                        blogPost.pk,
                        blogPost.title,
                        blogPost.body,
                        blogPost.image
                    )
                }
            }

            override fun setJob(job: Job) {
                addJob("updateBlogPost", job)
            }

        }.asLiveData()

    }

}