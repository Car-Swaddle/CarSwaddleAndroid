package com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.activities.ui.home.AutoServicesListViewModel
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListAdapter
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements

class AutoServiceDetailsFragment(val autoServiceId: String) : Fragment() {

//    private lateinit var autoServicesListViewModel: AutoServicesListViewModel
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var viewAdapter: RecyclerView.Adapter<*>
//    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var autoServiceDetailsViewModel: AutoServiceDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_autoservice_details, container, false)

        autoServiceDetailsViewModel = ViewModelProviders.of(this).get(AutoServiceDetailsViewModel::class.java)

        autoServiceDetailsViewModel.autoServiceId = autoServiceId

        autoServiceDetailsViewModel.autoServiceElement.observe(this, Observer<AutoServiceListElements> { autoServices ->

        })

//        autoServicesListViewModel = ViewModelProviders.of(this).get(AutoServicesListViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_autoservices_list, container, false)
//
//        autoServicesListViewModel.autoServices.observe(this, Observer<List<AutoServiceListElements>>{ autoServices ->
//            Log.d("log", "It done changed")
//
//            viewAdapter.notifyDataSetChanged()
//        })
//
//        viewManager = LinearLayoutManager(activity?.applicationContext)
//        viewAdapter = AutoServiceListAdapter(autoServicesListViewModel.autoServices)
//
//        recyclerView = root.findViewById<RecyclerView>(R.id.autoservice_recycler_view).apply {
//            // use this setting to improve performance if you know that changes
//            // in content do not change the layout size of the RecyclerView
//            setHasFixedSize(true)
//
//            // use a linear layout manager
//            layoutManager = viewManager
//
//            // specify an viewAdapter (see also next example)
//            adapter = viewAdapter
//        }

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