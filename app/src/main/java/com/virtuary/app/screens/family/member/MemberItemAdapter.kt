package com.virtuary.app.screens.family.member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.virtuary.app.R
import kotlinx.android.synthetic.main.member_list_item.view.*

class MemberItemAdapter(
    private val artifactsTitle: List<String>,
    private val relatedItemOnClick: (name: String) -> Unit
) :
    RecyclerView.Adapter<MemberItemAdapter.ViewHolder>() {

    // Restrict to show only maximum of 5 items
    override fun getItemCount(): Int {
        return if (artifactsTitle.size > 5) 5 else artifactsTitle.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.member_list_item, parent, false)

        // TODO: change click behaviour for each artifact
        view.member_item_card.setOnClickListener {
            relatedItemOnClick(view.artifact_title.text.toString())
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
