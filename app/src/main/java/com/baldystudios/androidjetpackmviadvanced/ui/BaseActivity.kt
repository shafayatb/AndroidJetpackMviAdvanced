package com.baldystudios.androidjetpackmviadvanced.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.ui.ResponseType.*
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.PERMISSIONS_REQUEST_READ_STORAGE
import com.baldystudios.androidjetpackmviadvanced.util.DataState
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(),
    DataStateChangeListener,
    UICommunicationListener {


    val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager

    abstract fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }


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

    override fun isStoragePermissionGranted(): Boolean {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSIONS_REQUEST_READ_STORAGE
            )
            return false
        } else {
            return true
        }
    }
}