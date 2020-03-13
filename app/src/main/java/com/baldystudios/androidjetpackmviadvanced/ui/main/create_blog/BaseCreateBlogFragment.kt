package com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog

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
import com.baldystudios.androidjetpackmviadvanced.ui.DataStateChangeListener
import com.baldystudios.androidjetpackmviadvanced.ui.UICommunicationListener
import com.baldystudios.androidjetpackmviadvanced.ui.main.MainDependencyProvider
import com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog.state.CREATE_BLOG_VIEW_STATE_BUNDLE_KEY
import com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog.state.CreateBlogViewState


abstract class BaseCreateBlogFragment : Fragment(), Injectable {

    val TAG: String = "AppDebug"

    lateinit var dependencyProvider: MainDependencyProvider

    lateinit var uiCommunicationListener: UICommunicationListener

    lateinit var viewModel: CreateBlogViewModel

    lateinit var stateChangeListener: DataStateChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(
                this,
                dependencyProvider.getViewModelProviderFactory()
            ).get(CreateBlogViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        cancelActiveJobs()

        //restore state after process death
        savedInstanceState?.let { instate ->
            (instate[CREATE_BLOG_VIEW_STATE_BUNDLE_KEY] as CreateBlogViewState?)?.let { createViewState ->
                viewModel.setViewState(createViewState)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.createBlogFragment, activity as AppCompatActivity)


    }

    fun isViewModelInitialized() = ::viewModel.isInitialized

    override fun onSaveInstanceState(outState: Bundle) {
        if (isViewModelInitialized()) {
            outState.putParcelable(
                CREATE_BLOG_VIEW_STATE_BUNDLE_KEY,
                viewModel.viewState.value
            )
        }
        super.onSaveInstanceState(outState)
    }

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