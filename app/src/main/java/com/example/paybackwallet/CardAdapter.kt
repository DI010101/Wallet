package com.example.paybackwallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paybackwallet.databinding.ItemCardBinding

class CardAdapter : ListAdapter<LoyaltyCard, CardAdapter.VH>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<LoyaltyCard>() {
            override fun areItemsTheSame(oldItem: LoyaltyCard, newItem: LoyaltyCard) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: LoyaltyCard, newItem: LoyaltyCard) = oldItem == newItem
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }
    override fun onBindViewHolder(holder: VH, position: Int) { holder.bind(getItem(position)) }
    class VH(private val b: ItemCardBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(card: LoyaltyCard) { b.title.text = card.name; b.subtitle.text = card.code }
    }
}
