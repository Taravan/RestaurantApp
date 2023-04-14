package com.isp.restaurantapp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.AllergenDefRowBinding
import com.isp.restaurantapp.models.dto.AllergenDTO
import com.isp.restaurantapp.viewModels.MainActivityVMOLD

class AllergenDefinitionAdapterOLD (
    private val viewModel: MainActivityVMOLD,  // pass view-model,
    private var allAllergensList: List<AllergenDTO> = emptyList()

) : RecyclerView.Adapter<AllergenDefinitionAdapterOLD.AllergenViewHolder>() {


    inner class AllergenViewHolder(private val binding: AllergenDefRowBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(alg: AllergenDTO) {
            // alg defined in red/layout/allergen_def_row.xml data variable
            binding.alg = alg
            binding.user = viewModel.loggedUser.value  // for visibility change
            binding.vm = viewModel

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergenViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<AllergenDefRowBinding>(
            layoutInflater,
            R.layout.allergen_def_row,
            parent,
            false
        )
        return AllergenViewHolder(binding)
    }



    override fun onBindViewHolder(holder: AllergenViewHolder, position: Int) {
        val allergen = allAllergensList[position]
        holder.bind(allergen)  // call viewholder.bind method
    }

    override fun getItemCount(): Int {
        return allAllergensList.size
    }


    fun updateData(newDataset: List<AllergenDTO>){
        this.allAllergensList = newDataset
        notifyDataSetChanged()
    }

}