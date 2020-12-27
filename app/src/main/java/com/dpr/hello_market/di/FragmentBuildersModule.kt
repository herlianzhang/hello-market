package com.dpr.hello_market.di

import com.dpr.hello_market.ui.account.AccountFragment
import com.dpr.hello_market.ui.decision.DecisionFragment
import com.dpr.hello_market.ui.login.LoginFragment
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
}