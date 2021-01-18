package jp.co.soramitsu.feature_account_impl.presentation.account.mixin.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import io.reactivex.Single
import jp.co.soramitsu.common.account.AddressIconGenerator
import jp.co.soramitsu.common.account.AddressModel
import jp.co.soramitsu.common.resources.ResourceManager
import jp.co.soramitsu.common.utils.combine
import jp.co.soramitsu.common.utils.mediatorLiveData
import jp.co.soramitsu.common.utils.setFrom
import jp.co.soramitsu.feature_account_api.domain.interfaces.AccountInteractor
import jp.co.soramitsu.feature_account_api.domain.model.Account
import jp.co.soramitsu.feature_account_api.domain.model.Node
import jp.co.soramitsu.feature_account_impl.data.mappers.mapAccountToAccountModel
import jp.co.soramitsu.feature_account_impl.data.mappers.mapNetworkTypeToNetworkModel
import jp.co.soramitsu.feature_account_impl.presentation.account.mixin.api.AccountListing
import jp.co.soramitsu.feature_account_impl.presentation.account.mixin.api.AccountListingMixin
import jp.co.soramitsu.feature_account_impl.presentation.account.model.AccountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

private const val ICON_SIZE_IN_DP = 24

@Suppress("EXPERIMENTAL_API_USAGE")
class AccountListingProvider(
    private val accountInteractor: AccountInteractor,
    private val resourceManager: ResourceManager,
    private val addressIconGenerator: AddressIconGenerator
) : AccountListingMixin {

    private val groupedAccountModelsLiveData = getGroupedAccounts()
        .asLiveData()

    override val selectedAccountLiveData: MutableLiveData<AccountModel> = mediatorLiveData {
        setFrom(getSelectedAccountModel().asLiveData())
    }

    override val accountListingLiveData = groupedAccountModelsLiveData
        .combine(selectedAccountLiveData) { groupedAccounts, selected ->
            AccountListing(groupedAccounts, selected)
        }

    private fun getSelectedAccountModel() = accountInteractor.selectedAccountFlow()
        .mapLatest { transformAccount(it) }

    private fun getGroupedAccounts() = accountInteractor.groupedAccountsFlow()
        .mapLatest { transformToModels(it) }
        .flowOn(Dispatchers.Default)

    private suspend fun transformToModels(list: List<Any>): List<Any> {
        return list.map {
            when (it) {
                is Account -> transformAccount(it)
                is Node.NetworkType -> Single.just(mapNetworkTypeToNetworkModel(it))
                else -> throw IllegalArgumentException()
            }
        }
    }

    private suspend fun transformAccount(account: Account): AccountModel {
        val addressModel = generateIcon(account.address)

        return mapAccountToAccountModel(account, addressModel.image, resourceManager)
    }

    private suspend fun generateIcon(address: String): AddressModel {
        return addressIconGenerator.createAddressModel(address, ICON_SIZE_IN_DP)
    }
}