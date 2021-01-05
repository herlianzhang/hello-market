package com.dpr.hello_market.ui.product.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpr.hello_market.BuildConfig
import com.dpr.hello_market.databinding.ItemProductBinding
import com.dpr.hello_market.helper.Helper
import com.dpr.hello_market.vo.Product
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import timber.log.Timber

class ProductAdapter : ListAdapter<Product, ProductAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val storageRef = Firebase.storage

        fun bind(item: Product) {
            binding.product = item
            binding.executePendingBindings()

            binding.tvPrice.text =
                "Rp ${Helper.convertToPriceFormatWithoutCurrency(item.price.toString())} / ${item.unit}"

            getProductImage(item)
        }

        private fun getProductImage(product: Product) {
            val mRef =
                storageRef.getReferenceFromUrl(BuildConfig.STORAGE_URL)
                    .child(product.picture ?: "")
            mRef.downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    try {
                        Glide.with(binding.root.context).load(it.result).into(binding.ivProduct)
                    } catch (e: Exception) {
                        Timber.e("Error fetch category image name $product}")
                    }
                }
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

}