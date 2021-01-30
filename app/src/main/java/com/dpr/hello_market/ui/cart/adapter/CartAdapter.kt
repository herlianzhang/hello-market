package com.dpr.hello_market.ui.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpr.hello_market.databinding.ItemCartBinding
import com.dpr.hello_market.db.cart.CartDbModel
import com.dpr.hello_market.helper.Helper

class CartAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<CartDbModel, CartAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener)
    }

    class ViewHolder private constructor(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartDbModel, onClickListener: OnClickListener) {
            binding.item = item
            binding.executePendingBindings()
            binding.tvPrice.text =
                "Rp ${Helper.convertToPriceFormatWithoutCurrency(item.price.toString())}/${item.unit}"
            binding.tvTotalPrice.text =
                "Rp ${Helper.convertToPriceFormatWithoutCurrency((item.price * item.total).toString())}"

            binding.tvTotal.text = item.total.toString()

            binding.ivMin.setOnClickListener {
                if (item.total <= 1) {

                } else {
                    onClickListener.onReplaceCart(item.copy(total = item.total - 1))
                }
            }

            binding.ivPlus.setOnClickListener {
                onClickListener.onReplaceCart(item.copy(total = item.total + 1))
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCartBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CartDbModel>() {
        override fun areItemsTheSame(oldItem: CartDbModel, newItem: CartDbModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartDbModel, newItem: CartDbModel): Boolean =
            oldItem == newItem

    }

    interface OnClickListener {
        fun onReplaceCart(cart: CartDbModel)
        fun onRemoveCart(cart: CartDbModel)
    }
}