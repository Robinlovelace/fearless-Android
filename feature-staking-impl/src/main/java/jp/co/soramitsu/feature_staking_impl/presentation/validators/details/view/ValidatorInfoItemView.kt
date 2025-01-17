package jp.co.soramitsu.feature_staking_impl.presentation.validators.details.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import jp.co.soramitsu.common.utils.dp
import jp.co.soramitsu.common.utils.makeGone
import jp.co.soramitsu.common.utils.makeVisible
import jp.co.soramitsu.common.utils.setCompoundDrawableTint
import jp.co.soramitsu.common.utils.setTextOrHide
import jp.co.soramitsu.common.utils.updatePadding
import jp.co.soramitsu.feature_staking_impl.R
import kotlinx.android.synthetic.main.view_validator_info_item.view.*

class ValidatorInfoItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    init {
        View.inflate(context, R.layout.view_validator_info_item, this)

        updatePadding(start = 16.dp(context), end = 16.dp(context))

        attrs?.let { applyAttributes(it) }
    }

    private fun applyAttributes(attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ValidatorInfoItemView)

        val titleText = typedArray.getString(R.styleable.ValidatorInfoItemView_validatorInfoItemTitle)
        titleText?.let { setTitle(it) }

        val titleIcon = typedArray.getResourceId(R.styleable.ValidatorInfoItemView_validatorInfoItemTitleIcon, 0)
        setTitleIconResource(titleIcon)

        val bodyIcon = typedArray.getResourceId(R.styleable.ValidatorInfoItemView_validatorInfoItemIcon, 0)
        setBodyIconResource(bodyIcon)

        typedArray.recycle()
    }

    fun setDescription(description: String, @DrawableRes icon: Int) {
        validatorsIdentityDescription.makeVisible()
        validatorsIdentityDescription.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        validatorsIdentityDescription.text = description
    }

    fun setTitle(title: String) {
        validatorIdentityTitle.text = title
    }

    fun setBody(body: String) {
        validatorIdentityBody.text = body
    }

    fun setBodyIconResource(resource: Int, @ColorRes tintRes: Int? = null) {
        validatorIdentityBody.setCompoundDrawablesWithIntrinsicBounds(0, 0, resource, 0)

        tintRes?.let {
            validatorIdentityBody.setCompoundDrawableTint(tintRes)
        }
    }

    fun setTitleIconResource(resource: Int) {
        validatorIdentityTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, resource, 0)
    }

    fun showExtra() {
        validatorIdentityExtra.makeVisible()
    }

    fun hideExtra() {
        validatorIdentityExtra.makeGone()
    }

    fun setExtraOrHide(extra: String?) {
        validatorIdentityExtra.setTextOrHide(extra)
    }

    fun showDescription() {
        validatorsIdentityDescription.makeVisible()
    }

    fun hideDescription() {
        validatorsIdentityDescription.makeGone()
    }
}

fun ValidatorInfoItemView.setBodyOrHide(text: String?) {
    if (text == null) {
        makeGone()
    } else {
        setBody(text)
        makeVisible()
    }
}
