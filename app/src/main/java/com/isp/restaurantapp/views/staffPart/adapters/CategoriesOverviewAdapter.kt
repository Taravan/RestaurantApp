package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvCategoryOverviewBinding
import com.isp.restaurantapp.models.dto.CategoryDTO
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class CategoriesOverviewAdapter(private val viewModel: StaffGoodsVM, private val callback: Callback, private var categories: List<CategoryDTO> = emptyList()):
    RecyclerView.Adapter<CategoriesOverviewAdapter.CategoryOverviewViewHolder>(){

    interface Callback{
        fun updateCategoryDialog(category: CategoryDTO)
    }

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

            binding.cardCategoryOverview.setOnLongClickListener {
                callback.updateCategoryDialog(category)
                return@setOnLongClickListener true
            }

//            binding.cardCategoryOverview.setOnClickListener {
//                callback.updateCategoryDialog(category)
//            }

            binding.btnDeleteCategory.setOnClickListener {
                deleteCategoryConfirmationDialog(binding.root.context, category)
            }

            binding.executePendingBindings()

        }

    }

    private fun deleteCategoryConfirmationDialog(context: Context, category: CategoryDTO) {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.getString(R.string.category_delete, category.name))
        builder.setPositiveButton(context.getString(R.string.btn_delete)) { _, _ ->
            viewModel.deleteCategory(category.id)
        }
        builder.setNegativeButton(context.getString(R.string.btn_cancel), null)
        val dialog = builder.create()
        dialog.show()
    }

}