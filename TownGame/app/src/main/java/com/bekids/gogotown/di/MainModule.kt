package com.bekids.gogotown.di

import com.bekids.gogotown.viewmodel.LoginViewModelImpl
import com.bekids.gogotown.model.repository.LoginRepository
import com.bekids.gogotown.model.repository.LoginRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 ***************************************
 * 项目名称:gogotown
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/8/24    4:06 下午
 * 用途
 ***************************************

 */

val viewModelModule = module {
    viewModel { LoginViewModelImpl(get()) }

}

val repositoryModule = module {
    single<LoginRepository> { LoginRepositoryImpl() }

}

val mainModule = listOf(viewModelModule, repositoryModule)