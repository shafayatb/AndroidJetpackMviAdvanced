package com.baldystudios.androidjetpackmviadvanced.ui

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.ui.ResponseType.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(),
    DataStateChangeListener,
    UICommunicationListener {


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

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
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

    override fun onUIMessageReceived(uiMessage: UIMessage) {
        when (uiMessage.uiMessageType) {

            is UIMessageType.Toast -> displayToast(uiMessage.message)

            is UIMessageType.Dialog -> displayInfoDialog(uiMessage.message)

            is UIMessageType.AreYouSureDialog -> areYouSureDialog(
                uiMessage.message,
                uiMessage.uiMessageType.callback
            )

            is UIMessageType.None -> Log.i(TAG, "onUIMessageReceived: ${uiMessage.message}")
        }
    }
}