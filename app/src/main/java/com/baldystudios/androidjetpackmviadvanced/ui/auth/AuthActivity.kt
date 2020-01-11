package com.baldystudios.androidjetpackmviadvanced.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.ui.BaseActivity
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.AuthStateEvent
import com.baldystudios.androidjetpackmviadvanced.ui.main.MainActivity
import com.baldystudios.androidjetpackmviadvanced.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : BaseActivity(), NavController.OnDestinationChangedListener {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)

        findNavController(R.id.auth_nav_host_fragment).addOnDestinationChangedListener(this)

        subscribeObservers()

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
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        viewModel.cancelActiveJobs()
    }

    override fun displayProgressBar(bool: Boolean) {
        progress_bar.visibility = if (bool) VISIBLE else GONE
    }
}
