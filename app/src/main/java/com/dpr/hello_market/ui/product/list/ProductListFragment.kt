package com.dpr.hello_market.ui.product.list

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentProductListBinding
import com.dpr.hello_market.databinding.LayoutAddItemBinding
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.injectViewModel
import com.dpr.hello_market.helper.Helper
import com.dpr.hello_market.ui.product.list.adapter.ProductAdapter
import com.dpr.hello_market.vo.Product
import javax.inject.Inject
import kotlin.math.max

class ProductListFragment : Fragment(), Injectable, ProductAdapter.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentProductListBinding
    private lateinit var viewModel: ProductListViewModel

    private val args: ProductListFragmentArgs by navArgs()

    private val productAdapter by lazy {
        ProductAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        viewModel.getProduct(args.category, args.subcategory)

        initAdapter()
        initListener()
        initObserver()
    }

    private fun initAdapter() {
        binding.rvProduct.adapter = productAdapter
    }

    private fun initListener() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObserver() {
        viewModel.product.observe(viewLifecycleOwner, { product ->
            productAdapter.submitList(product)
        })
    }

    private fun createDialog(product: Product) {
        val dialogBinding = LayoutAddItemBinding.inflate(LayoutInflater.from(requireContext()))

        val builder = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(dialogBinding.root)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        dialogBinding.tvName.text = product.name

        dialogBinding.ivPlus.setOnClickListener {
            dialogBinding.tvCount.text =
                (dialogBinding.tvCount.text.toString().toInt().plus(1)).toString()
            dialogBinding.tvTotalPrice.text = "Rp ${
                Helper.convertToPriceFormatWithoutCurrency(
                    (product.price?.times(
                        dialogBinding.tvCount.text.toString().toInt()
                    )).toString()
                )
            }"
            dialogBinding.btnAdd.isEnabled = true
        }

        dialogBinding.ivMinus.setOnClickListener {
            dialogBinding.tvCount.text =
                (max(dialogBinding.tvCount.text.toString().toInt().minus(1), 0)).toString()
            dialogBinding.tvTotalPrice.text = "Rp ${
                Helper.convertToPriceFormatWithoutCurrency(
                    (product.price?.times(
                        dialogBinding.tvCount.text.toString().toInt()
                    )).toString()
                )
            }"
            if (dialogBinding.tvCount.text.toString().toInt() <= 0) {
                dialogBinding.btnAdd.isEnabled = false
            }
        }

        dialogBinding.btnAdd.setOnClickListener {
            viewModel.addItemToCart(product, dialogBinding.tvCount.text.toString().toInt())
            builder.dismiss()
            Toast.makeText(requireContext(), "Success add Item to cart", Toast.LENGTH_SHORT).show()
        }


        builder.show()
    }

    override fun onItemClicked(product: Product) {
        createDialog(product)
    }
}