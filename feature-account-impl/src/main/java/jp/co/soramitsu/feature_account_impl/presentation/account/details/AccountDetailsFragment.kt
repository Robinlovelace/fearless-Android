package jp.co.soramitsu.feature_account_impl.presentation.account.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import jp.co.soramitsu.common.base.BaseFragment
import jp.co.soramitsu.common.di.FeatureUtils
import jp.co.soramitsu.common.utils.bindTo
import jp.co.soramitsu.common.utils.nameInputFilters
import jp.co.soramitsu.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import jp.co.soramitsu.feature_account_api.di.AccountFeatureApi
import jp.co.soramitsu.feature_account_api.presenatation.actions.ExternalAccountActions
import jp.co.soramitsu.feature_account_api.presenatation.actions.copyAddressClicked
import jp.co.soramitsu.feature_account_impl.R
import jp.co.soramitsu.feature_account_impl.di.AccountFeatureComponent
import jp.co.soramitsu.feature_account_impl.presentation.account.model.ExportSourceChooserPayload
import jp.co.soramitsu.feature_account_impl.presentation.common.accountSource.SourceTypeChooserBottomSheetDialog
import kotlinx.android.synthetic.main.fragment_account_details.accountDetailsChainAccounts
import kotlinx.android.synthetic.main.fragment_account_details.accountDetailsNameField
import kotlinx.android.synthetic.main.fragment_account_details.accountDetailsToolbar
import javax.inject.Inject

private const val ACCOUNT_ID_KEY = "ACCOUNT_ADDRESS_KEY"

class AccountDetailsFragment : BaseFragment<AccountDetailsViewModel>(), ChainAccountsAdapter.Handler {

    @Inject lateinit var imageLoader: ImageLoader

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ChainAccountsAdapter(this, imageLoader)
    }

    companion object {

        fun getBundle(metaAccountId: Long): Bundle {
            return Bundle().apply {
                putLong(ACCOUNT_ID_KEY, metaAccountId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = layoutInflater.inflate(R.layout.fragment_account_details, container, false)

    override fun initViews() {
        accountDetailsToolbar.setHomeButtonListener {
            viewModel.backClicked()
        }

        accountDetailsNameField.content.filters = nameInputFilters()
        accountDetailsChainAccounts.setHasFixedSize(true)
        accountDetailsChainAccounts.adapter = adapter
    }

    override fun inject() {
        val metaId = argument<Long>(ACCOUNT_ID_KEY)

        FeatureUtils.getFeature<AccountFeatureComponent>(
            requireContext(),
            AccountFeatureApi::class.java
        )
            .accountDetailsComponentFactory()
            .create(this, metaId)
            .inject(this)
    }

    override fun subscribe(viewModel: AccountDetailsViewModel) {
        accountDetailsNameField.content.bindTo(viewModel.accountNameFlow, viewLifecycleOwner.lifecycleScope)

        viewModel.chainAccountProjections.observe { adapter.submitList(it) }

        viewModel.showExternalActionsEvent.observeEvent(::showAccountActions)
        viewModel.showExportSourceChooser.observeEvent(::showExportSourceChooser)
    }

    override fun chainAccountClicked(item: AccountInChainUi) {
    }

    override fun chainAccountOptionsClicked(item: AccountInChainUi) {
        viewModel.chainAccountOptionsClicked(item)
    }

    private fun showAccountActions(payload: ExternalAccountActions.Payload) {
        WalletAccountActionsSheet(
            context = requireContext(),
            content = payload,
            onCopy = viewModel::copyAddressClicked,
            onExternalView = viewModel::viewExternalClicked,
            onExportAccount = viewModel::exportClicked,
            onSwitchNode = viewModel::switchNode
        ).show()
    }

    private fun showExportSourceChooser(payload: ExportSourceChooserPayload) {
        SourceTypeChooserBottomSheetDialog(
            context = requireActivity(),
            payload = DynamicListBottomSheet.Payload(payload.sources),
            onClicked = { viewModel.exportTypeSelected(it, payload.chainId) }
        ).show()
    }
}
