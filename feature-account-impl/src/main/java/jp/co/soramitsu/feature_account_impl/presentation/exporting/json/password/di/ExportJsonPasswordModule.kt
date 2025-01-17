package jp.co.soramitsu.feature_account_impl.presentation.exporting.json.password.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import jp.co.soramitsu.common.di.viewmodel.ViewModelKey
import jp.co.soramitsu.common.di.viewmodel.ViewModelModule
import jp.co.soramitsu.feature_account_api.domain.interfaces.AccountInteractor
import jp.co.soramitsu.feature_account_impl.presentation.AccountRouter
import jp.co.soramitsu.feature_account_impl.presentation.exporting.json.password.ExportJsonPasswordPayload
import jp.co.soramitsu.feature_account_impl.presentation.exporting.json.password.ExportJsonPasswordViewModel
import jp.co.soramitsu.runtime.multiNetwork.ChainRegistry

@Module(includes = [ViewModelModule::class])
class ExportJsonPasswordModule {

    @Provides
    @IntoMap
    @ViewModelKey(ExportJsonPasswordViewModel::class)
    fun provideViewModel(
        router: AccountRouter,
        accountInteractor: AccountInteractor,
        chainRegistry: ChainRegistry,
        payload: ExportJsonPasswordPayload
    ): ViewModel {
        return ExportJsonPasswordViewModel(router, accountInteractor, chainRegistry, payload)
    }

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): ExportJsonPasswordViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ExportJsonPasswordViewModel::class.java)
    }
}
