package jp.co.soramitsu.feature_wallet_impl.presentation.balance.manageAssets

import jp.co.soramitsu.runtime.multiNetwork.chain.model.ChainId

data class ManageAssetModel(
    val chainId: ChainId,
    val assetId: String,
    val tokenSymbol: String,
    val name: String,
    val iconUrl: String,
    // will be null if there is no account for this network
    val amount: String?,
    // will be null if the tokens are for the native network
    val network: Network?,
    val position: Int,
    val enabled: Boolean,
    val isTestNet: Boolean
) {
    data class Network(val iconUrl: String, val name: String)
}
