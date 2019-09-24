package com.virtuary.app.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virtuary.app.MainNavigationDirections
import com.virtuary.app.R
import com.virtuary.app.databinding.ArtifactListItemBinding
import com.virtuary.app.firebase.Item

class ItemAdapter(
    private val parentFragment: Fragment
) :
    ListAdapter<Item, ItemAdapter.ViewHolder>(ItemDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, parentFragment)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: ArtifactListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {

            binding.artifactTitle.text = item.name
            binding.artifactRelatedTo.text = item.relations?.joinToString(separator = ", ")
            binding.artifactCurrentLocation.text = item.currentLocation

            // TODO: change artifact image
            binding.artifactImage.setImageResource(R.drawable.ic_launcher_background)
        }

        companion object {
            fun from(parent: ViewGroup, parentFragment: Fragment): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ArtifactListItemBinding.inflate(layoutInflater, parent, false)

                // TODO: pass id on navigation instead of title
                binding.artifactCard.setOnClickListener {
                    parentFragment.findNavController()
                        .navigate(MainNavigationDirections.actionGlobalItemFragment(binding.artifactTitle.text.toString()))
                }

                return ViewHolder(binding)
            }
        }
    }
}

class ItemDiffCallBack : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        // TODO: check ID instead
        return oldItem.documentId == newItem.documentId
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

}