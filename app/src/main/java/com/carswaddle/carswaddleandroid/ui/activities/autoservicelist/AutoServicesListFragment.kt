package com.carswaddle.carswaddleandroid.activities.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.CarSwaddleApp.CarSwaddleApp
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListAdapter
import com.carswaddle.carswaddleandroid.ui.activities.schedule.MapsActivity


class AutoServicesListFragment : Fragment() {

    private lateinit var autoServicesListViewModel: AutoServicesListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: AutoServiceListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var scheduleButton: Button
    private lateinit var autoServiceEmptyState: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        autoServicesListViewModel =
            ViewModelProvider(requireActivity()).get(AutoServicesListViewModel::class.java)
        ViewModelProvider(requireActivity())
        val root = inflater.inflate(R.layout.fragment_autoservices_list, container, false)
        
        autoServiceEmptyState = root.findViewById(R.id.autoServiceEmptyState)
        
        // Hide until we've made the request and know if we should show it or not
        autoServiceEmptyState.visibility = View.GONE

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                reloadList()
            }
        }, IntentFilter(AutoServicesListFragment.UPDATE_AUTOSERVICE_LIST))
        
        scheduleButton = root.findViewById(R.id.scheduleButton)

        autoServicesListViewModel.upcomingAutoServices.observe(viewLifecycleOwner, { autoServices ->
                viewAdapter.notifyDataSetChanged()
                updateEmptyStateDisplay()
            }
        )

        autoServicesListViewModel.pastAutoServices.observe(viewLifecycleOwner, { autoServices ->
                viewAdapter.notifyDataSetChanged()
                updateEmptyStateDisplay()
            }
        )
        
        viewManager = LinearLayoutManager(activity?.applicationContext)

        viewAdapter = AutoServiceListAdapter(autoServicesListViewModel.upcomingAutoServices, autoServicesListViewModel.pastAutoServices) {
            val manager = childFragmentManager
            if (manager != null) {
                val bundle = bundleOf("autoServiceId" to it.autoService.id)
                findNavController().navigate(R.id.action_navigation_autoservices_list_to_navigation_autoservice_details, bundle)
            }
        }
        viewAdapter?.currentUser = autoServicesListViewModel.currentUser
        
        recyclerView = root.findViewById<RecyclerView>(R.id.autoservice_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        scheduleButton.setOnClickListener {
            didTapSchedule()
        }
        
        return root
    }

    private fun updateEmptyStateDisplay() {
        autoServiceEmptyState.visibility = if (shouldShowEmptyState()) View.VISIBLE else View.GONE 
    }
    
    private fun shouldShowEmptyState(): Boolean {
        return autoServicesListViewModel.pastAutoServices.value?.isNullOrEmpty() == true && autoServicesListViewModel.upcomingAutoServices.value.isNullOrEmpty() == true
    }
    
    private fun didTapSchedule() {
        startActivity(Intent(activity, MapsActivity::class.java))
    }
    
    private fun reloadList() {
        autoServicesListViewModel.loadAutoServices()
    }
    
    companion object {
        const val UPDATE_AUTOSERVICE_LIST = "AutoServicesListFragment.UPDATE_AUTOSERVICE_LIST"
    }

}
