package com.carswaddle.carswaddlemechanic.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.carswaddle.carswaddlemechanic.R

class AuthFragment : Fragment() {

    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_auth, container, false)

        loginButton = root.findViewById(R.id.loginButton)
        signUpButton = root.findViewById(R.id.signUpButton)

        loginButton.setOnClickListener {
            val navController = requireActivity().findNavController(R.id.auth_nav_host)
            navController.navigate(R.id.action_navigation_pre_auth_to_navigation_login, null)
        }

        signUpButton.setOnClickListener {
            val navController = requireActivity().findNavController(R.id.auth_nav_host)
            navController.navigate(R.id.action_navigation_pre_auth_to_signUpFragment, null)
        }

        return root
    }

}
