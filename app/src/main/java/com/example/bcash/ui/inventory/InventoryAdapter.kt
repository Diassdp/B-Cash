package com.example.bcash.ui.inventory

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bcash.R
import com.example.bcash.databinding.ItemPopularBinding
import com.example.bcash.service.response.data.ProductItem
import com.example.bcash.ui.detail.DetailInventoryActivity
import java.text.NumberFormat
import java.util.Locale

class InventoryAdapter : PagingDataAdapter<ProductItem, InventoryAdapter.ListViewHolder>(DIFF_ITEM_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ListViewHolder(private val binding: ItemPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductItem) {
            binding.apply {
                tvName.text = data.name
                tvPrice.text = data.price?.let { formatPrice(it.toInt()) }
                Glide.with(itemView.context)
                    .load(data.photo)
                    .fitCenter()
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(binding.ivImage)

                itemView.setOnClickListener {
                    navigateToDetail(data)
                }
            }
        }

        private fun formatPrice(price: Int): String {
            val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
            return "Rp" + format.format(price)
        }

        private fun navigateToDetail(data: ProductItem) {
            val intent = Intent(itemView.context, DetailInventoryActivity::class.java).apply {
                putExtra(DetailInventoryActivity.EXTRA_DATA, data)
            }

            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                itemView.context as Activity,
                androidx.core.util.Pair(binding.ivImage, "image"),
                androidx.core.util.Pair(binding.tvName, "title"),
                androidx.core.util.Pair(binding.tvPrice, "price"),
            )
            itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
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
