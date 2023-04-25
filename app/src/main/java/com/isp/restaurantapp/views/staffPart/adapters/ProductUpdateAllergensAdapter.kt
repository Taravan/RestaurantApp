package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvAllergenUpdateBinding
import com.isp.restaurantapp.models.dto.AllergenDTO
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class ProductUpdateAllergensAdapter(private val viewModel: StaffGoodsVM, private var allergens: List<AllergenDTO> = emptyList()):
    RecyclerView.Adapter<ProductUpdateAllergensAdapter.ProductUpdateAllergenViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductUpdateAllergenViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvAllergenUpdateBinding>(
            inflater,
            R.layout.staff_rv_allergen_update,
            parent,
            false
        )
        return ProductUpdateAllergenViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allergens.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newAllergens: List<AllergenDTO>) {
        this.allergens = newAllergens
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductUpdateAllergenViewHolder, position: Int) {
        holder.bind(allergens[position], viewModel)
    }

    inner class ProductUpdateAllergenViewHolder(private val binding: StaffRvAllergenUpdateBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(allergen: AllergenDTO, viewModel: StaffGoodsVM){

            binding.allergen = allergen
            binding.viewmodel = viewModel

            binding.executePendingBindings()

        }

    }

}