package com.dpr.hello_market.di

import android.app.Application
import androidx.room.Room
import com.dpr.hello_market.api.ApiService
import com.dpr.hello_market.api.AuthInterceptor
import com.dpr.hello_market.db.activity.ActivityDao
import com.dpr.hello_market.db.activity.ActivityDatabase
import com.dpr.hello_market.db.cart.CartDao
import com.dpr.hello_market.db.cart.CartDatabase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [CoreDataModule::class, ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideApiService(
        @ServiceApi okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = provideService(okHttpClient, converterFactory, ApiService::class.java)

    @ServiceApi
    @Provides
    fun providePrivateOkHttpClient(upstreamClient: OkHttpClient): OkHttpClient {
        return upstreamClient.newBuilder().addInterceptor(AuthInterceptor()).build()
    }

    private fun createRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
//            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideCartDb(app: Application): CartDatabase {
        return Room.databaseBuilder(app, CartDatabase::class.java, "cart.db")
            .fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideCartDao(db: CartDatabase): CartDao {
        return db.cartDao()
    }

    @Singleton
    @Provides
    fun provideActivityDb(app: Application): ActivityDatabase {
        return Room.databaseBuilder(app, ActivityDatabase::class.java, "activity.db")
            .fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideActivityDao(db: ActivityDatabase): ActivityDao {
        return db.activityDao()
    }

    private fun <T> provideService(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
        clazz: Class<T>
    ): T {
        return createRetrofit(okHttpClient, converterFactory).create(clazz)
    }
}
