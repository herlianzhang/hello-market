package com.dpr.hello_market.ui.edit_profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentEditProfileBinding
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.injectViewModel
import com.dpr.hello_market.vo.Customer
import com.dpr.hello_market.vo.Location
import timber.log.Timber
import javax.inject.Inject

class EditProfileFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val args: EditProfileFragmentArgs by navArgs()

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imageUri = data?.data
            viewModel.imageUri = imageUri
            Glide.with(requireContext()).load(imageUri).into(binding.civAvatar)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        binding.viewModel = viewModel

        setFragmentResultListener(LOCATION) { requestKey, bundle ->
            if (requestKey == LOCATION) {
                val result = bundle.getParcelable<Location>(LOCATION_DATA)
                result?.let {
                    Timber.d("Dapat Lokasinya di $it")
                    binding.etAddress.setText(it.address)
                }
            }
        }

        initCustomer(args.customer)
        initListener()
    }

    private fun initCustomer(customer: Customer) {
        viewModel.getAvatar(customer.avatar ?: "")
        binding.apply {
            etEmail.setText(customer.email)
            etName.setText(customer.name)
            etPhoneNumber.setText(customer.phoneNumber)
            etAddress.setText(customer.address)
        }
    }

    private fun initListener() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.civAvatar.setOnClickListener {
            requestPermission()
        }

        binding.btnAddress.setOnClickListener {
            val action = EditProfileFragmentDirections.actionEditProfileFragmentToChooseLocationFragment(Location(null, null, null))
            findNavController().navigate(action)
        }

        binding.btnEditProfile.setOnClickListener {

        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //permission already granted
                pickImageFromGallery()
            }
        } else {
            //system OS is < Marshmallow
            pickImageFromGallery();
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/jpg"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000;

        //Permission code
        private const val PERMISSION_CODE = 1001;

        const val LOCATION = "location"

        const val LOCATION_DATA = "location data"
    }
}