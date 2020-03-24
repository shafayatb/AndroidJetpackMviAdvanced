package com.baldystudios.androidjetpackmviadvanced.ui

import com.baldystudios.androidjetpackmviadvanced.util.DataState

interface DataStateChangeListener {

    fun onDataStateChange(dataState: DataState<*>?)

    fun expandAppBar()

    fun hideSoftKeyboard()

    fun isStoragePermissionGranted(): Boolean
}