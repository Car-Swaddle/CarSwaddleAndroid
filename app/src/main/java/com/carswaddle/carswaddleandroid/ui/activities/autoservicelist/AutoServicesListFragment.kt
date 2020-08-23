package com.carswaddle.carswaddleandroid.activities.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails.AutoServiceDetailsFragment
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListAdapter
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements


class AutoServicesListFragment : Fragment(), View.OnClickListener {

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
        val root = inflater.inflate(R.layout.fragment_autoservices_list, container, false)

        autoServicesListViewModel.autoServices.observe(viewLifecycleOwner, Observer<List<AutoServiceListElements>>{ autoServices ->
            viewAdapter.notifyDataSetChanged()
        })

        viewManager = LinearLayoutManager(activity?.applicationContext)

        viewAdapter = AutoServiceListAdapter(autoServicesListViewModel.autoServices) {
            val manager = fragmentManager
            if (manager != null) {
                val details = AutoServiceDetailsFragment(it.autoService.id)
                val transaction = manager.beginTransaction()
                transaction.add(R.id.autoservices_fragment, details)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        recyclerView = root.findViewById<RecyclerView>(R.id.autoservice_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        return root
    }

    override fun onClick(v: View?) {

    }
}
