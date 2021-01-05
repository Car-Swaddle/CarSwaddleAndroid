package com.carswaddle.carswaddlemechanic.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.login.AuthActivity
import com.carswaddle.services.Authentication

class MechanicProfileFragment : Fragment() {

    private lateinit var viewModel: MechanicProfileViewModel
    
    private lateinit var logoutButton: Button
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MechanicProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mechanic_profile, container, false)

        logoutButton = root.findViewById(R.id.profile_logout_button)
        
        logoutButton.setOnClickListener { 
            logout()
        }
        
        return root
    }
    
    private fun logout() {
        Authentication(requireContext()).logout { error, response ->
            val intent = Intent(activity, AuthActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
    
}