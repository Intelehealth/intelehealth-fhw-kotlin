package org.intelehealth.common.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import org.intelehealth.resource.R

/**
 * Created by Vaghela Mithun R. on 03-01-2025 - 13:53.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class DialogParams(
    @DrawableRes val icon: Int = 0,
    @StringRes val title: Int,
    @StringRes val message: Int,
    @StringRes val positiveLbl: Int = R.string.lbl_ok,
    @StringRes val negativeLbl: Int = 0,
    val onPositiveClick: () -> Unit = {},
    val onNegativeClick: () -> Unit = {}
)
