package com.virtuary.app.screens.family.member

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virtuary.app.R
import com.virtuary.app.databinding.MemberListItemBinding
import com.virtuary.app.firebase.Item
import com.virtuary.app.firebase.StorageRepository
import com.virtuary.app.util.GlideApp

class MemberItemAdapter(
    private val relatedItemOnClick: (item: Item) -> Unit, private val parentFragment: Fragment
) : ListAdapter<Item, MemberItemAdapter.ViewHolder>(ItemDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), parentFragment, relatedItemOnClick)
    }

    class ViewHolder private constructor(val binding: MemberListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Item, parentFragment: Fragment, relatedItemOnClick: (item: Item) -> Unit) {
            binding.artifactTitle.text = item.name

            GlideApp.with(parentFragment)
                .load(StorageRepository().getImage(item.image))
                .placeholder(R.drawable.ic_no_image)
                .centerCrop()
                .into(binding.artifactImage)

            binding.memberItemCard.setOnClickListener {
                relatedItemOnClick(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MemberListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class ItemDiffCallBack : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.documentId == newItem.documentId
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

}
