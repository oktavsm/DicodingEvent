package com.dicoding.dicodingevent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent
import com.dicoding.dicodingevent.databinding.ItemEventBinding

class FavoriteAdapter(private val onItemClick: (FavoriteEvent) -> Unit) :
    ListAdapter<FavoriteEvent, FavoriteAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class ViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEvent, onItemClick: (FavoriteEvent) -> Unit) {
            binding.tvItemName.text = event.name
            binding.tvItemSummary.text = event.summary ?: ""
            binding.chipCategory.text = event.category ?: ""
            Glide.with(itemView.context).load(event.imageLogo).into(binding.ivItemImage)
            itemView.setOnClickListener { onItemClick(event) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEvent>() {
            override fun areItemsTheSame(o: FavoriteEvent, n: FavoriteEvent) = o.id == n.id
            override fun areContentsTheSame(o: FavoriteEvent, n: FavoriteEvent) = o == n
        }
    }
}
