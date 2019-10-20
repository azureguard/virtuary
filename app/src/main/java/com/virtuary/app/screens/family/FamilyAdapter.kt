package com.virtuary.app.screens.family

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virtuary.app.R
import com.virtuary.app.databinding.FamilyItemViewBinding
import com.virtuary.app.firebase.StorageRepository
import com.virtuary.app.firebase.User
import com.virtuary.app.util.GlideApp

class FamilyAdapter(
    private val memberOnClick: (user: User) -> Unit,
    private val parentFragment: Fragment,
    private val currUserId: String
) : ListAdapter<User, FamilyAdapter.ViewHolder>(UserDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), parentFragment, memberOnClick, currUserId)
    }

    class ViewHolder private constructor(val binding: FamilyItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, parentFragment: Fragment, memberOnClick: (user: User) -> Unit, currUserId: String) {
            if(user.alias != null){
                if(user.alias!!.containsKey(currUserId)){
                    binding.familyMemberName.text = user.alias!![currUserId]
                } else {
                    binding.familyMemberName.text = user.name
                }
            } else {
                binding.familyMemberName.text = user.name
            }

            GlideApp.with(parentFragment)
                .load(StorageRepository().getImage(user.image))
                .placeholder(R.drawable.ic_no_image)
                .centerCrop()
                .into(binding.familyMemberImage)

            // For now pass only the name to the member detail page
            binding.familyMemberImage.setOnClickListener {
                memberOnClick(user)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FamilyItemViewBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class UserDiffCallBack : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.documentId == newItem.documentId
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

