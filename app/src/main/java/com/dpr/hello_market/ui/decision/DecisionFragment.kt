package com.dpr.hello_market.ui.decision

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dpr.hello_market.R
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.di.injectViewModel
import javax.inject.Inject

class DecisionFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DecisionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_decision, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)

        findNavController().navigate(R.id.action_decisionFragment_to_loginFragment)
    }

}