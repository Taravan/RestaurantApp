package com.isp.restaurantapp.views.customerPart.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ItemMenuBinding
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import com.isp.restaurantapp.viewModels.CustomerActivityVM
import com.isp.restaurantapp.viewModels.MenuHolderVM

class MenuAdapter(
    private var itemsList: List<GoodsItemDTO> = emptyList(),
    private val viewModel: MenuHolderVM,
    private val activityViewModel: CustomerActivityVM):
    RecyclerView.Adapter<MenuAdapter.ItemsViewHolder>() {


    inner class ItemsViewHolder(private val binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(goodsItem: GoodsItemDTO, viewModel: MenuHolderVM){
            binding.item = goodsItem
            binding.viewModel = viewModel


            // TODO: Je potřeba dodělat tuhle metodu, těžko říct, jestli půjde nacpat uid rovnou
            //  nebo bude potřeba udělat nějaký binding

            // TODO: TADY JE TABLE ID ZASE NAPEVNO
            binding.btnOrder.setOnClickListener {
                orderConfirmationDialog(binding.root.context, goodsItem)
            }

            binding.itemName.setOnLongClickListener {
                itemDescriptionDialog(binding.root.context, goodsItem)
                return@setOnLongClickListener true
            }

            binding.root.context
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemMenuBinding>(
            inflater,
            R.layout.item_menu,
            parent,
            false
        )
        return ItemsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(itemsList[position], viewModel)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newGoodsItemList: List<GoodsItemDTO>) {
        this.itemsList = newGoodsItemList
        notifyDataSetChanged()
    }

    private fun itemDescriptionDialog(context: Context, item: GoodsItemDTO) {
        if (item.goodsDesc != null) {
            if (item.goodsDesc != "") {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(item.goodsName)
                builder.setMessage(item.goodsDesc)
                builder.setNeutralButton(R.string.btn_close, null)
                builder.create().show()
            }
        }
    }

    private fun orderConfirmationDialog(context: Context, goodsItem: GoodsItemDTO){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.string_buy_confirmation_title)

        val msg: String = context.getString(
            R.string.string_buy_confirmation_text,
            " ${goodsItem.goodsName}, ${goodsItem.price}")
        builder.setMessage(msg)

        builder.setPositiveButton(R.string.btn_Ok) { _, _ ->
            // Perform action when "ok" button is clicked
            orderItem(goodsItem)
        }
        builder.setNegativeButton(R.string.btn_cancel, null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun orderItem(goodsItem: GoodsItemDTO) {
        val tableId = activityViewModel.table.value?.id ?: -1
        val tableNumber = activityViewModel.table.value?.tableNumber ?: -1
        val uid = activityViewModel.user.value?.uid ?: ""
        Log.i("MenuAdapter", "insert: tableId=$tableId, uid=$uid")
        viewModel.orderButtonClicked(
            goodsItem,
            tableId, tableNumber,
            uid
        )
    }


}