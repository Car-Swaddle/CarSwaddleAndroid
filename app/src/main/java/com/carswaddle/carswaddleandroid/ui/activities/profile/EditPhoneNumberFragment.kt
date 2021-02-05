package com.carswaddle.carswaddleandroid.ui.activities.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.ui.LabeledEditText


class EditPhoneNumberFragment() : Fragment() {

    private lateinit var editPhoneNumberViewModel: EditPhoneNumberViewModel

    private lateinit var phoneNumberEditText: LabeledEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editPhoneNumberViewModel = ViewModelProviders.of(this).get(EditPhoneNumberViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_edit_phone_number, container, false)

        phoneNumberEditText = root.findViewById(R.id.phoneNumberLabeledEditText)
//        lastNameLabeledEditText = root.findViewById(R.id.lastNameLabeledEditText)

        phoneNumberEditText.labelText = getString(R.string.phone_number)

        editPhoneNumberViewModel.currentUser.observe(viewLifecycleOwner, Observer<User> { user ->
            phoneNumberEditText.editTextValue = user.phoneNumber
        })

        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.finish_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_done) {
            Log.w("car swaddle android", "options")
//            editPhoneNumberViewModel.updateName(firstNameLabeledEditText.editTextValue ?: null, lastNameLabeledEditText.editTextValue ?: null, {
            val number = phoneNumberEditText.editTextValue
            if (number != null) {
                editPhoneNumberViewModel.updatePhoneNumber(number, {
                    childFragmentManager.popBackStack()
                }) { error ->
                    Log.w("carswaddle android", "got back from updating user name")
                    if (error != null) {
                        activity?.baseContext.let {
                            Toast.makeText(it, "Car Swaddle was unable to update your name", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
        childFragmentManager.popBackStack()
        activity?.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}