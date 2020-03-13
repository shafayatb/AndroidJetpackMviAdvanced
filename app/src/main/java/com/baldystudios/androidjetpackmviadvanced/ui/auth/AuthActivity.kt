package com.baldystudios.androidjetpackmviadvanced.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.baldystudios.androidjetpackmviadvanced.BaseApplication
import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.fragments.auth.AuthNavHostFragment
import com.baldystudios.androidjetpackmviadvanced.ui.BaseActivity
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.AuthStateEvent
import com.baldystudios.androidjetpackmviadvanced.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    val viewModel: AuthViewModel by viewModels {
        providerFactory
    }

    override fun inject() {
        (application as BaseApplication).authComponent()
            .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        subscribeObservers()
        onRestoreInstanceState()
    }

    private fun onRestoreInstanceState() {
        val host = supportFragmentManager
            .findFragmentById(R.id.auth_fragment_container)
        host?.let {

        } ?: createNavHost()
    }

    private fun createNavHost() {
        val navHost = AuthNavHostFragment.create(
            R.navigation.auth_nav_graph
        )
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.auth_fragment_container,
                navHost,
                getString(R.string.AuthNavHost)
            )
            .setPrimaryNavigationFragment(navHost)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        checkPreviousAuthUser()
    }

    override fun expandAppBar() {

    }

    fun subscribeObservers() {

        viewModel.dataState.observe(this, Observer { dataState ->

            onDataStateChange(dataState)

            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.authToken?.let { authToken ->
                            Log.d(TAG, "AuthActivity, DataState: $authToken")
                            viewModel.setAuthToken(authToken)
                        }
                    }
                }

            }

        })

        viewModel.viewState.observe(this, Observer {
            it.authToken?.let { authToken ->
                sessionManager.login(authToken)
            }
        })

        sessionManager.cachedToken.observe(this, Observer { authToken ->

            Log.d(TAG, "AuthActivity: subscribeObservers: AuthToken $authToken")

            if (authToken != null && authToken.account_pk != -1 && authToken.token != null) {
                navMainActivity()
            }

        })

    }

    fun checkPreviousAuthUser() {
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousAuthEvent())
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        (application as BaseApplication).releaseAuthComponent()
    }

    override fun displayProgressBar(bool: Boolean) {
        progress_bar.visibility = if (bool) VISIBLE else GONE
    }
}
