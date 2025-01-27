package com.carswaddle.carswaddleandroid.ui.activities.profile

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.ui.activities.PreAuthenticationActivity
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase
import java.util.*


class ProfileFragment() : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var nameValueTextView: TextView
    private lateinit var phoneNumberValueTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        nameValueTextView = root.findViewById(R.id.nameValueTextView)
        phoneNumberValueTextView = root.findViewById(R.id.phoneNumberValueTextView)
        
        profileViewModel.currentUser.observe(viewLifecycleOwner, Observer { user -> 
            nameValueTextView.text = user.displayName()
            if (!user.phoneNumber.isNullOrBlank()) {
                phoneNumberValueTextView.text = PhoneNumberUtils.formatNumber(
                    user.phoneNumber,
                    Locale.getDefault().getCountry()
                )
            }
        })

        nameValueTextView.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_edit_name)
//            val details = AutoServiceDetailsFragment(it.autoService.id)
//            val manager = childFragmentManager
//            if (manager != null) {
//                val editNameFragment = EditNameFragment()
//                val transaction = manager.beginTransaction()
//                transaction.add(R.id.fragment_profile, editNameFragment)
//                transaction.addToBackStack(null)
//                transaction.commit()
//            }
        }

        phoneNumberValueTextView.setOnClickListener {
//            val details = AutoServiceDetailsFragment(it.autoService.id)
//            val manager = childFragmentManager
//            if (manager != null) {
//                val phone = EditPhoneNumberFragment()
//                val transaction = manager.beginTransaction()
//                transaction.add(R.id.fragment_profile, phone)
//                transaction.addToBackStack(null)
//                transaction.commit()
//            }

            findNavController().navigate(R.id.action_navigation_profile_to_editPhoneNumberFragment)
        }

        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.profile_action_logout) {
            Log.w("car swaddle android", "logged out∂")
            activity?.let {
                val db = context?.let { context -> AppDatabase.getDatabase(context) }
                db?.clearAllTables()

                Authentication(it).logout { error, response ->
                    val intent = Intent(activity, PreAuthenticationActivity::class.java)

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

}