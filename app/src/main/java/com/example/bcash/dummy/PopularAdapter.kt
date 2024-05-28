package com.example.bcash.dummy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.R

class PopularAdapter(private val itemList: List<PopularItem>) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.pic)
        val titleText: TextView = view.findViewById(R.id.titleTxt)
        val feeText: TextView = view.findViewById(R.id.feeTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_popular, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.imageView.setImageResource(item.image)
        holder.titleText.text = item.title
        holder.feeText.text = item.price
    }

    override fun getItemCount(): Int = itemList.size
}
