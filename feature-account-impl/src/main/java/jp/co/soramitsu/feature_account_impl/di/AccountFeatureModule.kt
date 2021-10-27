package jp.co.soramitsu.feature_account_impl.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import jp.co.soramitsu.common.data.network.AppLinksProvider
import jp.co.soramitsu.common.data.network.rpc.SocketSingleRequestExecutor
import jp.co.soramitsu.common.data.storage.Preferences
import jp.co.soramitsu.common.data.storage.encrypt.EncryptedPreferences
import jp.co.soramitsu.common.di.scope.FeatureScope
import jp.co.soramitsu.common.resources.ClipboardManager
import jp.co.soramitsu.common.resources.LanguagesHolder
import jp.co.soramitsu.common.resources.ResourceManager
import jp.co.soramitsu.core_db.dao.AccountDao
import jp.co.soramitsu.core_db.dao.NodeDao
import jp.co.soramitsu.fearless_utils.encrypt.json.JsonSeedDecoder
import jp.co.soramitsu.fearless_utils.encrypt.json.JsonSeedEncoder
import jp.co.soramitsu.feature_account_api.domain.interfaces.AccountInteractor
import jp.co.soramitsu.feature_account_api.domain.interfaces.AccountRepository
import jp.co.soramitsu.feature_account_api.domain.interfaces.SelectedAccountUseCase
import jp.co.soramitsu.feature_account_api.domain.updaters.AccountUpdateScope
import jp.co.soramitsu.feature_account_api.presenatation.account.AddressDisplayUseCase
import jp.co.soramitsu.feature_account_api.presenatation.actions.ExternalAccountActions
import jp.co.soramitsu.feature_account_api.presenatation.actions.ExternalAccountActionsProvider
import jp.co.soramitsu.feature_account_impl.data.network.blockchain.AccountSubstrateSource
import jp.co.soramitsu.feature_account_impl.data.network.blockchain.AccountSubstrateSourceImpl
import jp.co.soramitsu.feature_account_impl.data.repository.AccountRepositoryImpl
import jp.co.soramitsu.feature_account_impl.data.repository.datasource.AccountDataSource
import jp.co.soramitsu.feature_account_impl.data.repository.datasource.AccountDataSourceImpl
import jp.co.soramitsu.feature_account_impl.data.repository.datasource.migration.AccountDataMigration
import jp.co.soramitsu.feature_account_impl.domain.AccountInteractorImpl
import jp.co.soramitsu.feature_account_impl.domain.NodeHostValidator
import jp.co.soramitsu.feature_account_impl.presentation.common.mixin.api.CryptoTypeChooserMixin
import jp.co.soramitsu.feature_account_impl.presentation.common.mixin.impl.CryptoTypeChooser
import java.util.Random

@Module
class AccountFeatureModule {

    @Provides
    @FeatureScope
    fun provideJsonDecoder(
        jsonMapper: Gson
    ) = JsonSeedDecoder(jsonMapper)

    @Provides
    @FeatureScope
    fun provideJsonEncoder(
        random: Random,
        jsonMapper: Gson
    ) = JsonSeedEncoder(jsonMapper)

    @Provides
    fun provideCryptoChooserMixin(
        interactor: AccountInteractor,
        resourceManager: ResourceManager
    ): CryptoTypeChooserMixin = CryptoTypeChooser(interactor, resourceManager)

    @Provides
    @FeatureScope
    fun provideAccountRepository(
        accountDataSource: AccountDataSource,
        accountDao: AccountDao,
        nodeDao: NodeDao,
        jsonSeedDecoder: JsonSeedDecoder,
        jsonSeedEncoder: JsonSeedEncoder,
        accountSubstrateSource: AccountSubstrateSource,
        languagesHolder: LanguagesHolder
    ): AccountRepository {
        return AccountRepositoryImpl(
            accountDataSource,
            accountDao,
            nodeDao,
            jsonSeedDecoder,
            jsonSeedEncoder,
            languagesHolder,
            accountSubstrateSource
        )
    }

    @Provides
    @FeatureScope
    fun provideAccountInteractor(
        accountRepository: AccountRepository
    ): AccountInteractor {
        return AccountInteractorImpl(accountRepository)
    }

    @Provides
    @FeatureScope
    fun provideAccountDataSource(
        preferences: Preferences,
        encryptedPreferences: EncryptedPreferences,
        jsonMapper: Gson,
        nodeDao: NodeDao,
        accountDataMigration: AccountDataMigration
    ): AccountDataSource {
        return AccountDataSourceImpl(preferences, encryptedPreferences, nodeDao, jsonMapper, accountDataMigration)
    }

    @Provides
    fun provideNodeHostValidator() = NodeHostValidator()

    @Provides
    @FeatureScope
    fun provideAccountSubstrateSource(socketRequestExecutor: SocketSingleRequestExecutor): AccountSubstrateSource {
        return AccountSubstrateSourceImpl(socketRequestExecutor)
    }

    @Provides
    @FeatureScope
    fun provideAccountDataMigration(
        preferences: Preferences,
        encryptedPreferences: EncryptedPreferences,
        accountDao: AccountDao
    ): AccountDataMigration {
        return AccountDataMigration(preferences, encryptedPreferences, accountDao)
    }

    @Provides
    @FeatureScope
    fun provideExternalAccountActions(
        clipboardManager: ClipboardManager,
        appLinksProvider: AppLinksProvider,
        resourceManager: ResourceManager
    ): ExternalAccountActions.Presentation {
        return ExternalAccountActionsProvider(clipboardManager, appLinksProvider, resourceManager)
    }

    @Provides
    @FeatureScope
    fun provideAccountUpdateScope(
        accountRepository: AccountRepository
    ) = AccountUpdateScope(accountRepository)

    @Provides
    @FeatureScope
    fun provideAddressDisplayUseCase(
        accountRepository: AccountRepository
    ) = AddressDisplayUseCase(accountRepository)

    @Provides
    @FeatureScope
    fun provideAccountUseCase(
        accountRepository: AccountRepository
    ) = SelectedAccountUseCase(accountRepository)
}
