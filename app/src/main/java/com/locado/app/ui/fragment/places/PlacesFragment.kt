package com.locado.app.ui.fragment.places

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.locado.app.R
import com.locado.app.databinding.FragmentPlacesBinding
import com.locado.app.helper.Constants
import com.locado.app.helper.Constants.placeTypes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlacesFragment : Fragment() {

    private val viewModel: PlacesViewModel by viewModels()


    private var _binding: FragmentPlacesBinding? = null
    private val binding get() = _binding!!

    private var dialog: Dialog? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListeners()
        initObservers()

    }

    private fun initObservers() {


    }

    private fun initListeners() {

        binding.btnSearch.setOnClickListener {
            viewModel.getNearBy(requireContext())
        }

        binding.tVType.setOnClickListener {
            dialog = Dialog(requireContext())
            dialog!!.setContentView(R.layout.dialog_searchable_spinner)
            dialog!!.window?.setLayout(650, 1000)
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.show()

            val editText: EditText = dialog!!.findViewById(R.id.edit_text)
            val listView: ListView = dialog!!.findViewById(R.id.list_view)
            val placeTypeStrings = placeTypes.map { getString(it) }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, placeTypeStrings)


            listView.adapter = adapter
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
            listView.onItemClickListener =
                OnItemClickListener { parent, view, position, id -> // when item selected from list


                    adapter.getItem(position)?.let { it1 ->
                        binding.tVType.text = it1

                        val typeIndex = placeTypeStrings.indexOf(it1)
                        viewModel.updatePlaceName(typeIndex)


                    }


                    dialog!!.dismiss()
                }

        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress==0){
                    binding.seekBar.progress = 1
                    binding.kmText.text = "${binding.seekBar.progress} km"
                    viewModel.updateDistance(binding.seekBar.progress)
                }else{
                    binding.kmText.text = "${progress} km"
                    viewModel.updateDistance(progress)

                }


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })



    }

    private fun initView() {


        when {
            (ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) -> {

                viewModel.getNearBy(requireContext())
            }
            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)
            -> {
                Log.e(TAG, "Showing permission rationale dialog")
            }
            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION
                    ),
                    PERMISSION_REQUEST_CODE
                )
            }
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlacesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PlacesFragment"
        private const val PERMISSION_REQUEST_CODE = 9
    }
}