package com.baldystudios.androidjetpackmviadvanced.fragments.main.blog

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.baldystudios.androidjetpackmviadvanced.di.main.MainScope
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.BlogFragment
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.UpdateBlogFragment
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.ViewBlogFragment
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import javax.inject.Inject

@MainScope
class BlogFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestOptions: RequestOptions,
    private val requestManager: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            BlogFragment::class.java.name -> BlogFragment(viewModelFactory, requestOptions)

            ViewBlogFragment::class.java.name -> ViewBlogFragment(viewModelFactory, requestManager)

            UpdateBlogFragment::class.java.name -> UpdateBlogFragment(viewModelFactory, requestManager)

            else -> BlogFragment(viewModelFactory, requestOptions)

        }


}