package com.baldystudios.androidjetpackmviadvanced.ui.auth


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.RegistrationFields
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.registrationFields?.let { registrationFields ->
                registrationFields.registration_email?.let { email -> input_email.setText(email) }
                registrationFields.registration_username?.let { username -> input_username.setText(username) }
                registrationFields.registration_password?.let { password -> input_password.setText(password) }
                registrationFields.registration_confirm_password?.let { confirmPassword -> input_password_confirm.setText(confirmPassword) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }


}
