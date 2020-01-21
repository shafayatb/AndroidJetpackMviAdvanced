package com.baldystudios.androidjetpackmviadvanced.ui.main.blog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.di.Injectable
import com.baldystudios.androidjetpackmviadvanced.ui.DataStateChangeListener
import com.baldystudios.androidjetpackmviadvanced.ui.UICommunicationListener
import com.baldystudios.androidjetpackmviadvanced.ui.main.MainDependencyProvider
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BLOG_VIEW_STATE_BUNDLE_KEY
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogViewState
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.viewmodel.BlogViewModel

abstract class BaseBlogFragment : Fragment(), Injectable {

    val TAG: String = "AppDebug"

    lateinit var dependencyProvider: MainDependencyProvider

    lateinit var viewModel: BlogViewModel

    lateinit var stateChangeListener: DataStateChangeListener

    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(
                this,
                dependencyProvider.getViewModelProviderFactory()
            ).get(
                BlogViewModel::class.java
            )
        } ?: throw Exception("Invalid Activity")

        cancelActiveJobs()

        //restore state after process death
        savedInstanceState?.let { instate ->
            (instate[BLOG_VIEW_STATE_BUNDLE_KEY] as BlogViewState?)?.let { blogViewState ->
                viewModel.setViewState(blogViewState)
            }
        }
    }

    fun isViewModelInitialized() = ::viewModel.isInitialized

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.blogFragment, activity as AppCompatActivity)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (isViewModelInitialized()) {
            outState.putParcelable(
                BLOG_VIEW_STATE_BUNDLE_KEY,
                viewModel.viewState.value
            )
        }
        super.onSaveInstanceState(outState)
    }

    /*
          @fragmentId is id of fragment from graph to be EXCLUDED from action back bar nav
        */
    fun setupActionBarWithNavController(fragmentId: Int, activity: AppCompatActivity) {
        val appBarConfiguration = AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appBarConfiguration
        )
    }

    fun cancelActiveJobs() {
        viewModel.cancelActiveJobs()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangeListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DataStateChangeListener")
        }

        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement UICommunicationListener")
        }

        try {
            dependencyProvider = context as MainDependencyProvider
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement MainDependencyProvider")
        }
    }
}