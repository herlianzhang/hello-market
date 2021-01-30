package com.dpr.hello_market.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dpr.hello_market.ui.account.AccountFragment
import com.dpr.hello_market.ui.activity.ActivityFragment
import com.dpr.hello_market.ui.cart.CartFragment
import com.dpr.hello_market.ui.home.HomeFragment

class MainPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val myFragments = listOf<Fragment>(
        HomeFragment(),
        ActivityFragment(),
        CartFragment(),
        AccountFragment()
    )

    override fun getItemCount(): Int = myFragments.size

    override fun createFragment(position: Int): Fragment {
        return myFragments[position]
    }
}