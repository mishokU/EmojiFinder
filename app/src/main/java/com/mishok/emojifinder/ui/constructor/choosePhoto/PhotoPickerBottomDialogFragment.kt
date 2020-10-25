package com.mishok.emojifinder.ui.constructor.choosePhoto

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.viewModels.PhotosViewModel
import com.mishok.emojifinder.domain.viewModels.SharedViewModel
import com.mishok.emojifinder.ui.utils.REQUEST_CODE_READ_EXTERNAL
import kotlinx.android.synthetic.main.fragment_photo_picker.*
import javax.inject.Inject

class PhotoPickerBottomDialogFragment() : DaggerBottomSheetDialogFragment() {

    lateinit var root : View
    lateinit var adapter: PhotoPickerAdapter

    private val model: SharedViewModel by activityViewModels()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: PhotosViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_photo_picker, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        initRecyclerView()
        initRefresh()
        loadPhotos()
    }

    private fun initRefresh() {

    }

    private fun initRecyclerView() {
        adapter = PhotoPickerAdapter(PhotoPickerAdapter.OnPhotoPick {
            model.sentImage(it)
            this.dismiss()
        })
        photosRecyclerView.adapter = adapter
    }

    private fun loadPhotos() {
        setupPermissions()
        viewModel.images.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Success -> {
                        adapter.setData(it.data as MutableList<PhotoPickerModel>)
                        progressBarImages.visibility = View.GONE
                        errorLoadingImages.visibility = View.GONE
                    }
                    is Result.Error -> {
                        errorLoadingImages.visibility = View.VISIBLE
                        progressBarImages.visibility = View.GONE
                    }
                    is Result.Loading -> {
                        progressBarImages.playAnimation()
                        errorLoadingImages.visibility = View.GONE
                        progressBarImages.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_READ_EXTERNAL){
            viewModel = injectViewModel(viewModelFactory)
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(requireActivity(), READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL)
    }
}
