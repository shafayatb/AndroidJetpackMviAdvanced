package com.baldystudios.androidjetpackmviadvanced.ui.auth


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse

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

        viewModel.testRegistration().observe(viewLifecycleOwner, Observer { response ->

            when (response) {

                is GenericApiResponse.ApiSuccessResponse -> {
                    Log.d(TAG, "Registration Response: ${response.body}")
                }

                is GenericApiResponse.ApiErrorResponse -> {
                    Log.d(TAG, "Registration Response: ${response.errorMessage}")
                }

                is GenericApiResponse.ApiEmptyResponse -> {
                    Log.d(TAG, "Registration Response: Empty Response")
                }

            }

        })
    }


}
