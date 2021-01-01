package com.dpr.hello_market.ui.change_password

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentChangePasswordBinding
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.injectViewModel
import javax.inject.Inject

class ChangePasswordFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val args: ChangePasswordFragmentArgs by navArgs()

    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
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
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnChangePassword.setOnClickListener {
            val currentPassword = args.currentPassword
            val oldPassword = binding.tietOldPassword.text.toString()
            val newPassword = binding.tietNewPassword.text.toString()
            val confirmPassword = binding.tietConfirmPassword.text.toString()

            viewModel.changePassword(currentPassword, oldPassword, newPassword, confirmPassword)
        }
    }

    private fun initObserver() {
        viewModel.changePasswordSuccess.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "Change Password Success", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        })

        viewModel.changePasswordFail.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }
}