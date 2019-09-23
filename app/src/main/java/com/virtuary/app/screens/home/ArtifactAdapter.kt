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

class ArtifactAdapter(
    private val parentFragment: Fragment
) :
    ListAdapter<Artifact, ArtifactAdapter.ViewHolder>(ArtifactDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, parentFragment)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: ArtifactListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Artifact) {
            binding.artifactTitle.text = item.title
            binding.artifactRelatedTo.text = item.relatedTo
            binding.artifactCurrentLocation.text = item.location

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

class ArtifactDiffCallBack : DiffUtil.ItemCallback<Artifact>() {
    override fun areItemsTheSame(oldItem: Artifact, newItem: Artifact): Boolean {
        // TODO: check ID instead
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Artifact, newItem: Artifact): Boolean {
        return oldItem == newItem
    }

}
