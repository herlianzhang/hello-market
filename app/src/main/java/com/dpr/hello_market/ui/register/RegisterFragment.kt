package com.dpr.hello_market.ui.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentRegisterBinding
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.injectViewModel
import timber.log.Timber
import javax.inject.Inject

class RegisterFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    private val startForResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            viewModel.setImageUri(imageUri)
            Glide.with(requireContext()).load(imageUri).into(binding.civAvatar)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d("requestCode $requestCode\npermissions $permissions\ngrantResults $grantResults")
        var isAllGranted = true
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false
                break
            }
        }
        if (isAllGranted) {
            pickImageFromGallery()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        binding.viewModel = viewModel

        initListener()
        initObserver()
    }

    private fun initListener() {
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.buttonRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val name = binding.etName.text.toString()
            val password = binding.etPassword.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            viewModel.createUserWithEmailAndPassword(email, name, password, phoneNumber)
        }

        binding.civAvatar.setOnClickListener {
            requestPermission()
        }
    }

    private fun initObserver() {
        viewModel.registerSuccess.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_registerFragment_to_main_fragment)
        })

        viewModel.registerFail.observe(viewLifecycleOwner, { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Timber.e("Register failed cause: $e")
        })
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
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
        startForResult.launch(intent)
    }

    companion object {
        //Permission code
        private const val PERMISSION_CODE = 1001;
    }
}