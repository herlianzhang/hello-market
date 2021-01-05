package com.dpr.hello_market.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpr.hello_market.BuildConfig
import com.dpr.hello_market.databinding.ItemBannerBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import timber.log.Timber

class HomeBannerAdapter : ListAdapter<String, HomeBannerAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val storageRef = Firebase.storage

        fun bind(item: String) {
            binding.executePendingBindings()

            getBannerImage(item)
        }

        private fun getBannerImage(link: String) {
            val mRef =
                storageRef.getReferenceFromUrl(BuildConfig.STORAGE_URL)
                    .child(link)
            mRef.downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    try {
                        Glide.with(binding.root.context).load(it.result).into(binding.ivBanner)
                    } catch (e: Exception) {
                        Timber.e("Error fetch category image name $link}")
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemBannerBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }
}