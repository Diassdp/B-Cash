package com.example.bcash.ui.inventory

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bcash.R
import com.example.bcash.databinding.ItemPopularBinding
import com.example.bcash.service.response.data.ProductItem
import com.example.bcash.ui.detail.DetailInventoryActivity
import java.text.NumberFormat
import java.util.Locale

class InventoryAdapter(private val onItemClicked: (ProductItem) -> Unit) : RecyclerView.Adapter<InventoryAdapter.ListViewHolder>() {

    private var productList: List<ProductItem> = emptyList()

    fun setData(newList: List<ProductItem>) {
        productList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size

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
                    onItemClicked(data)
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
}
