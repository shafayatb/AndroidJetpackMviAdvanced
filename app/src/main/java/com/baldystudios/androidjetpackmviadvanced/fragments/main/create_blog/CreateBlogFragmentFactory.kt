package com.baldystudios.androidjetpackmviadvanced.fragments.main.create_blog

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthScope
import com.baldystudios.androidjetpackmviadvanced.di.main.MainScope
import com.baldystudios.androidjetpackmviadvanced.ui.auth.ForgotPasswordFragment
import com.baldystudios.androidjetpackmviadvanced.ui.auth.LauncherFragment
import com.baldystudios.androidjetpackmviadvanced.ui.auth.LoginFragment
import com.baldystudios.androidjetpackmviadvanced.ui.auth.RegisterFragment
import com.baldystudios.androidjetpackmviadvanced.ui.main.account.AccountFragment
import com.baldystudios.androidjetpackmviadvanced.ui.main.account.ChangePasswordFragment
import com.baldystudios.androidjetpackmviadvanced.ui.main.account.UpdateAccountFragment
import com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog.CreateBlogFragment
import com.bumptech.glide.RequestManager
import javax.inject.Inject

@MainScope
class CreateBlogFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            CreateBlogFragment::class.java.name -> CreateBlogFragment(viewModelFactory, requestManager)

            else -> CreateBlogFragment(viewModelFactory, requestManager)

        }


}