package com.dpr.hello_market.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dpr.hello_market.ui.account.AccountViewModel
import com.dpr.hello_market.ui.change_password.ChangePasswordViewModel
import com.dpr.hello_market.ui.choose_location.ChooseLocationViewModel
import com.dpr.hello_market.ui.decision.DecisionViewModel
import com.dpr.hello_market.ui.edit_profile.EditProfileViewModel
import com.dpr.hello_market.ui.home.HomeViewModel
import com.dpr.hello_market.ui.login.LoginViewModel
import com.dpr.hello_market.ui.product.subcategory.SubcategoryViewModel
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
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun bindEditProfileViewModel(editProfileViewModel: EditProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseLocationViewModel::class)
    abstract fun bindChooseLocationViewModel(chooseLocationViewModel: ChooseLocationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    abstract fun bindChangePasswordViewModel(changePasswordViewModel: ChangePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubcategoryViewModel::class)
    abstract fun bindSubcategoryViewModel(subcategoryViewModel: SubcategoryViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}