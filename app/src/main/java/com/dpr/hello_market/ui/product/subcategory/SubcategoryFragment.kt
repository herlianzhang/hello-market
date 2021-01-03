package com.dpr.hello_market.ui.product.subcategory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentSubcategoryBinding
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.injectViewModel
import com.dpr.hello_market.ui.product.subcategory.adapter.SubcategoryAdapter
import com.facebook.stetho.dumpapp.ArgsHelper
import timber.log.Timber
import javax.inject.Inject

class SubcategoryFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentSubcategoryBinding
    private lateinit var viewModel: SubcategoryViewModel

    private val args: SubcategoryFragmentArgs by navArgs()

    private val subcategoryAdapter by lazy {
        SubcategoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subcategory, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        binding.viewModel = viewModel
        viewModel.getSubCategory(args.id)

        initAdapter()
        initListener()
        initObserver()
    }

    private fun initAdapter() {
        binding.rvSubcategory.adapter = subcategoryAdapter
    }

    private fun initListener() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObserver() {
        viewModel.subcategories.observe(viewLifecycleOwner, { subcategories ->
            subcategoryAdapter.submitList(subcategories)
        })
    }
}