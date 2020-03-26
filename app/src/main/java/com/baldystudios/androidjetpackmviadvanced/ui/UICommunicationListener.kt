package com.baldystudios.androidjetpackmviadvanced.ui

import com.baldystudios.androidjetpackmviadvanced.util.Response
import com.baldystudios.androidjetpackmviadvanced.util.StateMessageCallback

interface UICommunicationListener {

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun displayProgressBar(isLoading: Boolean)

    fun expandAppBar()

    fun hideSoftKeyboard()

    fun isStoragePermissionGranted(): Boolean

}