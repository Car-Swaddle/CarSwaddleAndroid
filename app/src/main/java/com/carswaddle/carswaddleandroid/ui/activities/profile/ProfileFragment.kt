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
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.activities.ui.LoginActivity
import com.carswaddle.carswaddleandroid.data.Authentication
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.ui.activities.PreAuthenticationActivity
import com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails.AutoServiceDetailsFragment
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

        profileViewModel.currentUser.observeForever {
            nameValueTextView.text = it.displayName()
            phoneNumberValueTextView.text = PhoneNumberUtils.formatNumber(
                it.phoneNumber,
                Locale.getDefault().getCountry()
            )
        }

        profileViewModel.currentUser.observe(viewLifecycleOwner, Observer<User> { user ->
            nameValueTextView.text = user.displayName()
            phoneNumberValueTextView.text = PhoneNumberUtils.formatNumber(
                user.phoneNumber,
                Locale.getDefault().getCountry()
            )
        })

        nameValueTextView.setOnClickListener {
//            val details = AutoServiceDetailsFragment(it.autoService.id)
            val manager = childFragmentManager
            if (manager != null) {
                val editNameFragment = EditNameFragment()
                val transaction = manager.beginTransaction()
                transaction.add(R.id.fragment_profile, editNameFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        phoneNumberValueTextView.setOnClickListener {
//            val details = AutoServiceDetailsFragment(it.autoService.id)
            val manager = childFragmentManager
            if (manager != null) {
                val phone = EditPhoneNumberFragment()
                val transaction = manager.beginTransaction()
                transaction.add(R.id.fragment_profile, phone)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.profile_action_logout) {
            Log.w("car swaddle android", "logged out∂")
            activity?.let {
                Authentication(it).logout { error, response ->
                    val intent = Intent(activity, PreAuthenticationActivity::class.java)

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.logout -> {
//            // User chose the "Settings" item, show the app settings UI...
//            Log.w("tag app name", "Option logout selected")
//            true
//        }
//        else -> {
//            // If we got here, the user's action was not recognized.
//            // Invoke the superclass to handle it.
//            super.onOptionsItemSelected(item)
//        }
//    }

}