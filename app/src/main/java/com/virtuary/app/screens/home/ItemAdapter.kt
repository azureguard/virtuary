package com.virtuary.app.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.virtuary.app.MainNavigationDirections
import com.virtuary.app.R
import com.virtuary.app.databinding.ArtifactListItemBinding
import com.virtuary.app.firebase.Item
import com.virtuary.app.firebase.StorageRepository
import com.virtuary.app.util.GlideApp


class ItemAdapter(
    private val parentFragment: Fragment
) :
    ListAdapter<Item, ItemAdapter.ViewHolder>(ItemDiffCallBack()),
    ListPreloader.PreloadModelProvider<Item> {
    override fun getPreloadItems(position: Int): MutableList<Item> {
        return try {
            mutableListOf(getItem(position), getItem(position + 1))
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    override fun getPreloadRequestBuilder(item: Item): RequestBuilder<*>? {
        val thumbnail = item.image?.split('/') as MutableList?
        if (thumbnail != null) {
            thumbnail.last()
            thumbnail[thumbnail.lastIndex] = "thumb_" + thumbnail.last()
        }
        return GlideApp.with(parentFragment)
            .load(StorageRepository().getImage(thumbnail?.joinToString(separator = "/")))
            .centerCrop()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), parentFragment)
    }

    class ViewHolder private constructor(val binding: ArtifactListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, parentFragment: Fragment) {
            binding.artifactCard.setOnClickListener {
                parentFragment.findNavController()
                    .navigate(MainNavigationDirections.actionGlobalItemFragment(item))
            }
            binding.artifactTitle.text = item.name
            binding.artifactRelatedTo.text = item.relations?.joinToString(separator = ", ")
            binding.artifactCurrentLocation.text = item.currentLocation
            val thumbnail = item.image?.split('/') as MutableList?
            if (thumbnail != null) {
                thumbnail.last()
                thumbnail[thumbnail.lastIndex] = "thumb_" + thumbnail.last()
            }
            GlideApp.with(parentFragment)
                .load(StorageRepository().getImage(thumbnail?.joinToString(separator = "/")))
                .placeholder(R.drawable.ic_launcher_foreground).centerCrop()
                .into(binding.artifactImage)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ArtifactListItemBinding.inflate(layoutInflater, parent, false)

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
