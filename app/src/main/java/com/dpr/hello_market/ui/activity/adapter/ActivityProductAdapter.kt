package com.dpr.hello_market.ui.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpr.hello_market.databinding.ItemActivityProductBinding
import com.dpr.hello_market.db.cart.CartDbModel

class ActivityProductAdapter : ListAdapter<CartDbModel, ActivityProductAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemActivityProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartDbModel) {
            binding.item = item
            binding.executePendingBindings()

            binding.tvWeight.text = "${item.total}${item.unit}"
            binding.tvTotalPrice.text = String.format("#.###,#", (item.total * item.price) / 1000)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemActivityProductBinding.inflate(layoutInflater, parent, false)
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
}