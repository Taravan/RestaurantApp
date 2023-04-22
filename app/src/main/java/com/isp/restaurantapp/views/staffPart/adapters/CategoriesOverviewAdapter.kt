package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvCategoryOverviewBinding
import com.isp.restaurantapp.models.dto.CategoryDTO
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class CategoriesOverviewAdapter(private val viewModel: StaffGoodsVM, private var categories: List<CategoryDTO> = emptyList()):
    RecyclerView.Adapter<CategoriesOverviewAdapter.CategoryOverviewViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryOverviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvCategoryOverviewBinding>(
            inflater,
            R.layout.staff_rv_category_overview,
            parent,
            false
        )
        return CategoryOverviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCategories: List<CategoryDTO>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CategoryOverviewViewHolder, position: Int) {
        holder.bind(categories[position], viewModel)
    }

    inner class CategoryOverviewViewHolder(private val binding: StaffRvCategoryOverviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryDTO, viewModel: StaffGoodsVM){
            binding.category = category

            binding.cardCategoryOverview.setOnClickListener {

            }

            binding.executePendingBindings()

        }

    }

}