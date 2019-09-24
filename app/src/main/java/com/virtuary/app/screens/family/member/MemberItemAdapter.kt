package com.virtuary.app.screens.family.member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.virtuary.app.R
import com.virtuary.app.firebase.Item
import kotlinx.android.synthetic.main.member_list_item.view.*

class MemberItemAdapter(
    private val artifactsTitle: List<String>,
    private val relatedItemOnClick: (item: Item) -> Unit
) :
    RecyclerView.Adapter<MemberItemAdapter.ViewHolder>() {

    // Restrict to show only maximum of 5 items
    override fun getItemCount(): Int {
        return if (artifactsTitle.size > 5) 5 else artifactsTitle.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.member_list_item, parent, false)

        // TODO: implement query by family member to pass items related
        view.member_item_card.setOnClickListener {
            relatedItemOnClick(Item(name = "A"))
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvArtifactTitle.text = artifactsTitle[position]

        // TODO: change artifact image
        holder.ivArtifactImg.setImageResource(R.drawable.ic_launcher_background)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvArtifactTitle: TextView = view.artifact_title
        var ivArtifactImg: ImageView = view.artifact_image
    }
}
