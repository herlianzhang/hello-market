package com.dpr.hello_market.ui.product.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpr.hello_market.databinding.ItemProductBinding
import com.dpr.hello_market.helper.Helper
import com.dpr.hello_market.vo.Product

class ProductAdapter(private val onClickListener: OnClickListener) : ListAdapter<Product, ProductAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener)
    }

    class ViewHolder private constructor(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product, onClickListener: OnClickListener) {
            binding.product = item
            binding.executePendingBindings()

            binding.tvPrice.text =
                "Rp ${Helper.convertToPriceFormatWithoutCurrency(item.price.toString())} / ${item.unit}"

            Helper.setImage(item, binding.ivProduct)

            binding.tvOrder.setOnClickListener {
                onClickListener.onItemClicked(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

    interface OnClickListener {
        fun onItemClicked(product: Product)
    }
}