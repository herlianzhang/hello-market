package com.dpr.hello_market.ui.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpr.hello_market.databinding.ItemActivityBinding
import com.dpr.hello_market.db.activity.ActivityDbModel
import com.dpr.hello_market.helper.Helper

class ActivityAdapter : ListAdapter<ActivityDbModel, ActivityAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ActivityDbModel) {
            binding.executePendingBindings()

            binding.llContent.isVisible = false
            binding.tvId.text = "Order ${item.id}"
            val mAdapter = ActivityProductAdapter()
            binding.rvMain.adapter = mAdapter
            mAdapter.submitList(item.order)

            var totalPrice = 0
            for (product in item.order) {
                totalPrice += product.total.times(product.price).toInt()
            }
            binding.tvTotalPrice.text =
                "Rp ${Helper.convertToPriceFormatWithoutCurrency(totalPrice.toString())}"

            binding.root.setOnClickListener {
                binding.ivExpanded.animate().setDuration(500)
                    .rotation(if (binding.llContent.isVisible) 0f else 180f).start()
                binding.llContent.isVisible = !binding.llContent.isVisible
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemActivityBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ActivityDbModel>() {
        override fun areItemsTheSame(oldItem: ActivityDbModel, newItem: ActivityDbModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ActivityDbModel,
            newItem: ActivityDbModel
        ): Boolean =
            oldItem == newItem
    }
}