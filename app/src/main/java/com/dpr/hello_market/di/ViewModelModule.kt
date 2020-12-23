package com.dpr.hello_market.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dpr.hello_market.ui.decision.DecisionViewModel
import com.dpr.hello_market.ui.login.LoginViewModel
import com.dpr.hello_market.ui.register.RegisterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DecisionViewModel::class)
    abstract fun bindDecisionViewModel(decisionViewModel:DecisionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}