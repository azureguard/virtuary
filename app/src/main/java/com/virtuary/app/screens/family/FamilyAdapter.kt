package com.virtuary.app.screens.family

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virtuary.app.R
import com.virtuary.app.databinding.FamilyItemViewBinding

class FamilyAdapter(
    private val memberOnClick: (name: String) -> Unit
) : ListAdapter<User, FamilyAdapter.ViewHolder>(UserDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, memberOnClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: FamilyItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) {
            binding.familyMemberName.text = item.name

            // TODO: change artifact image
            binding.familyMemberImage.setImageResource(R.drawable.ic_launcher_background)
        }

        companion object {
            fun from(parent: ViewGroup, memberOnClick: (name: String) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FamilyItemViewBinding.inflate(layoutInflater, parent, false)

                // For now pass only the name to the member detail page
                binding.familyMemberImage.setOnClickListener {
                    memberOnClick(binding.familyMemberName.text.toString())
                }

                return ViewHolder(binding)
            }
        }
    }
}

class UserDiffCallBack : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        // TODO: check ID instead
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

}

