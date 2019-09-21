package com.virtuary.app.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.virtuary.app.MainNavigationDirections
import com.virtuary.app.R
import kotlinx.android.synthetic.main.artifact_list_item.view.*

class ArtifactAdapter(
    private val artifactsTitle: List<String>,
    private val artifactsRelatedTo: List<String>,
    private val artifactsLocation: List<String>,
    private val parentFragment: Fragment
) :
    RecyclerView.Adapter<ArtifactAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return artifactsTitle.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.artifact_list_item, parent, false)

        // TODO: change click behaviour for each artifact
        view.artifact_card.setOnClickListener {
            parentFragment.findNavController()
                .navigate(MainNavigationDirections.actionGlobalItemFragment(view.artifact_title.text.toString()))
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvArtifactTitle.text = artifactsTitle[position]
        holder.tvArtifactLocation.text = artifactsLocation[position]
        holder.tvArtifactRelatedTo.text = artifactsRelatedTo[position]

        // TODO: change artifact image
        holder.ivArtifactImg.setImageResource(R.drawable.ic_launcher_background)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvArtifactTitle: TextView = view.artifact_title
        var tvArtifactLocation: TextView = view.artifact_current_location
        var tvArtifactRelatedTo: TextView = view.artifact_related_to
        var ivArtifactImg: ImageView = view.artifact_image
    }
}
