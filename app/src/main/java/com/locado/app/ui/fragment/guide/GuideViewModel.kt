package com.locado.app.ui.fragment.guide

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jsoup.nodes.Document
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locado.app.helper.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GuideViewModel
@Inject
constructor(

) : ViewModel() {

    private val _info= MutableLiveData<String>()
    val infoLiveData: LiveData<String> get() = _info

    private val _cityName= MutableLiveData<String?>()
    val cityNameLiveData: MutableLiveData<String?> get() = _cityName

    val gezilecekYerlerLinkLiveData = MutableLiveData<String>()
    val seyehatHatirasiLinkLiveData = MutableLiveData<String>()
    val gelenekselMutfakLinkLiveData = MutableLiveData<String>()
    val turizmAktiviteleriLinkLiveData = MutableLiveData<String>()
    val kulturAtlasiLinkLiveData = MutableLiveData<String>()


    private val _placeList= MutableLiveData<List<GuidePlaceModel>>()
    val placeListLiveData: LiveData<List<GuidePlaceModel>> get() = _placeList

    private val _seyehatHatirasiList= MutableLiveData<List<GuidePlaceModel>>()
    val seyehatHatirasiLiveData: LiveData<List<GuidePlaceModel>> get() = _seyehatHatirasiList

    private val _gelenekselMutfakList= MutableLiveData<List<GuidePlaceModel>>()
    val gelenekselMutfakLiveData: LiveData<List<GuidePlaceModel>> get() = _gelenekselMutfakList

    private val _turizmAktiviteList= MutableLiveData<List<GuidePlaceModel>>()
    val turizmAktiviteLiveData: LiveData<List<GuidePlaceModel>> get() = _turizmAktiviteList

    private val _kulturAtlasiList= MutableLiveData<List<GuidePlaceModel>>()
    val kulturAtlasiLiveData: LiveData<List<GuidePlaceModel>> get() = _kulturAtlasiList

    fun getCityName(context: Context){
        val geoCoder = Geocoder(context, Locale.getDefault())
        val adress = geoCoder.getFromLocation(Constants.lastLocation.latitude,Constants.lastLocation.longitude,3)

        val cityName = adress?.get(0)?.adminArea
        _cityName.postValue(cityName)
        viewModelScope.launch {
            fetchCityInfo(cityName!!.lowercase())
        }



    }

    private suspend fun fetchCityInfo(cityName: String) {

        withContext(Dispatchers.IO) {
            val url = "https://www.kulturportali.gov.tr/turkiye/$cityName/genelbilgiler"

            try {
                val doc = Jsoup.connect(url).get()
                val info = doc.select("span#ContentPlaceHolder1_lblTarihce").text()
                _info.postValue(info)

                val gezilecekYerlerDiv: Element? = doc.select("#ContentPlaceHolder1_pnlGezilecekYerler").first()
                val seyehatHatirasiDiv: Element? = doc.select("#ContentPlaceHolder1_pnlSeyahatHatirasi").first()
                val gelenekselMutfakDiv: Element? = doc.select("#ContentPlaceHolder1_pnlGelenekselTurkMutfagi").first()
                val turizmAktiviteleriDiv: Element? = doc.select("#ContentPlaceHolder1_pnlTurizmAktiviteleri").first()
                val kulturAtlasiDiv: Element? = doc.select("#ContentPlaceHolder1_pnlKulturAtlasi").first()

                var gezilecekYerlerLink = "https://www.kulturportali.gov.tr"+gezilecekYerlerDiv?.select("h3 a.btn.btn-tema")?.attr("href")
                var seyehatHatirasiLink = "https://www.kulturportali.gov.tr"+seyehatHatirasiDiv?.select("h3 a.btn.btn-tema")?.attr("href")
                var gelenekselMutfakLink = "https://www.kulturportali.gov.tr"+gelenekselMutfakDiv?.select("h3 a.btn.btn-tema")?.attr("href")
                var turizmAktiviteleriLink = "https://www.kulturportali.gov.tr"+turizmAktiviteleriDiv?.select("h3 a.btn.btn-tema")?.attr("href")
                val kulturAtlasiLink = "https://www.kulturportali.gov.tr"+kulturAtlasiDiv?.select("h3 a.btn.btn-tema")?.attr("href")

                gezilecekYerlerLinkLiveData.postValue(gezilecekYerlerLink)
                seyehatHatirasiLinkLiveData.postValue(seyehatHatirasiLink)
                gelenekselMutfakLinkLiveData.postValue(gelenekselMutfakLink)
                turizmAktiviteleriLinkLiveData.postValue(turizmAktiviteleriLink)
                kulturAtlasiLinkLiveData.postValue(kulturAtlasiLink)



                val articles: Elements = gezilecekYerlerDiv?.select("article.portfolio-item") ?: return@withContext
                val articlesSeyehatHatirasi: Elements = seyehatHatirasiDiv?.select("article.portfolio-item") ?: return@withContext
                val articlesGelenekselMutfak: Elements = gelenekselMutfakDiv?.select("article.portfolio-item") ?: return@withContext
                val articlesTurizmAktiviteleri: Elements = turizmAktiviteleriDiv?.select("article.portfolio-item") ?: return@withContext
                val articlesKulturAtlasi: Elements = kulturAtlasiDiv?.select("article.portfolio-item") ?: return@withContext

                val listPlace = ArrayList<GuidePlaceModel>()

                for (article in articles) {
                    val title: String = article.select("div.portfolio-desc h3 a").text()
                    val link: String = "https://www.kulturportali.gov.tr" + article.select("div.portfolio-desc h3 a").attr("href")
                    val imageUrl: String = "https://www.kulturportali.gov.tr" + article.select("div.portfolio-image img").attr("src")

                    val model= GuidePlaceModel(
                        title = title,
                        link = link,
                        imageUrl = imageUrl
                    )

                    listPlace.add(model)
                }
                _placeList.postValue(listPlace)

                val listSeyehat = ArrayList<GuidePlaceModel>()
                for (article in articlesSeyehatHatirasi) {
                    val title: String = article.select("div.portfolio-desc h3 a").text()
                    val link: String = "https://www.kulturportali.gov.tr" + article.select("div.portfolio-desc h3 a").attr("href")
                    val imageUrl: String = "https://www.kulturportali.gov.tr" + article.select("div.portfolio-image img").attr("src")

                    val model= GuidePlaceModel(
                        title = title,
                        link = link,
                        imageUrl = imageUrl
                    )

                    listSeyehat.add(model)
                }
                _seyehatHatirasiList.postValue(listSeyehat)


                val listGelenekselMutfak = ArrayList<GuidePlaceModel>()
                for (article in articlesGelenekselMutfak) {
                    val title: String = article.select("div.portfolio-desc h3 a").text()
                    val link: String = "https://www.kulturportali.gov.tr" + article.select("div.portfolio-desc h3 a").attr("href")
                    val imageUrl: String = "https://www.kulturportali.gov.tr" + article.select("div.portfolio-image img").attr("src")

                    val model= GuidePlaceModel(
                        title = title,
                        link = link,
                        imageUrl = imageUrl
                    )


                    listGelenekselMutfak.add(model)
                }
                _gelenekselMutfakList.postValue(listGelenekselMutfak)

                val listTurizmAktiviteleri = ArrayList<GuidePlaceModel>()
                for (article in articlesTurizmAktiviteleri) {
                    val title: String = article.select("div.portfolio-desc h3 a").text()
                    val link: String = "https://www.kulturportali.gov.tr" + article.select("div.portfolio-desc h3 a").attr("href")
                    val imageUrl: String = "https://www.kulturportali.gov.tr" + article.select("div.portfolio-image img").attr("src")

                    val model= GuidePlaceModel(
                        title = title,
                        link = link,
                        imageUrl = imageUrl
                    )


                    listTurizmAktiviteleri.add(model)
                }
                _turizmAktiviteList.postValue(listTurizmAktiviteleri)

                val listKulturAtlasi = ArrayList<GuidePlaceModel>()
                for (article in articlesKulturAtlasi) {
                    val title: String = article.select("div.portfolio-desc h3 a").text()
                    val link: String = "https://www.kulturportali.gov.tr" + article.select("div.portfolio-desc h3 a").attr("href")
                    val imageUrl: String = "https://www.kulturportali.gov.tr" + article.select("div.portfolio-image img").attr("src")

                    val model= GuidePlaceModel(
                        title = title,
                        link = link,
                        imageUrl = imageUrl
                    )


                    listKulturAtlasi.add(model)
                }
                _kulturAtlasiList.postValue(listKulturAtlasi)

            } catch (e: Exception) {
                Log.e("TAG","exception : ${e.message}")
            }
        }
    }


}
data class GuidePlaceModel(
    val title: String,
    val link: String,
    val imageUrl:String
)
