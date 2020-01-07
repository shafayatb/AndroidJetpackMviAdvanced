package com.baldystudios.androidjetpackmviadvanced.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.ui.BaseActivity
import com.baldystudios.androidjetpackmviadvanced.ui.ResponseType.*
import com.baldystudios.androidjetpackmviadvanced.ui.main.MainActivity
import com.baldystudios.androidjetpackmviadvanced.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)

        subscribeObservers()
    }

    fun subscribeObservers() {

        viewModel.dataState.observe(this, Observer { dataState ->

            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.authToken?.let { authToken ->
                            Log.d(TAG, "AuthActivity, DataState: $authToken")
                            viewModel.setAuthToken(authToken)
                        }
                    }
                }

                data.response?.let { event ->
                    event.getContentIfNotHandled()?.let { response ->
                        when (response.responseType) {

                            is Dialog -> {

                            }

                            is Toast -> {

                            }

                            is None -> {
                                Log.e(TAG, "AuthActivity, Response: ${response.message}")
                            }

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

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
