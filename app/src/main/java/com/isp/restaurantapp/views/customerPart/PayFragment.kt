package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.isp.restaurantapp.R
import com.isp.restaurantapp.views.customerPart.adapters.PayAdapter
import com.isp.restaurantapp.databinding.FragmentPayBinding
import com.isp.restaurantapp.viewModels.PayVM

class PayFragment : Fragment() {

    private lateinit var viewModel: PayVM
    private lateinit var binding: FragmentPayBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay, container, false)
        viewModel = ViewModelProvider(this)[PayVM::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = PayAdapter(viewModel)
        val recyclerView = binding.itemsToPayRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter

        viewModel.unpaidItems.observe(viewLifecycleOwner) { unpaiedItems ->
            adapter.updateData(unpaiedItems)
        }

        return binding.root
    }

}