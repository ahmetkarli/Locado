package com.locado.app.helper

import com.google.android.gms.maps.model.LatLng
import com.locado.app.R

object Constants {
    const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    val placeValues = listOf(
        "accounting",
        "airport",
        "amusement_park",
        "aquarium",
        "art_gallery",
        "atm",
        "bakery",
        "bank",
        "bar",
        "beauty_salon",
        "bicycle_store",
        "book_store",
        "bowling_alley",
        "bus_station",
        "cafe",
        "campground",
        "car_dealer",
        "car_rental",
        "car_repair",
        "car_wash",
        "casino",
        "cemetery",
        "church",
        "city_hall",
        "clothing_store",
        "convenience_store",
        "courthouse",
        "dentist",
        "department_store",
        "doctor",
        "electrician",
        "electronics_store",
        "embassy",
        "fire_station",
        "florist",
        "funeral_home",
        "furniture_store",
        "gas_station",
        "gym",
        "hair_care",
        "hardware_store",
        "hindu_temple",
        "home_goods_store",
        "hospital",
        "insurance_agency",
        "jewelry_store",
        "laundry",
        "lawyer",
        "library",
        "light_rail_station",
        "liquor_store",
        "local_government_office",
        "locksmith",
        "lodging",
        "meal_delivery",
        "meal_takeaway",
        "mosque",
        "movie_rental",
        "movie_theater",
        "moving_company",
        "museum",
        "night_club",
        "painter",
        "park",
        "parking",
        "pet_store",
        "pharmacy",
        "physiotherapist",
        "plumber",
        "police",
        "post_office",
        "primary_school",
        "real_estate_agency",
        "restaurant",
        "roofing_contractor",
        "rv_park",
        "school",
        "secondary_school",
        "shoe_store",
        "shopping_mall",
        "spa",
        "stadium",
        "storage",
        "store",
        "subway_station",
        "supermarket",
        "synagogue",
        "taxi_stand",
        "tourist_attraction",
        "train_station",
        "transit_station",
        "travel_agency",
        "university",
        "veterinary_care",
        "zoo"
    )



    val placeTypes = listOf(
        R.string.accounting,
        R.string.airport,
        R.string.amusement_park,
        R.string.aquarium,
        R.string.art_gallery,
        R.string.atm,
        R.string.bakery,
        R.string.bank,
        R.string.bar,
        R.string.beauty_salon,
        R.string.bicycle_store,
        R.string.book_store,
        R.string.bowling_alley,
        R.string.bus_station,
        R.string.cafe,
        R.string.campground,
        R.string.car_dealer,
        R.string.car_rental,
        R.string.car_repair,
        R.string.car_wash,
        R.string.casino,
        R.string.cemetery,
        R.string.church,
        R.string.city_hall,
        R.string.clothing_store,
        R.string.convenience_store,
        R.string.courthouse,
        R.string.dentist,
        R.string.department_store,
        R.string.doctor,
        R.string.electrician,
        R.string.electronics_store,
        R.string.embassy,
        R.string.fire_station,
        R.string.florist,
        R.string.funeral_home,
        R.string.furniture_store,
        R.string.gas_station,
        R.string.gym,
        R.string.hair_care,
        R.string.hardware_store,
        R.string.hindu_temple,
        R.string.home_goods_store,
        R.string.hospital,
        R.string.insurance_agency,
        R.string.jewelry_store,
        R.string.laundry,
        R.string.lawyer,
        R.string.library,
        R.string.light_rail_station,
        R.string.liquor_store,
        R.string.local_government_office,
        R.string.locksmith,
        R.string.lodging,
        R.string.meal_delivery,
        R.string.meal_takeaway,
        R.string.mosque,
        R.string.movie_rental,
        R.string.movie_theater,
        R.string.moving_company,
        R.string.museum,
        R.string.night_club,
        R.string.painter,
        R.string.park,
        R.string.parking,
        R.string.pet_store,
        R.string.pharmacy,
        R.string.physiotherapist,
        R.string.plumber,
        R.string.police,
        R.string.post_office,
        R.string.primary_school,
        R.string.real_estate_agency,
        R.string.restaurant,
        R.string.roofing_contractor,
        R.string.rv_park,
        R.string.school,
        R.string.secondary_school,
        R.string.shoe_store,
        R.string.shopping_mall,
        R.string.spa,
        R.string.stadium,
        R.string.storage,
        R.string.store,
        R.string.subway_station,
        R.string.supermarket,
        R.string.synagogue,
        R.string.taxi_stand,
        R.string.tourist_attraction,
        R.string.train_station,
        R.string.transit_station,
        R.string.travel_agency,
        R.string.university,
        R.string.veterinary_care,
        R.string.zoo
    )

    lateinit var lastLocation:LatLng



}