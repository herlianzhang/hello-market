package com.dpr.hello_market.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpr.hello_market.databinding.ItemCategoryBinding
import com.dpr.hello_market.vo.Category
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import timber.log.Timber

class HomeCategoryAdapter : ListAdapter<Category, HomeCategoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val storageRef = Firebase.storage

        fun bind(item: Category) {
            binding.category = item
            binding.executePendingBindings()

            getCategoryIcon(item)

            binding.root.setOnClickListener {
                Toast.makeText(binding.root.context, "Coming soon", Toast.LENGTH_SHORT).show()
            }
        }

        private fun getCategoryIcon(category: Category) {
            val mRef =
                storageRef.getReferenceFromUrl("gs://hello-market-dpr.appspot.com")
                    .child(category.picture ?: "")
            mRef.downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    try {
                        Glide.with(binding.root.context).load(it.result).into(binding.ivCategory)
                    } catch (e: Exception) {
                        Timber.e("Error fetch category image name $category}")
                    }
                }
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
}