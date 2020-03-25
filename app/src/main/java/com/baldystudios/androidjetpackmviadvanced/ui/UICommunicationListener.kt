package com.baldystudios.androidjetpackmviadvanced.ui

import com.baldystudios.androidjetpackmviadvanced.util.Response

interface UICommunicationListener {

    fun onResponseReceived(response: Response)

    fun displayProgressBar(isLoading: Boolean)

    fun expandAppBar()

    fun hideSoftKeyboard()

    fun isStoragePermissionGranted(): Boolean

}