package com.dpr.hello_market.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.*
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentHomeBinding
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.injectViewModel
import com.dpr.hello_market.ui.home.adapter.HomeBannerAdapter
import com.dpr.hello_market.ui.home.adapter.HomeCategoryAdapter
import com.dpr.hello_market.ui.main.MainFragmentDirections
import com.dpr.hello_market.vo.Category
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : Fragment(), Injectable, HomeCategoryAdapter.OnClickListener {

    private val delay = 5000L

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    private val mainNavController: NavController? by lazy { activity?.findNavController(R.id.fcv_nav) }

    lateinit var mainHandler: Handler

    private val updateTextTask = object : Runnable {
        override fun run() {
            moveBanner()
            mainHandler.postDelayed(this, delay)
        }
    }

    private val bannerAdapter by lazy {
        HomeBannerAdapter()
    }

    private val categoryAdapter by lazy {
        HomeCategoryAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainHandler = Handler(Looper.getMainLooper())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        binding.viewModel = viewModel

        initAdapter()
        initObserver()
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTextTask)
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.banner.value != null)
            mainHandler.postDelayed(updateTextTask, delay)
    }


    private fun initAdapter() {
        val pagerHelper = PagerSnapHelper()
        pagerHelper.attachToRecyclerView(binding.rvBanner)
        binding.indicator.attachToRecyclerView(binding.rvBanner, pagerHelper)

        binding.rvBanner.adapter = bannerAdapter
        binding.rvCategory.adapter = categoryAdapter
    }

    private fun initObserver() {
        viewModel.categories.observe(viewLifecycleOwner, { categories ->
            Timber.d("Masuk pak eko $categories")
            categoryAdapter.submitList(categories)
        })

        viewModel.banner.observe(viewLifecycleOwner, { banners ->
            bannerAdapter.submitList(banners.toMutableList())
            binding.indicator.createIndicators(banners.size, 0)
            if (!mainHandler.hasCallbacks(updateTextTask))
                mainHandler.postDelayed(updateTextTask, delay)
        })
    }

    private fun moveBanner() {
        if (bannerAdapter.itemCount != 0) {
            var position =
                (binding.rvBanner.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            position = (position + 1) % bannerAdapter.itemCount
            binding.rvBanner.smoothScrollToPosition(position)
        }
    }

    override fun onItemClicked(category: Category) {
        val id =
            if (category.name == "All Product") "" else category.name.toString()
        if (id == "") {
            Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
            return
        }
        val action =
            MainFragmentDirections.actionMainFragmentToSubcategoryFragment(id)
        mainNavController?.navigate(action)
    }
}