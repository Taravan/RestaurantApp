package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.isp.restaurantapp.R
import com.isp.restaurantapp.views.customerPart.adapters.PayAdapter
import com.isp.restaurantapp.databinding.FragmentPayBinding
import com.isp.restaurantapp.viewModels.CustomerActivityVM
import com.isp.restaurantapp.viewModels.PayVM

class PayFragment : Fragment() {

    private val activityViewModel: CustomerActivityVM by activityViewModels()
    private lateinit var viewModel: PayVM
    private lateinit var binding: FragmentPayBinding
    private lateinit var adapter: PayAdapter

    companion object{
        const val TAG = "PayFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay, container, false)
        viewModel = ViewModelProvider(this)[PayVM::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // TODO: Tady to není napojený na stůl!!
        viewModel.fetchUnpaidItems(1)

        adapter = PayAdapter(viewModel)
        val recyclerView = binding.itemsToPayRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter

        activityViewModel.user.observe(viewLifecycleOwner){

        }

        viewModel.unpaidItems.observe(viewLifecycleOwner) { unpaidItems ->
            adapter.updateData(unpaidItems)
        }

        /*
        * This is just for checking all checkboxes in recyclerview
        * No other logic here...
         */
        binding.checkAllItemsToPay.setOnCheckedChangeListener { _, isChecked ->
            allItemsChecker(isChecked)
        }
        // END of checkAll

        binding.btnPayAll.setOnClickListener {
            payForItems()
        }

        return binding.root
    }

    fun allItemsChecker(isChecked: Boolean){
        for (i in 0 until adapter.itemCount) {
            val vh = binding.itemsToPayRecyclerView.findViewHolderForAdapterPosition(i)
            if (vh != null) {
                val checkBox = vh.itemView.findViewById<CheckBox>(R.id.checkItemToPay)
                checkBox.isChecked = isChecked
            }
        }
    }


    fun payForItems(){
        if (viewModel.selectedItemsToPay.isEmpty()){
            Toast.makeText(context, R.string.pay_nothing_to_pay_for, Toast.LENGTH_SHORT).show()
            return
        }

        val uid = activityViewModel.user.value?.uid ?: ""
        Log.i(TAG, "uid for payment: $uid")

        try {
            viewModel.payForSelectedItems(uid)
            allItemsChecker(false)
            Toast.makeText(context, R.string.pay_wait_for_waiter, Toast.LENGTH_LONG).show()
        } catch (e: Exception){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

}