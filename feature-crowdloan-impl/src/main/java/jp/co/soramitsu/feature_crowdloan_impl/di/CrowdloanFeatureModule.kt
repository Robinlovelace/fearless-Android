package jp.co.soramitsu.feature_crowdloan_impl.di

import dagger.Module
import dagger.Provides
import jp.co.soramitsu.common.data.network.NetworkApiCreator
import jp.co.soramitsu.common.data.storage.Preferences
import jp.co.soramitsu.common.di.scope.FeatureScope
import jp.co.soramitsu.common.resources.ResourceManager
import jp.co.soramitsu.feature_account_api.data.extrinsic.ExtrinsicService
import jp.co.soramitsu.feature_account_api.domain.interfaces.AccountRepository
import jp.co.soramitsu.feature_crowdloan_api.data.repository.CrowdloanRepository
import jp.co.soramitsu.feature_crowdloan_impl.data.CrowdloanSharedState
import jp.co.soramitsu.feature_crowdloan_impl.data.network.api.acala.AcalaApi
import jp.co.soramitsu.feature_crowdloan_impl.data.network.api.moonbeam.MoonbeamApi
import jp.co.soramitsu.feature_crowdloan_impl.data.network.api.parachain.ParachainMetadataApi
import jp.co.soramitsu.feature_crowdloan_impl.data.repository.CrowdloanRepositoryImpl
import jp.co.soramitsu.feature_crowdloan_impl.di.customCrowdloan.CustomContributeModule
import jp.co.soramitsu.feature_crowdloan_impl.domain.contribute.CrowdloanContributeInteractor
import jp.co.soramitsu.feature_crowdloan_impl.domain.main.CrowdloanInteractor
import jp.co.soramitsu.feature_crowdloan_impl.storage.CrowdloanStorage
import jp.co.soramitsu.feature_wallet_api.domain.AssetUseCase
import jp.co.soramitsu.feature_wallet_api.domain.TokenUseCase
import jp.co.soramitsu.feature_wallet_api.domain.implementations.AssetUseCaseImpl
import jp.co.soramitsu.feature_wallet_api.domain.implementations.TokenUseCaseImpl
import jp.co.soramitsu.feature_wallet_api.domain.interfaces.TokenRepository
import jp.co.soramitsu.feature_wallet_api.domain.interfaces.WalletRepository
import jp.co.soramitsu.feature_wallet_api.presentation.mixin.TransferValidityChecks
import jp.co.soramitsu.feature_wallet_api.presentation.mixin.TransferValidityChecksProvider
import jp.co.soramitsu.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorFactory
import jp.co.soramitsu.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorMixin
import jp.co.soramitsu.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import jp.co.soramitsu.feature_wallet_api.presentation.mixin.fee.FeeLoaderProvider
import jp.co.soramitsu.runtime.di.REMOTE_STORAGE_SOURCE
import jp.co.soramitsu.runtime.multiNetwork.ChainRegistry
import jp.co.soramitsu.runtime.repository.ChainStateRepository
import jp.co.soramitsu.runtime.storage.source.StorageDataSource
import javax.inject.Named

@Module(
    includes = [
        CustomContributeModule::class
    ]
)
class CrowdloanFeatureModule {

    @Provides
    @FeatureScope
    fun provideCrowdloanStorage(preferences: Preferences) = CrowdloanStorage(preferences)

    @Provides
    @FeatureScope
    fun provideAssetUseCase(
        walletRepository: WalletRepository,
        accountRepository: AccountRepository,
        sharedState: CrowdloanSharedState,
    ): AssetUseCase = AssetUseCaseImpl(
        walletRepository,
        accountRepository,
        sharedState
    )

    @Provides
    fun provideAssetSelectorMixinFactory(
        assetUseCase: AssetUseCase,
        singleAssetSharedState: CrowdloanSharedState,
        resourceManager: ResourceManager
    ): AssetSelectorMixin.Presentation.Factory = AssetSelectorFactory(
        assetUseCase,
        singleAssetSharedState,
        resourceManager
    )

    @Provides
    @FeatureScope
    fun provideTokenUseCase(
        tokenRepository: TokenRepository,
        sharedState: CrowdloanSharedState,
    ): TokenUseCase = TokenUseCaseImpl(
        tokenRepository,
        sharedState
    )

    @Provides
    @FeatureScope
    fun provideFeeLoaderMixin(
        resourceManager: ResourceManager,
        tokenUseCase: TokenUseCase,
    ): FeeLoaderMixin.Presentation = FeeLoaderProvider(
        resourceManager,
        tokenUseCase
    )

    @Provides
    @FeatureScope
    fun provideCrowdloanSharedState(
        chainRegistry: ChainRegistry,
        preferences: Preferences,
    ) = CrowdloanSharedState(chainRegistry, preferences)

    @Provides
    @FeatureScope
    fun crowdloanRepository(
        @Named(REMOTE_STORAGE_SOURCE) remoteStorageSource: StorageDataSource,
        crowdloanMetadataApi: ParachainMetadataApi,
        chainRegistry: ChainRegistry,
        moonbeamApi: MoonbeamApi,
        crowdloanStorage: CrowdloanStorage
    ): CrowdloanRepository = CrowdloanRepositoryImpl(
        remoteStorageSource,
        chainRegistry,
        crowdloanMetadataApi,
        moonbeamApi,
        crowdloanStorage
    )

    @Provides
    @FeatureScope
    fun provideCrowdloanInteractor(
        accountRepository: AccountRepository,
        crowdloanRepository: CrowdloanRepository,
        chainStateRepository: ChainStateRepository
    ) = CrowdloanInteractor(
        accountRepository,
        crowdloanRepository,
        chainStateRepository
    )

    @Provides
    @FeatureScope
    fun provideCrowdloanMetadataApi(networkApiCreator: NetworkApiCreator): ParachainMetadataApi {
        return networkApiCreator.create(ParachainMetadataApi::class.java)
    }

    @Provides
    @FeatureScope
    fun provideTransferChecks(): TransferValidityChecks.Presentation = TransferValidityChecksProvider()

    @Provides
    @FeatureScope
    fun provideCrowdloanContributeInteractor(
        extrinsicService: ExtrinsicService,
        accountRepository: AccountRepository,
        chainRegistry: ChainRegistry,
        chainStateRepository: ChainStateRepository,
        sharedState: CrowdloanSharedState,
        crowdloanRepository: CrowdloanRepository,
        walletRepository: WalletRepository,
        moonbeamApi: MoonbeamApi,
        acalaApi: AcalaApi,
        resourceManager: ResourceManager,
    ) = CrowdloanContributeInteractor(
        extrinsicService,
        accountRepository,
        chainRegistry,
        chainStateRepository,
        sharedState,
        crowdloanRepository,
        walletRepository,
        moonbeamApi,
        acalaApi,
        resourceManager
    )
}
