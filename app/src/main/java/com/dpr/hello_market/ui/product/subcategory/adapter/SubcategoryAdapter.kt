package com.dpr.hello_market.ui.product.subcategory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpr.hello_market.BuildConfig
import com.dpr.hello_market.databinding.ItemSubcategoryBinding
import com.dpr.hello_market.vo.Category
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import timber.log.Timber

class SubcategoryAdapter : ListAdapter<Category, SubcategoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemSubcategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private val storageRef = Firebase.storage

        fun bind(item: Category) {
            binding.subcategory = item
            binding.executePendingBindings()
            Timber.d("masuk pak eko item $item")
            getCategoryIcon(item)
        }

        private fun getCategoryIcon(category: Category) {
            val mRef =
                storageRef.getReferenceFromUrl(BuildConfig.STORAGE_URL)
                    .child(category.picture ?: "")
            mRef.downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    try {
                        Glide.with(binding.root.context).load(it.result).into(binding.ivSubcategory)
                    } catch (e: Exception) {
                        Timber.e("Error fetch category image name $category}")
                    }
                }
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
}