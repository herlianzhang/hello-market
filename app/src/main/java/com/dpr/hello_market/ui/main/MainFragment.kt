package com.dpr.hello_market.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private lateinit var pagerAdapter: MainPagerAdapter
    private lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback

    private val bnvIds = listOf(
        R.id.homeFragment,
        R.id.activityFragment,
        R.id.cartFragment,
        R.id.accountFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPagerAdapter()
        initListener()
    }

    private fun initPagerAdapter() {
        pagerAdapter = MainPagerAdapter(requireActivity())
        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bnvMain.selectedItemId = bnvIds[position]
            }
        }

        binding.vpMain.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(pageChangeCallback)
        }
    }

    private fun initListener() {
        binding.bnvMain.setOnNavigationItemSelectedListener { item ->
            val index = bnvIds.indexOf(item.itemId)
            binding.vpMain.currentItem = index
            true
        }
    }
}