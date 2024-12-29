package com.locado.app.ui.fragment.guide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.locado.app.R
import com.locado.app.base.BaseFragment
import com.locado.app.databinding.FragmentGuideBinding
import com.locado.app.ui.fragment.guide.adapter.GuidePlaceAdapter

class GuideFragment : BaseFragment<FragmentGuideBinding>(){

    private val viewModel: GuideViewModel by viewModels()

    private var isTextExpanded = false

    private lateinit var guidePlaceAdapter: GuidePlaceAdapter
    private lateinit var seyehatAdapter: GuidePlaceAdapter
    private lateinit var gelenekselMutfakAdapter: GuidePlaceAdapter
    private lateinit var turizmAktiviteleriAdapter: GuidePlaceAdapter
    private lateinit var kulturAtlasiAdapter: GuidePlaceAdapter


    private val isLoading = MutableLiveData<Boolean>()

    override fun initView() {
        showLoading()
        viewModel.getCityName(requireContext())
    }

    override fun initListeners() {

        binding.tvAll.setOnClickListener {
            val action = GuideFragmentDirections.actionNavigationGuideToGuideDetailFragment(viewModel.gezilecekYerlerLinkLiveData.value ?: "")
            it.findNavController().navigate(action)
        }

        binding.tvAll2.setOnClickListener {
            val action = GuideFragmentDirections.actionNavigationGuideToGuideDetailFragment(viewModel.seyehatHatirasiLinkLiveData.value ?: "")
            it.findNavController().navigate(action)
        }

        binding.tvAll3.setOnClickListener {
            val action = GuideFragmentDirections.actionNavigationGuideToGuideDetailFragment(viewModel.gelenekselMutfakLinkLiveData.value ?: "")
            it.findNavController().navigate(action)
        }

        binding.tvAll4.setOnClickListener {
            val action = GuideFragmentDirections.actionNavigationGuideToGuideDetailFragment(viewModel.turizmAktiviteleriLinkLiveData.value ?: "")
            it.findNavController().navigate(action)
        }

        binding.tvAll5.setOnClickListener {
            val action = GuideFragmentDirections.actionNavigationGuideToGuideDetailFragment(viewModel.kulturAtlasiLinkLiveData.value ?: "")
            it.findNavController().navigate(action)
        }

        binding.tvInfo.setOnClickListener {
            if (isTextExpanded) {
                binding.tvInfo.maxLines = 3
                binding.tvInfo.ellipsize = android.text.TextUtils.TruncateAt.END
            } else {

                binding.tvInfo.maxLines = Integer.MAX_VALUE
                binding.tvInfo.ellipsize = null
            }

            isTextExpanded = !isTextExpanded

        }

    }

    override fun initObservers() {

        isLoading.observe(viewLifecycleOwner) {
            if (!it) {
                hideLoading()
            }
        }

        viewModel.cityNameLiveData.observe(viewLifecycleOwner){cityName ->
            binding.tvTitle.text = requireContext().getString(R.string.city_guide,cityName)

        }

        viewModel.infoLiveData.observe(viewLifecycleOwner){ info ->
            isLoading.postValue(false)
            binding.cL.visibility = View.VISIBLE
            binding.tvInfo.text = info
        }

        viewModel.placeListLiveData.observe(viewLifecycleOwner){ list ->
            guidePlaceAdapter = GuidePlaceAdapter(requireContext(), list)
            binding.rvPlaces.adapter = guidePlaceAdapter
            binding.rvPlaces.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }

        viewModel.seyehatHatirasiLiveData.observe(viewLifecycleOwner){ list ->
            seyehatAdapter = GuidePlaceAdapter(requireContext(), list)
            binding.rvPlaces2.adapter = seyehatAdapter
            binding.rvPlaces2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }

        viewModel.gelenekselMutfakLiveData.observe(viewLifecycleOwner){ list ->
            gelenekselMutfakAdapter = GuidePlaceAdapter(requireContext(), list)
            binding.rvPlaces3.adapter = gelenekselMutfakAdapter
            binding.rvPlaces3.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }

        viewModel.turizmAktiviteLiveData.observe(viewLifecycleOwner){ list ->
            turizmAktiviteleriAdapter = GuidePlaceAdapter(requireContext(), list)
            binding.rvPlaces4.adapter = turizmAktiviteleriAdapter
            binding.rvPlaces4.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }

        viewModel.kulturAtlasiLiveData.observe(viewLifecycleOwner){ list ->
            kulturAtlasiAdapter = GuidePlaceAdapter(requireContext(), list)
            binding.rvPlaces5.adapter = kulturAtlasiAdapter
            binding.rvPlaces5.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }


    }


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGuideBinding {
        return FragmentGuideBinding.inflate(inflater, container, false)

    }


}