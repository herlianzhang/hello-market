package com.dpr.hello_market.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpr.hello_market.databinding.ItemCategoryBinding
import com.dpr.hello_market.helper.Helper
import com.dpr.hello_market.vo.Category

class HomeCategoryAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Category, HomeCategoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener)
    }

    class ViewHolder private constructor(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Category, onClickListener: OnClickListener) {
            binding.category = item
            binding.executePendingBindings()

            Helper.setImage(item, binding.ivCategory)

            binding.root.setOnClickListener {
                onClickListener.onItemClicked(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem.picture == newItem.picture

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem == newItem
    }

    interface OnClickListener {
        fun onItemClicked(category: Category)
    }
}