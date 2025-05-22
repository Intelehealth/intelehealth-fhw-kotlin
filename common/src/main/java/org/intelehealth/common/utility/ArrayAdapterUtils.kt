package org.intelehealth.common.utility

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import org.intelehealth.common.R
import org.intelehealth.common.ui.adapter.SpinnerItemAdapter

/**
 * Created by Vaghela Mithun R. on 11-07-2024 - 11:46.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
object ArrayAdapterUtils {
    fun getArrayAdapter(
        context: Context, @ArrayRes arrayResId: Int
    ) = ArrayAdapter.createFromResource(
        context, arrayResId, R.layout.view_dropdown_item
    )

    fun <T> getObjectArrayAdapter(
        context: Context, list: List<T>
    ) = ArrayAdapter(context, R.layout.view_dropdown_item, list)

    fun getSpinnerArrayAdapter(
        context: Context, @ArrayRes arrayResId: Int
    ) = SpinnerItemAdapter.createFromResource(context, arrayResId, R.layout.view_dropdown_item)

    fun <T> getSpinnerItemAdapter(
        context: Context, list: List<T>
    ) = SpinnerItemAdapter(context, list, R.layout.view_dropdown_item)

}
