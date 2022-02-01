package jp.co.soramitsu.feature_wallet_impl.presentation.balance.manageAssets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.co.soramitsu.common.base.BaseFragment
import jp.co.soramitsu.feature_wallet_impl.R

class ManageAssetsFragment : BaseFragment<ManageAssetsViewModel>() {

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ChainAccountsAdapter(this, imageLoader)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_manage_assets, container, false)

    override fun initViews() {

    }

    override fun inject() {
        TODO("Not yet implemented")
    }

    override fun subscribe(viewModel: ManageAssetsViewModel) {
        TODO("Not yet implemented")
    }
}
