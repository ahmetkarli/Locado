package com.locado.app.ui.fragment.places

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import com.locado.app.helper.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Arrays
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
class PlacesViewModel
@Inject
constructor(

) : ViewModel() {


    private val _distance = MutableLiveData(1000.0)
    val distanceLiveData: LiveData<Double> get() = _distance

    private val _placeName = MutableLiveData("restaurant")
    val placeNameLiveData: LiveData<String> get() = _placeName

    fun updateDistance(distance: Int){
        _distance.value = distance*1000.0

    }

    fun updatePlaceName(index: Int){
        Log.e("TAG", "updatePlaceName: $index ", )
        Log.e("TAG", "updatePlaceName constants: ${Constants.placeValues[index]} ", )
        _placeName.value = Constants.placeValues[index]
    }



    fun getNearBy(context: Context) {

        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)
                searchNearbyPlaces(context,userLocation)
            }
        }


        }

    private fun searchNearbyPlaces(context: Context, userLocation: LatLng) {

        val placesClient: PlacesClient = Places.createClient(context)

        val placeFields = listOf(Place.Field.ID, Place.Field.DISPLAY_NAME,Place.Field.OPENING_HOURS)
        val  circle = CircularBounds.newInstance( userLocation,_distance.value!!)
        val includedTypes = listOf(_placeName.value!!)

        val searchNearbyRequest =
            SearchNearbyRequest.builder(circle, placeFields)
                .setIncludedTypes(includedTypes)
                .build()


        placesClient.searchNearby(searchNearbyRequest)
            .addOnSuccessListener { response ->
                val places = response.places
                places.forEach { place ->
                    Log.e("TAG", "Place Name: ${place.displayName}")
                }

               // getPlaceDetail(context,places[0].id)

            }
            .addOnFailureListener { exception ->
                Log.e("TAG","Error : ${exception.message}")
            }
    }

    private fun getPlaceDetail(context: Context,placeId: String){
        val placesClient: PlacesClient = Places.createClient(context)

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.PHONE_NUMBER,
            Place.Field.PHOTO_METADATAS
        )


        val request = FetchPlaceRequest.newInstance(placeId, placeFields)


        val placeTask: Task<FetchPlaceResponse> = placesClient.fetchPlace(request)

        placeTask.addOnSuccessListener { placeDetail ->
            Log.e("TAG", "getPlaceDetail: ${placeDetail.place}", )
        }



    }


}