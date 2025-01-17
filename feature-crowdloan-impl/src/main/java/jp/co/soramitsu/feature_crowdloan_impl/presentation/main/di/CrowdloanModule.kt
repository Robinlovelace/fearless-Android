package jp.co.soramitsu.feature_crowdloan_impl.presentation.main.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import jp.co.soramitsu.common.address.AddressIconGenerator
import jp.co.soramitsu.common.di.viewmodel.ViewModelKey
import jp.co.soramitsu.common.di.viewmodel.ViewModelModule
import jp.co.soramitsu.common.resources.ClipboardManager
import jp.co.soramitsu.common.resources.ResourceManager
import jp.co.soramitsu.core.updater.UpdateSystem
import jp.co.soramitsu.feature_account_api.domain.interfaces.SelectedAccountUseCase
import jp.co.soramitsu.feature_crowdloan_impl.data.CrowdloanSharedState
import jp.co.soramitsu.feature_crowdloan_impl.domain.main.CrowdloanInteractor
import jp.co.soramitsu.feature_crowdloan_impl.presentation.CrowdloanRouter
import jp.co.soramitsu.feature_crowdloan_impl.presentation.main.CrowdloanViewModel
import jp.co.soramitsu.feature_wallet_api.domain.AssetUseCase
import jp.co.soramitsu.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorMixin

@Module(includes = [ViewModelModule::class])
class CrowdloanModule {

    @Provides
    @IntoMap
    @ViewModelKey(CrowdloanViewModel::class)
    fun provideViewModel(
        interactor: CrowdloanInteractor,
        assetUseCase: AssetUseCase,
        resourceManager: ResourceManager,
        iconGenerator: AddressIconGenerator,
        crowdloanSharedState: CrowdloanSharedState,
        router: CrowdloanRouter,
        crowdloanUpdateSystem: UpdateSystem,
        sharedState: CrowdloanSharedState,
        assetSelectorFactory: AssetSelectorMixin.Presentation.Factory,
        accountUseCase: SelectedAccountUseCase,
        clipboardManager: ClipboardManager
    ): ViewModel {
        return CrowdloanViewModel(
            interactor,
            assetUseCase,
            iconGenerator,
            resourceManager,
            crowdloanSharedState,
            router,
            sharedState,
            crowdloanUpdateSystem,
            assetSelectorFactory,
            accountUseCase,
            clipboardManager
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): CrowdloanViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(CrowdloanViewModel::class.java)
    }
}
