package com.farukaygun.kotlinartbookwithfragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.farukaygun.kotlinartbookwithfragments.model.Art
import com.farukaygun.kotlinartbookwithfragments.databinding.RecyclerViewItemBinding
import com.farukaygun.kotlinartbookwithfragments.view.ArtListFragmentDirections

class ArtListAdapter(val artList : List<Art>) : RecyclerView.Adapter<ArtListAdapter.ArtHolder>() {

    class ArtHolder(val binding : RecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtHolder {
        val binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtHolder(binding)
    }

    override fun getItemCount(): Int {
        return artList.size
    }

    override fun onBindViewHolder(holder: ArtHolder, position: Int) {
        holder.binding.artNameText.text = artList[position].name
        holder.itemView.setOnClickListener {
            val action = ArtListFragmentDirections.actionArtListFragmentToArtDetailsFragment(artList[position].id,"old")
            Navigation.findNavController(it).navigate(action)

        }
    }
}
