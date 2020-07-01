package com.carswaddle.carswaddleandroid.activities.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService

class AutoServicesListFragment : Fragment() {

    private lateinit var autoServicesListViewModel: AutoServicesListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        autoServicesListViewModel = ViewModelProviders.of(this).get(AutoServicesListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        autoServicesListViewModel.autoServices.observe(this, Observer<List<AutoService>>{ autoServices ->

        })

////        val textView: TextView = root.findViewById(R.id.text_home)
////        autoServicesListViewModel.text.observe(viewLifecycleOwner, Observer {
////            textView.text = it
////        })
//
//        autoServicesListViewModel.also {
//
//        }

//        val model: AutoServicesListViewModel by viewModels()
//        model.getUsers().observe(this, Observer<List<User>>{ users ->
//            // update UI
//        })

        return root
    }
}
