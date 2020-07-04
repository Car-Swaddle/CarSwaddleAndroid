package com.carswaddle.carswaddleandroid.activities.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService

class AutoServicesListFragment : Fragment() {

    private lateinit var autoServicesListViewModel: AutoServicesListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        autoServicesListViewModel = ViewModelProviders.of(this).get(AutoServicesListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        autoServicesListViewModel.autoServices.observe(this, Observer<List<AutoServiceListElements>>{ autoServices ->

        })

        viewManager = LinearLayoutManager(activity?.applicationContext)
//        viewAdapter = MyAdapter(myDataset)

//        recyclerView = findViewById<RecyclerView>(R.id.autoservices_recycler_view).apply {
//            // use this setting to improve performance if you know that changes
//            // in content do not change the layout size of the RecyclerView
//            setHasFixedSize(true)
//
//            // use a linear layout manager
//            layoutManager = viewManager
//
//            // specify an viewAdapter (see also next example)
//            adapter = viewAdapter
//
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
