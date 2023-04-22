package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvAllergenBinding
import com.isp.restaurantapp.models.dto.AllergenDTO
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class ProductAddAllergensAdapter(private val viewModel: StaffGoodsVM, private var allergens: List<AllergenDTO> = emptyList()):
    RecyclerView.Adapter<ProductAddAllergensAdapter.ProductAddAllergenViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAddAllergenViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvAllergenBinding>(
            inflater,
            R.layout.staff_rv_allergen,
            parent,
            false
        )
        return ProductAddAllergenViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allergens.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newAllergens: List<AllergenDTO>) {
        this.allergens = newAllergens
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductAddAllergenViewHolder, position: Int) {
        holder.bind(allergens[position], viewModel)
    }

    inner class ProductAddAllergenViewHolder(private val binding: StaffRvAllergenBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(allergen: AllergenDTO, viewModel: StaffGoodsVM){

            binding.allergen = allergen

            binding.checkAllergen.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateSelectedList(isChecked, allergen)
            }

            binding.executePendingBindings()

        }

    }

}