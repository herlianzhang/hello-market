package com.dpr.hello_market.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentCartBinding
import com.dpr.hello_market.db.cart.CartDbModel
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.injectViewModel
import com.dpr.hello_market.helper.Helper
import com.dpr.hello_market.ui.cart.adapter.CartAdapter
import javax.inject.Inject

class CartFragment : Fragment(), Injectable, CartAdapter.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel

    private val cartAdapter by lazy {
        CartAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)

        initAdapter()
        initObserver()
    }

    private fun initAdapter() {
        binding.rvMain.adapter = cartAdapter
    }

    private fun initObserver() {
        viewModel.cart.observe(viewLifecycleOwner, {
            cartAdapter.submitList(it)
            var totalPrice = 0
            for (cart in it) {
                totalPrice += cart.total.times(cart.price).toInt()
            }
            binding.tvTotalPrice.text =
                "Rp ${Helper.convertToPriceFormatWithoutCurrency(totalPrice.toString())}"
            binding.noData.isVisible = it.isEmpty()
            binding.btnPlaceOrder.isVisible = it.isNotEmpty()
        })
    }

    override fun onReplaceCart(cart: CartDbModel) {
        viewModel.replaceCart(cart)
    }

    override fun onRemoveCart(cart: CartDbModel) {
        viewModel.removeCart(cart)
    }

}