package com.locado.app.ui.fragment.guide.detail

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.locado.app.base.BaseFragment
import com.locado.app.databinding.FragmentGuideDetailBinding

class GuideDetailFragment : BaseFragment<FragmentGuideDetailBinding>() {

    private val viewModel: GuideDetailViewModel by viewModels()
    private val args: GuideDetailFragmentArgs by navArgs()

    val isLoading = MutableLiveData<Boolean>()


    override fun initObservers() {

        isLoading.observe(viewLifecycleOwner) {
            if (!it) {
                binding.frameLayout.visibility = View.VISIBLE
                hideLoading()
            }
        }

    }

    override fun initListeners() {

        binding.back.setOnClickListener {
            it.findNavController().navigateUp()
        }

    }

    override fun initView() {

        showLoading()

        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showLoading()
            }
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                isLoading.postValue(false)
            }
        }
        binding.webView.loadUrl(args.url)

    }


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGuideDetailBinding {
        return FragmentGuideDetailBinding.inflate(inflater, container, false)

    }


}