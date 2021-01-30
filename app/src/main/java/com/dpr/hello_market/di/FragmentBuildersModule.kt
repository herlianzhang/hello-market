package com.dpr.hello_market.di

import com.dpr.hello_market.ui.account.AccountFragment
import com.dpr.hello_market.ui.activity.ActivityFragment
import com.dpr.hello_market.ui.cart.CartFragment
import com.dpr.hello_market.ui.change_password.ChangePasswordFragment
import com.dpr.hello_market.ui.choose_location.ChooseLocationFragment
import com.dpr.hello_market.ui.decision.DecisionFragment
import com.dpr.hello_market.ui.edit_profile.EditProfileFragment
import com.dpr.hello_market.ui.home.HomeFragment
import com.dpr.hello_market.ui.login.LoginFragment
import com.dpr.hello_market.ui.product.list.ProductListFragment
import com.dpr.hello_market.ui.product.subcategory.SubcategoryFragment
import com.dpr.hello_market.ui.register.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeDecisionFragment(): DecisionFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector
    abstract fun contributeEditProfileFragment(): EditProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeChooseLocationFragment(): ChooseLocationFragment

    @ContributesAndroidInjector
    abstract fun contributeChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeSubcategoryFragment(): SubcategoryFragment

    @ContributesAndroidInjector
    abstract fun contributeCartFragment(): CartFragment

    @ContributesAndroidInjector
    abstract fun contributeActivityFragment(): ActivityFragment

    @ContributesAndroidInjector
    abstract fun contributeProductListFragment(): ProductListFragment
}