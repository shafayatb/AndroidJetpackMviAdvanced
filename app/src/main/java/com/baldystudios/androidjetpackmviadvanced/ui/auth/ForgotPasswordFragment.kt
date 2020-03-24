package com.baldystudios.androidjetpackmviadvanced.ui.auth


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthScope
import com.baldystudios.androidjetpackmviadvanced.util.DataState
import com.baldystudios.androidjetpackmviadvanced.ui.DataStateChangeListener
import com.baldystudios.androidjetpackmviadvanced.ui.Response
import com.baldystudios.androidjetpackmviadvanced.ui.ResponseType
import com.baldystudios.androidjetpackmviadvanced.ui.auth.ForgotPasswordFragment.WebAppInterface.OnWebInteractionCallback
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.PASSWORD_RESET_URL
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@AuthScope
class ForgotPasswordFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
) : Fragment(R.layout.fragment_forgot_password) {

    val viewModel: AuthViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.cancelActiveJobs()
    }

    lateinit var webView: WebView

    lateinit var stateChangeListener: DataStateChangeListener

    val webInteractionCallback = object : OnWebInteractionCallback {
        override fun onSuccess(email: String) {

            onPasswordResetLinkSent()
        }

        override fun onError(errorMessage: String) {


            val dataState = DataState.error<Any>(
                response = Response(errorMessage, ResponseType.Dialog())
            )
            stateChangeListener.onDataStateChange(dataState)
        }

        override fun onLoading(isLoading: Boolean) {

            GlobalScope.launch(Main) {
                stateChangeListener.onDataStateChange(
                    DataState.loading(isLoading = isLoading, cachedData = null)
                )
            }
        }

    }

    private fun onPasswordResetLinkSent() {
        GlobalScope.launch(Main) {
            parent_view.removeView(webView)
            webView.destroy()

            val animation = TranslateAnimation(
                password_reset_done_container.width.toFloat(),
                0f,
                0f,
                0f
            )
            animation.duration = 500
            password_reset_done_container.startAnimation(animation)
            password_reset_done_container.visibility = View.VISIBLE
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = webview



        loadPasswordResetWebView()

        return_to_launcher_fragment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun loadPasswordResetWebView() {
        stateChangeListener.onDataStateChange(
            DataState.loading(
                isLoading = true,
                cachedData = null
            )
        )

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                stateChangeListener.onDataStateChange(
                    DataState.loading(isLoading = false, cachedData = null)
                )
            }
        }

        webView.loadUrl(PASSWORD_RESET_URL)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(
            WebAppInterface(webInteractionCallback),
            "AndroidTextListener"
        )
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangeListener
        } catch (e: ClassCastException) {


        }
    }

    class WebAppInterface constructor(private val callback: OnWebInteractionCallback) {

        private val TAG: String = "AppDebug"

        @JavascriptInterface
        fun onSuccess(email: String) {
            callback.onSuccess(email)
        }

        @JavascriptInterface
        fun onError(errorMessage: String) {
            callback.onError(errorMessage)
        }

        @JavascriptInterface
        fun onLoading(isLoading: Boolean) {
            callback.onLoading(isLoading)
        }

        interface OnWebInteractionCallback {
            fun onSuccess(email: String)

            fun onError(errorMessage: String)

            fun onLoading(isLoading: Boolean)
        }
    }

}
