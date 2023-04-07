package com.isp.restaurantapp.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.AllergenDefRowBinding
import com.isp.restaurantapp.models.Allergen
import com.isp.restaurantapp.viewModels.MainActivityVM

class AllergenDefinitionAdapter (
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainActivityVM,  // pass view-model,
    private var allAllergensList: List<Allergen> = emptyList()

) : RecyclerView.Adapter<AllergenDefinitionAdapter.AllergenViewHolder>() {


    inner class AllergenViewHolder(private val binding: AllergenDefRowBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(alg: Allergen) {
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


    fun updateData(newDataset: List<Allergen>){
        this.allAllergensList = newDataset
        notifyDataSetChanged()
    }

}
/*) : RecyclerView.Adapter<AllergenDefinitionAdapter.AllergenViewHolder>() {

    inner class AllergenViewHolder(private val binding: AllergenDefRowBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(alg: Allergen, viewModel: MainActivityVM) {
            // alg defined in red/layout/allergen_def_row.xml data variable
            binding.alg = alg
            binding.user = viewModel.loggedUser.value  // for visibility change

            viewModel.userDefinedAllergens.observe(lifecycleOwner){
                Log.v("Adapter","observe fire")
                binding.switchAllergen.isChecked = viewModel.isAllergenUserDefined(alg)
            }

            binding.switchAllergen.setOnCheckedChangeListener { _, isChecked ->
                viewModel.allergenSwitchAction(isChecked, alg)
            }


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
        val myData = dataset[position]
        holder.bind(myData, viewModel)  // call viewholder.bind method
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


    fun updateData(newDataset: List<Allergen>){
        this.dataset = newDataset
        notifyDataSetChanged()
    }

}*/