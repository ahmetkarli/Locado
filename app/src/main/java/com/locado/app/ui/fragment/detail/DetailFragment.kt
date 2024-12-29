package com.locado.app.ui.fragment.detail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.locado.app.R
import com.locado.app.base.BaseFragment
import com.locado.app.databinding.FragmentDetailBinding
import com.locado.app.helper.Constants
import com.locado.app.helper.Helper
import com.locado.app.ui.fragment.detail.adapter.ImageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(), OnMapReadyCallback {

    private val viewModel: DetailViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()

    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback

    private lateinit var mMap: GoogleMap


    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8,0,8,0)
    }



    override fun initView() {
        showLoading()
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val placeId = args.placeId
        viewModel.getPlaceDetail(requireContext(),placeId)

        binding.tvMaps.paintFlags = binding.tvMaps.paintFlags or Paint.UNDERLINE_TEXT_FLAG



    }

    override fun initListeners() {

        binding.imgBack.setOnClickListener {
            it.findNavController().navigateUp()
        }

        binding.imgPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${binding.tvPhone.text}")
            startActivity(intent)
        }

        binding.tvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${binding.tvPhone.text}")
            startActivity(intent)
        }

        binding.tvWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(binding.tvWebsite.text.toString())
            startActivity(intent)
        }

        binding.imgWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(binding.tvWebsite.text.toString())
            startActivity(intent)
        }

        binding.tvMaps.setOnClickListener {
            openGoogleMapsForDirections(Constants.placeLocation.latitude,Constants.placeLocation.longitude)
        }

        binding.imgMaps.setOnClickListener {
            openGoogleMapsForDirections(Constants.placeLocation.latitude,Constants.placeLocation.longitude)
        }

    }

    override fun initObservers() {

        viewModel.placeDetailLiveData.observe(viewLifecycleOwner){ place ->
            hideLoading()
            Constants.placeLocation = place.location
            binding.tvName.visibility = View.VISIBLE
            binding.tvName.text = place.displayName

            if(place.internationalPhoneNumber!=null){
                binding.imgPhone.visibility = View.VISIBLE
                binding.tvPhone.visibility = View.VISIBLE
                binding.tvPhone.text = place.internationalPhoneNumber
                binding.tvPhone.paintFlags = binding.tvPhone.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            }

            binding.tvRating.text = place.rating?.toString() ?: "0.0"
            binding.ratingBar.rating = place.rating?.toFloat() ?: 0.0f

            val userRatingCount = place.userRatingCount?: 0
            binding.tvUserCount.text = "($userRatingCount)"


            val distance = Helper.distanceInPreferredUnit(Constants.lastLocation,place.location)
            binding.tvDistance.text = distance

            binding.tvAdress2.text = place.formattedAddress

            Log.e("TAG", "website uri: ${place.websiteUri} ", )


            if(place.websiteUri != null){
                binding.imgWebsite.visibility = View.VISIBLE
                binding.tvWebsite.visibility = View.VISIBLE
                binding.tvWebsite.text = place.websiteUri.toString()

                binding.tvWebsite.paintFlags = binding.tvWebsite.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            }

            if(place.openingHours!=null){
                binding.tvOpeningHours.visibility = View.VISIBLE
                val openingHoursText = place.openingHours.weekdayText.joinToString(separator = "\n\n")
                binding.tvOpeningHours.text = openingHoursText
            }

        }


        viewModel.photoUrlListLiveData.observe(viewLifecycleOwner){ photoUrlList ->

            binding.viewpager2.visibility = View.VISIBLE
            setupPhotoSlide(photoUrlList)
        }

    }

    private fun setupPhotoSlide(photoUrlList: List<String>){

        val imageAdapter = ImageAdapter()
        binding.viewpager2.adapter = imageAdapter
        imageAdapter.submitList(photoUrlList)

        val slideDotLL = binding.slideDotLL
        val dotsImage = Array(photoUrlList.size) { ImageView(requireContext()) }

        dotsImage.forEach {
            it.setImageResource(
                R.drawable.non_active_dot
            )
            slideDotLL.addView(it,params)
        }


        dotsImage[0].setImageResource(R.drawable.active_dot)

        pageChangeListener = object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                dotsImage.mapIndexed { index, imageView ->
                    if (position == index){
                        imageView.setImageResource(
                            R.drawable.active_dot
                        )
                    }else{
                        imageView.setImageResource(R.drawable.non_active_dot)
                    }
                }
                super.onPageSelected(position)
            }
        }
        binding.viewpager2.registerOnPageChangeCallback(pageChangeListener)

    }

    fun openGoogleMapsForDirections(latitude: Double, longitude: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps") // Google Maps uygulamasını açar

        if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            val url = "https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)

        }
    }


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater, container, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        viewModel.placeDetailLiveData.observe(viewLifecycleOwner) { place ->
            val location = place.location
            if (location != null) {
                mMap.addMarker(
                    MarkerOptions().position(location).title(place.displayName)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@observe
                }
                mMap.isMyLocationEnabled = true

            }
        }
    }



}