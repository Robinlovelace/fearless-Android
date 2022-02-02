package jp.co.soramitsu.feature_wallet_impl.presentation.balance.manageAssets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import javax.inject.Inject
import jp.co.soramitsu.common.base.BaseFragment
import jp.co.soramitsu.feature_wallet_impl.R

class ManageAssetsFragment : BaseFragment<ManageAssetsViewModel>(), ManageAssetsAdapter.Handler {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ManageAssetsAdapter(this, imageLoader)
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

    override fun switch(item: ManageAssetModel) {
        viewModel.toggleEnabled(item)
    }

    override fun addAccount(item: ManageAssetModel) {
        viewModel.addAccount(item)
    }
}
