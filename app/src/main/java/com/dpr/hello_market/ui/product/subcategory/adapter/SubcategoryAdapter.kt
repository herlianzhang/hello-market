package com.dpr.hello_market.ui.product.subcategory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpr.hello_market.BuildConfig
import com.dpr.hello_market.databinding.ItemSubcategoryBinding
import com.dpr.hello_market.helper.Helper
import com.dpr.hello_market.vo.Category
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class SubcategoryAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Category, SubcategoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener)
    }

    class ViewHolder private constructor(private val binding: ItemSubcategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Category, onClickListener: OnClickListener) {
            binding.subcategory = item
            binding.executePendingBindings()
            Timber.d("masuk pak eko item $item")

            Helper.setImage(item, binding.ivSubcategory)

            binding.root.setOnClickListener {
                onClickListener.onItemClicked(item.name ?: "")
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSubcategoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem == newItem
    }

    interface OnClickListener {
        fun onItemClicked(subcategory: String)
    }
}