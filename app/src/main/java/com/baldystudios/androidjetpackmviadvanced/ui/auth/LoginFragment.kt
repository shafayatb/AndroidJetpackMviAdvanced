package com.baldystudios.androidjetpackmviadvanced.ui.auth


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthScope
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.AuthStateEvent
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.LoginFields
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@AuthScope
class LoginFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
) : Fragment(R.layout.fragment_login) {

    val viewModel: AuthViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.cancelActiveJobs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()

        login_button.setOnClickListener {
            login()
        }

    }


    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.loginFields?.let { loginFields ->
                loginFields.login_email?.let { email -> input_email.setText(email) }
                loginFields.login_password?.let { password -> input_password.setText(password) }
            }
        })
    }

    fun login() {

        viewModel.setStateEvent(
            AuthStateEvent.LoginAttemptEvent(
                input_email.text.toString(),
                input_password.text.toString()
            )
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLoginFields(
            LoginFields(
                input_email.text.toString(),
                input_password.text.toString()
            )
        )
    }

}
