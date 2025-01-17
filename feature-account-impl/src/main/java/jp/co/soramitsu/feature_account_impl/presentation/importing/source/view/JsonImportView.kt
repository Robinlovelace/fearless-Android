package jp.co.soramitsu.feature_account_impl.presentation.importing.source.view

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import jp.co.soramitsu.common.utils.EventObserver
import jp.co.soramitsu.common.utils.bindTo
import jp.co.soramitsu.common.utils.nameInputFilters
import jp.co.soramitsu.common.view.InputField
import jp.co.soramitsu.feature_account_impl.R
import jp.co.soramitsu.feature_account_impl.presentation.importing.source.model.ImportSource
import jp.co.soramitsu.feature_account_impl.presentation.importing.source.model.JsonImportSource
import kotlinx.android.synthetic.main.import_source_json.view.*

class JsonImportView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImportSourceView(R.layout.import_source_json, context, attrs, defStyleAttr) {

    override val nameInputView: InputField
        get() = importJsonUsernameInput

    init {
        importJsonUsernameInput.editText!!.filters = nameInputFilters()
    }

    override fun observeSource(source: ImportSource, lifecycleOwner: LifecycleOwner) {
        require(source is JsonImportSource)

        source.jsonContentLiveData.observe(lifecycleOwner, Observer(importJsonContent::setMessage))

        source.showJsonInputOptionsEvent.observe(
            lifecycleOwner,
            EventObserver {
                showJsonInputOptionsSheet(source)
            }
        )

        importJsonPasswordInput.content.bindTo(source.passwordLiveData, lifecycleOwner)

        importJsonContent.setActionClickListener {
            source.chooseFileClicked()
        }

        importJsonContent.setOnClickListener {
            source.jsonClicked()
        }
    }

    private fun showJsonInputOptionsSheet(source: JsonImportSource) {
        JsonPasteOptionsSheet(context, source::pasteClicked, source::chooseFileClicked)
            .show()
    }
}
