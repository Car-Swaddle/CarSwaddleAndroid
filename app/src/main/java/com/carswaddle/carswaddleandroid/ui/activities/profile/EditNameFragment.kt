package com.carswaddle.carswaddleandroid.ui.activities.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.User


class EditNameFragment() : Fragment() {

    private lateinit var editNameViewModel: EditNameViewModel

    private lateinit var firstNameLabeledEditText: LabeledEditText
    private lateinit var lastNameLabeledEditText: LabeledEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editNameViewModel = ViewModelProviders.of(this).get(EditNameViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_edit_name, container, false)

        firstNameLabeledEditText = root.findViewById(R.id.firstNameLabeledEditText)
        lastNameLabeledEditText = root.findViewById(R.id.lastNameLabeledEditText)

        firstNameLabeledEditText.labelText = getString(R.string.first_name_label)
        lastNameLabeledEditText.labelText = getString(R.string.last_name_label)

        editNameViewModel.currentUser.observe(viewLifecycleOwner, Observer<User> { user ->
            firstNameLabeledEditText.editTextValue = user.firstName
            lastNameLabeledEditText.editTextValue = user.lastName
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
            editNameViewModel.updateName(firstNameLabeledEditText.editTextValue ?: null, lastNameLabeledEditText.editTextValue ?: null, {
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
//        childFragmentManager.popBackStack()
        activity?.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}