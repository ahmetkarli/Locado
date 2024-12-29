package com.locado.app.ui.fragment.guide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.locado.app.databinding.ItemPlaceGuideBinding
import com.locado.app.ui.fragment.guide.GuideFragmentDirections
import com.locado.app.ui.fragment.guide.GuidePlaceModel
import com.squareup.picasso.Picasso

class GuidePlaceAdapter(private var context: Context, private var guidePlaces: List<GuidePlaceModel>) :
    RecyclerView.Adapter<GuidePlaceAdapter.GuidePlaceViewHolder>() {

    inner class GuidePlaceViewHolder(val binding: ItemPlaceGuideBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuidePlaceViewHolder {
        return GuidePlaceViewHolder(
            ItemPlaceGuideBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: GuidePlaceViewHolder, position: Int) {
        val currentPlace = guidePlaces[position]
        currentPlace.let {
            holder.binding.apply {
                tvName.text = currentPlace.title
                Picasso.get().load(currentPlace.imageUrl).into(imgPlace)
            }



            holder.binding.root.setOnClickListener {
                val action = GuideFragmentDirections.actionNavigationGuideToGuideDetailFragment(currentPlace.link)
                it.findNavController().navigate(action)
            }
        }


    }

    override fun getItemCount(): Int = guidePlaces.size


}