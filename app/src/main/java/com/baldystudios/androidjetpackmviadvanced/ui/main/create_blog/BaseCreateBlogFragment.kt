package com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.baldystudios.androidjetpackmviadvanced.di.Injectable
import com.baldystudios.androidjetpackmviadvanced.ui.DataStateChangeListener


abstract class BaseCreateBlogFragment : Fragment(), Injectable {

    val TAG: String = "AppDebug"

    lateinit var stateChangeListener: DataStateChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangeListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DataStateChangeListener")
        }
    }
}