package com.baldystudios.androidjetpackmviadvanced.ui

import android.util.Log
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.ui.ResponseType.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), DataStateChangeListener {


    val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager


    override fun onDataStateChange(dataState: DataState<*>?) {

        dataState?.let {
            GlobalScope.launch(Main) {
                displayProgressBar(it.loading.isLoading)

                it.error?.let { errorEvent ->
                    handleStateError(errorEvent)
                }

                it.data?.let {

                    it.response?.let { responseEvent ->
                        handleStateResponse(responseEvent)
                    }

                }
            }
        }

    }

    private fun handleStateResponse(event: Event<Response>) {

        event.getContentIfNotHandled()?.let {

            when (it.responseType) {

                is Toast -> {
                    it.message?.let { message ->
                        displayToast(message)
                    }
                }

                is Dialog -> {
                    it.message?.let { message ->
                        displaySuccesDialog(message)
                    }
                }

                is None -> {
                    Log.d(TAG, "handleStateError: ${it.message}")
                }

            }

        }

    }

    private fun handleStateError(event: Event<StateError>) {

        event.getContentIfNotHandled()?.let {

            when (it.response.responseType) {

                is Toast -> {
                    it.response.message?.let { message ->
                        displayToast(message)
                    }
                }

                is Dialog -> {
                    it.response.message?.let { message ->
                        displayErrorDialog(message)
                    }
                }

                is None -> {
                    Log.d(TAG, "handleStateError: ${it.response.message}")
                }

            }

        }

    }

    abstract fun displayProgressBar(bool: Boolean)
}