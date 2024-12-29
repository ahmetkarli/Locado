package com.locado.app.ui.fragment.detail

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class DetailViewModel
@Inject
constructor(

) : ViewModel() {

    private val _photoUrlList = MutableLiveData<List<String>>()
    val photoUrlListLiveData: LiveData<List<String>> get() = _photoUrlList

    private val _placeDetail = MutableLiveData<Place>()
    val placeDetailLiveData: LiveData<Place> get() = _placeDetail


    fun getPlaceDetail(context: Context, placeId: String){
        val placesClient: PlacesClient = Places.createClient(context)

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.PHONE_NUMBER,
            Place.Field.PHOTO_METADATAS,
            Place.Field.LOCATION,
            Place.Field.INTERNATIONAL_PHONE_NUMBER,
            Place.Field.WEBSITE_URI,
            Place.Field.USER_RATING_COUNT,
            Place.Field.RATING,
            Place.Field.OPENING_HOURS


        )

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            _placeDetail.value = place


            val metadata = place.photoMetadatas
            if (metadata == null || metadata.isEmpty()) {
                Log.e("TAG", "No photo metadata.")
                return@addOnSuccessListener
            }

            getPhotos(context,metadata)

        }



    }

    private fun getPhotos(context: Context,metadata: List<PhotoMetadata>) {

        CoroutineScope(Dispatchers.Main).launch {
            val photoUrlList = mutableListOf<String>()

            metadata.forEach { place ->
                try {
                    val uri = fetchResolvedPhotoUri(context,place)
                    photoUrlList.add(uri.toString())
                } catch (e: ApiException) {
                    Log.e("TAG", "Place not found: ${e.message}")
                }
            }
            _photoUrlList.value = photoUrlList

        }

    }

    private suspend fun fetchResolvedPhotoUri(context: Context, metadata: PhotoMetadata): Uri? {
        val placesClient: PlacesClient = Places.createClient(context)

        val photoRequest = FetchResolvedPhotoUriRequest.builder(metadata)
            .setMaxWidth(500)
            .setMaxHeight(300)
            .build()

        val response = placesClient.fetchResolvedPhotoUri(photoRequest).await()
        return response.uri
    }

}