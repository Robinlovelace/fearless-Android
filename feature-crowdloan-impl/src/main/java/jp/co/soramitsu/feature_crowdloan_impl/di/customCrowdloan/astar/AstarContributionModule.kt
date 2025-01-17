package jp.co.soramitsu.feature_crowdloan_impl.di.customCrowdloan.astar

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import jp.co.soramitsu.common.di.scope.FeatureScope
import jp.co.soramitsu.common.resources.ResourceManager
import jp.co.soramitsu.feature_account_api.domain.interfaces.AccountRepository
import jp.co.soramitsu.feature_crowdloan_impl.di.customCrowdloan.CustomContributeFactory
import jp.co.soramitsu.feature_crowdloan_impl.domain.contribute.custom.astar.AstarContributeInteractor
import jp.co.soramitsu.feature_crowdloan_impl.presentation.contribute.custom.astar.AstarContributeSubmitter

@Module
class AstarContributionModule {

    @Provides
    @FeatureScope
    fun provideAstarInteractor(
        accountRepository: AccountRepository,
    ) = AstarContributeInteractor(accountRepository)

    @Provides
    @FeatureScope
    fun provideAstarSubmitter(
        interactor: AstarContributeInteractor
    ) = AstarContributeSubmitter(interactor)

    @Provides
    @FeatureScope
    @IntoSet
    fun provideAstarFactory(
        submitter: AstarContributeSubmitter,
        acalaInteractor: AstarContributeInteractor,
        resourceManager: ResourceManager
    ): CustomContributeFactory = AstarContributeFactory(
        submitter,
        acalaInteractor,
        resourceManager
    )
}
