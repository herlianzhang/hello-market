package com.dpr.hello_market.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentRegisterBinding
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.injectViewModel
import javax.inject.Inject

class RegisterFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)

        initListener()
    }

    private fun initListener() {
        binding.tvLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}