package com.virtuary.app.screens.family

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.virtuary.app.R
import kotlinx.android.synthetic.main.family_item_view.view.*

class FamilyAdapter(
    private val familyMemberName: List<String>,
    private val memberOnClick: (name: String) -> Unit
) : RecyclerView.Adapter<FamilyAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return familyMemberName.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.family_item_view, parent, false)

        // For now pass only the name to the member detail page
        view.family_member_image.setOnClickListener {
            memberOnClick(view.family_member_name.text.toString())
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivFamilyMemberImage.setImageResource(R.drawable.ic_launcher_background)
        holder.tvFamilyMemberName.text = familyMemberName[position]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivFamilyMemberImage: ImageView = view.family_member_image
        var tvFamilyMemberName: TextView = view.family_member_name
    }
}
