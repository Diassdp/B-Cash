package com.example.bcash.ui.inventory.inventransaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bcash.R
import com.example.bcash.databinding.ItemPopularBinding
import com.example.bcash.service.response.ProductItem

class InventoryTransactionAdapter(private val itemClickListener: ItemClickListener) :
    PagingDataAdapter<ProductItem, InventoryTransactionAdapter.ListViewHolder>(DIFF_ITEM_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ListViewHolder(private val binding: ItemPopularBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductItem) {
            binding.apply {
                tvName.text = data.name
                tvPrice.text = data.price.toString()
                Glide.with(itemView.context)
                    .load(data.photo)
                    .fitCenter()
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(binding.ivImage)

                itemView.setOnClickListener {
                    itemClickListener.onItemClick(data)
                }
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(data: ProductItem)
    }

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<ProductItem>() {
            override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProductItem,
                newItem: ProductItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
