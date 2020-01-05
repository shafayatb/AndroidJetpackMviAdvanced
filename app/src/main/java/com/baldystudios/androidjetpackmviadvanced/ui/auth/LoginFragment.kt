package com.baldystudios.androidjetpackmviadvanced.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.testLogin().observe(viewLifecycleOwner, Observer { response ->

            when (response) {

                is ApiSuccessResponse -> {
                    Log.d(TAG, "Login Response: ${response.body}")
                }

                is ApiErrorResponse -> {
                    Log.d(TAG, "Login Response: ${response.errorMessage}")
                }

                is ApiEmptyResponse -> {
                    Log.d(TAG, "Login Response: Empty Response")
                }

            }

        })
    }

}
