package jp.co.soramitsu.feature_wallet_impl.presentation.balance.manageAssets

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.soramitsu.common.utils.inflateChild
import jp.co.soramitsu.feature_wallet_impl.R
import jp.co.soramitsu.runtime.multiNetwork.chain.model.ChainId

class ManageAssetsAdapter : ListAdapter<ManageAssetModel, ManageAssetViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageAssetViewHolder {
        val itemView = parent.inflateChild(R.layout.item_manage_asset)

        return ManageAssetViewHolder(itemView).apply {

        }
    }

    override fun onBindViewHolder(holder: ManageAssetViewHolder, position: Int) {

    }

}

class ManageAssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

data class ManageAssetModel(
    val chainId: ChainId,
    val assetId: String,
    val name: String,
    val iconUrl: String,
    val token: String,
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

private val diffUtil = object : DiffUtil.ItemCallback<ManageAssetModel>() {
    override fun areItemsTheSame(oldItem: ManageAssetModel, newItem: ManageAssetModel) =
        oldItem.chainId == newItem.chainId && oldItem.assetId == newItem.assetId

    override fun areContentsTheSame(oldItem: ManageAssetModel, newItem: ManageAssetModel) =
        oldItem == newItem

}
