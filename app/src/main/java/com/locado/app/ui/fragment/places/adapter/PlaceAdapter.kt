package com.locado.app.ui.fragment.places.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.Place
import com.locado.app.R
import com.locado.app.databinding.ItemPlaceBinding
import com.locado.app.helper.Constants
import com.locado.app.helper.Helper
import com.locado.app.ui.fragment.places.PlacesFragmentDirections
import com.squareup.picasso.Picasso

class PlaceAdapter(private var context: Context, private var places: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(
            ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val currentPlace = places[position]
        currentPlace.let {
            holder.binding.apply {
                tvName.text = currentPlace.displayName
                tvRating.text = currentPlace.rating?.toString() ?: "0.0"
                ratingBar.rating = currentPlace.rating?.toFloat() ?: 0.0f

                val userRatingCount = currentPlace.userRatingCount?: 0
                tvUserCount.text = "($userRatingCount)"


                val distance = Helper.distanceInPreferredUnit(Constants.lastLocation,currentPlace.location)
                tvDistance.text = distance

                if(currentPlace.photoMetadatas!=null){
                    val photoReference = currentPlace.photoMetadatas?.get(0).toString().substringAfter("photoReference=").substringBefore(",")
                    val photoUrl = "${Constants.BASE_URL}place/photo?maxwidth=400&photoreference=$photoReference&key=${context.getString(
                        R.string.places_api_key)}"

                    Picasso.get().load(photoUrl).into(imgPlace)

                }else{
                    imgPlace.setBackgroundDrawable(context.getDrawable(R.drawable.ic_no_place_photo))
                }
                tvShortAdress.text = currentPlace.shortFormattedAddress
                Picasso.get().load(currentPlace.iconMaskUrl).into(imgPlaceType)

            }



            holder.binding.root.setOnClickListener {

                val action = PlacesFragmentDirections.actionNavigationPlacesToDetailFragment(currentPlace.id)
                it.findNavController().navigate(action)

            }
        }


    }

    override fun getItemCount(): Int = places.size


}



