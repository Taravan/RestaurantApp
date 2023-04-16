package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.databinding.FragmentHistoryBinding
import com.isp.restaurantapp.viewModels.CustomerActivityVM
import com.isp.restaurantapp.viewModels.HistoryVM
import com.isp.restaurantapp.views.customerPart.adapters.HistoryAdapter

class HistoryFragment: Fragment() {

    private val activityViewModel: CustomerActivityVM by activityViewModels()
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: HistoryVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // TODO: [Pro Tomáše]
        //  Do hostorie přidat datum (formát YYYY/MM/DD) a trošku to tam zkrášlit,
        //  mrkni na to Tome prosím, já jsem s tím layoutem uplně marnej.
        //  Je to item_rv_history.xml

        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HistoryVM::class.java]


        val historyAdapter = HistoryAdapter(viewModel)
        binding.recyclerViewOrdersRealtime.adapter = historyAdapter

        activityViewModel.user.observe(viewLifecycleOwner){ user ->
            // if user is authenticated
            if (user != null){
                viewModel.getRealtimeOrders(user.uid).observe(viewLifecycleOwner){
                    historyAdapter.updateData(it)   // whenever realtime emits new values
                }
            }
        }

        return binding.root
    }

}