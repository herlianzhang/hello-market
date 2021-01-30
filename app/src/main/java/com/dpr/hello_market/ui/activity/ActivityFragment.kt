package com.dpr.hello_market.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentActivityBinding
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.injectViewModel
import com.dpr.hello_market.ui.activity.adapter.ActivityAdapter
import javax.inject.Inject

class ActivityFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentActivityBinding
    private lateinit var viewModel: ActivityViewModel

    private val activityAdapter by lazy {
        ActivityAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)

        initAdapter()
        initObserver()
    }

    private fun initAdapter() {
        binding.rvMain.adapter = activityAdapter
    }

    private fun initObserver() {
        viewModel.activity.observe(viewLifecycleOwner, {
            activityAdapter.submitList(it)

            binding.noData.isVisible = it.isEmpty()
        })
    }
}