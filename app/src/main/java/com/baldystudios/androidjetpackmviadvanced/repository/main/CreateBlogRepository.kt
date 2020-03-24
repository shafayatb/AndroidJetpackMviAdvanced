package com.baldystudios.androidjetpackmviadvanced.repository.main

import androidx.lifecycle.LiveData
import com.baldystudios.androidjetpackmviadvanced.api.main.OpenApiMainService
import com.baldystudios.androidjetpackmviadvanced.api.main.responses.BlogCreateUpdateResponse
import com.baldystudios.androidjetpackmviadvanced.di.main.MainScope
import com.baldystudios.androidjetpackmviadvanced.models.AuthToken
import com.baldystudios.androidjetpackmviadvanced.models.BlogPost
import com.baldystudios.androidjetpackmviadvanced.persistence.BlogPostDao
import com.baldystudios.androidjetpackmviadvanced.repository.JobManager
import com.baldystudios.androidjetpackmviadvanced.repository.NetworkBoundResource
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.util.DataState
import com.baldystudios.androidjetpackmviadvanced.ui.Response
import com.baldystudios.androidjetpackmviadvanced.ui.ResponseType
import com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog.state.CreateBlogViewState
import com.baldystudios.androidjetpackmviadvanced.util.AbsentLiveData
import com.baldystudios.androidjetpackmviadvanced.util.DateUtils
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse
import com.baldystudios.androidjetpackmviadvanced.util.SuccessHandling.Companion.RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@MainScope
class CreateBlogRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
) : JobManager("CreateBlogRepository") {

    private val TAG: String = "AppDebug"

    fun createNewBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): LiveData<DataState<CreateBlogViewState>> {

        return object :
            NetworkBoundResource<BlogCreateUpdateResponse, BlogPost, CreateBlogViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                true,
                false
            ) {
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<BlogCreateUpdateResponse>) {

                // If user don't have a paid membership account it will still return 200
                if (response.body.response != RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER) {
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
                }

                withContext(Dispatchers.Main) {
                    //finish with success
                    onCompleteJob(
                        DataState.data(
                            null,
                            response = Response(response.body.response, ResponseType.Dialog())
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogCreateUpdateResponse>> {
                return openApiMainService.createBlog(
                    "Token ${authToken.token}",
                    title,
                    body,
                    image
                )
            }

            override fun loadFromCache(): LiveData<CreateBlogViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: BlogPost?) {
                cacheObject?.let { blogPost ->
                    blogPostDao.insert(
                        blogPost
                    )
                }
            }

            override fun setJob(job: Job) {
                addJob("createNewBlogPost", job)
            }

        }.asLiveData()

    }


}