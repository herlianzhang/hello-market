package com.dpr.hello_market.ui.account

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentAccountBinding
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.ViewModelFactory
import com.dpr.hello_market.di.injectViewModel
import com.dpr.hello_market.ui.main.MainFragmentDirections
import javax.inject.Inject

class AccountFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: AccountViewModel

    private val mainNavController: NavController? by lazy { activity?.findNavController(R.id.fcv_nav) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        binding.viewModel = viewModel

        initListener()
    }

    private fun initListener() {
        binding.llEdit.setOnClickListener {
            val customer = viewModel.customer
            if (customer != null) {
                val action =
                    MainFragmentDirections.actionMainFragmentToEditProfileFragment(customer)
                mainNavController?.navigate(action)
            } else {
                showToast()
            }
        }

        binding.llChangePassword.setOnClickListener {
            val customer = viewModel.customer
            if (customer != null) {
                val action =
                    MainFragmentDirections.actionMainFragmentToChangePasswordFragment(
                        customer.password ?: ""
                    )
                mainNavController?.navigate(action)
            } else {
                showToast()
            }
        }

        binding.llLogout.setOnClickListener {
            showLogoutAlert()
        }
    }

    private fun showLogoutAlert() {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure want to logout?")
            .setPositiveButton("yes") { _, _ ->
                viewModel.signOut()
                mainNavController?.navigate(R.id.action_main_fragment_to_loginFragment)
            }
            .setNegativeButton("No") { _, _ ->

            }
            .show()
    }

    private fun showToast() {
        Toast.makeText(requireContext(), "Please wait until data ready", Toast.LENGTH_SHORT)
            .show()
    }
}