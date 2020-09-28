package jp.co.soramitsu.feature_wallet_impl.presentation.balance.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.co.soramitsu.common.base.BaseFragment
import jp.co.soramitsu.common.di.FeatureUtils
import jp.co.soramitsu.feature_wallet_api.di.WalletFeatureApi
import jp.co.soramitsu.feature_wallet_impl.R
import jp.co.soramitsu.feature_wallet_impl.di.WalletFeatureComponent
import jp.co.soramitsu.feature_wallet_impl.presentation.model.AssetModel
import jp.co.soramitsu.feature_wallet_impl.util.formatAsCurrency
import kotlinx.android.synthetic.main.fragment_balance_list.balanceListAssets
import kotlinx.android.synthetic.main.fragment_balance_list.balanceListAvatar
import kotlinx.android.synthetic.main.fragment_balance_list.balanceListContent
import kotlinx.android.synthetic.main.fragment_balance_list.balanceListTotalAmount
import kotlinx.android.synthetic.main.fragment_balance_list.transfersContainer

class BalanceListFragment : BaseFragment<BalanceListViewModel>(), BalanceListAdapter.ItemAssetHandler {

    private lateinit var adapter: BalanceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_balance_list, container, false)
    }

    override fun initViews() {
        adapter = BalanceListAdapter(this)
        balanceListAssets.adapter = adapter

        transfersContainer.anchorTo(balanceListContent)

        transfersContainer.setPageLoadListener {
            viewModel.shouldLoadPage()
        }
    }

    override fun inject() {
        FeatureUtils.getFeature<WalletFeatureComponent>(
            requireContext(),
            WalletFeatureApi::class.java
        )
            .balanceListComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: BalanceListViewModel) {
        viewModel.syncAssets()

        viewModel.transactionsLiveData.observe(transfersContainer::showTransactions)

        viewModel.balanceLiveData.observe {
            adapter.submitList(it.assetModels)

            balanceListTotalAmount.text = it.totalBalance.formatAsCurrency()
        }

        viewModel.userIconLiveData.observe {
            balanceListAvatar.setImageDrawable(it)
        }
    }

    override fun assetClicked(asset: AssetModel) {
        viewModel.assetClicked()
    }
}