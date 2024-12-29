package com.locado.app.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.locado.app.helper.Helper

abstract class BaseFragment<VB : ViewBinding>: Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!
    var loadingDialog: Dialog? = null


    fun hideLoading() {
        loadingDialog?.let {
            if (it.isShowing) {
                it.cancel()
            }
        }
    }

    fun showLoading() {
        hideLoading()
        loadingDialog = Helper.showLoadingDialog(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoading()
        _binding = null
    }


    protected abstract fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB


    open fun initView() {}
    open fun initListeners() {}
    open fun initObservers() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListeners()
        initObservers()
    }

}