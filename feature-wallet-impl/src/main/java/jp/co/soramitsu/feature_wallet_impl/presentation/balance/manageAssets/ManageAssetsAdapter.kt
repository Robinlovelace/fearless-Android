package jp.co.soramitsu.feature_wallet_impl.presentation.balance.manageAssets

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import jp.co.soramitsu.common.utils.inflateChild
import jp.co.soramitsu.feature_wallet_impl.R
import kotlinx.android.synthetic.main.item_manage_asset.view.manageAssetsAccountExistStateGroup
import kotlinx.android.synthetic.main.item_manage_asset.view.manageAssetsAddAccountButton
import kotlinx.android.synthetic.main.item_manage_asset.view.manageAssetsBadge
import kotlinx.android.synthetic.main.item_manage_asset.view.manageAssetsItemAmount
import kotlinx.android.synthetic.main.item_manage_asset.view.manageAssetsItemIcon
import kotlinx.android.synthetic.main.item_manage_asset.view.manageAssetsItemName
import kotlinx.android.synthetic.main.item_manage_asset.view.manageAssetsItemSwitch
import kotlinx.android.synthetic.main.item_manage_asset.view.manageAssetsMissingAccountStateGroup

class ManageAssetsAdapter(private val handler: Handler, private val imageLoader: ImageLoader) :
    ListAdapter<ManageAssetModel, ManageAssetViewHolder>(diffUtil) {

    interface Handler {
        fun switch(item: ManageAssetModel)
        fun addAccount(item: ManageAssetModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageAssetViewHolder {
        val itemView = parent.inflateChild(R.layout.item_manage_asset)

        return ManageAssetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ManageAssetViewHolder, position: Int) =
        with(holder.itemView) {
            val item = getItem(position)

            manageAssetsItemIcon.load(item.iconUrl, imageLoader)
            manageAssetsItemName.text = item.name

            manageAssetsAccountExistStateGroup.isVisible = item.amount?.let {
                manageAssetsItemAmount.text = item.amount
                manageAssetsItemSwitch.isChecked = item.enabled
                holder.setupNetworkBadge(item.network)
                true
            } ?: false

            manageAssetsMissingAccountStateGroup.isVisible = item.amount == null

            manageAssetsItemSwitch.setOnCheckedChangeListener { _, _ ->
                this@ManageAssetsAdapter.handler.switch(item)
            }

            manageAssetsAddAccountButton.setOnClickListener {
                this@ManageAssetsAdapter.handler.addAccount(item)
            }
        }

    private fun ManageAssetViewHolder.setupNetworkBadge(model: ManageAssetModel.Network?) = itemView.apply {
        manageAssetsBadge.isVisible = model?.let {
            manageAssetsBadge.setIcon(it.iconUrl, imageLoader)
            manageAssetsBadge.setText(stringText = it.name)
            true
        } ?: false
    }

}

class ManageAssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

private val diffUtil = object : DiffUtil.ItemCallback<ManageAssetModel>() {
    override fun areItemsTheSame(oldItem: ManageAssetModel, newItem: ManageAssetModel) =
        oldItem.chainId == newItem.chainId && oldItem.tokenSymbol == newItem.tokenSymbol

    override fun areContentsTheSame(oldItem: ManageAssetModel, newItem: ManageAssetModel) =
        oldItem == newItem

}
