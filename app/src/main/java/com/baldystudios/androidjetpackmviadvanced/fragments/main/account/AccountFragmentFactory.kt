package com.baldystudios.androidjetpackmviadvanced.fragments.main.account

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
import javax.inject.Inject

@MainScope
class AccountFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            AccountFragment::class.java.name -> AccountFragment(viewModelFactory)

            UpdateAccountFragment::class.java.name -> UpdateAccountFragment(viewModelFactory)

            ChangePasswordFragment::class.java.name -> ChangePasswordFragment(viewModelFactory)

            else -> AccountFragment(viewModelFactory)

        }


}