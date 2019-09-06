package com.virtuary.app.home.item

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.virtuary.app.R
import kotlinx.android.synthetic.main.artifact_list_item.view.*

class ArtifactAdapter(private val artifactsTitle: List<String>,
                      private val artifactsRelatedTo: List<String>):
    RecyclerView.Adapter<ArtifactAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return artifactsTitle.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.artifact_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvArtifactTitle.text = artifactsTitle[position]
        holder.tvArtifactRelatedTo.text = artifactsRelatedTo[position]

        // TODO: change click behaviour for each artifact
        holder.tvArtifactCard.setOnClickListener {
            Log.i("ArtifactCard", "Card clicked!")
        }

        // TODO: change artifact image
        holder.ivArtifactImg.setImageResource(R.drawable.ic_launcher_background)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tvArtifactTitle: TextView = view.artifact_title
        var tvArtifactRelatedTo: TextView = view.artifact_related_to
        var tvArtifactCard: CardView = view.artifact_card
        var ivArtifactImg: ImageView = view.artifact_image
    }
}