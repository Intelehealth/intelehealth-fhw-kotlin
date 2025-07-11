package org.intelehealth.common.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.LayoutRes
import org.intelehealth.common.R

/**
 * Created by Vaghela Mithun R. on 08-05-2025 - 12:31.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A custom [ArrayAdapter] for populating a [android.widget.Spinner] with a list of items.
 *
 * This adapter can be used to display a list of items (of any type `T`) in a
 * [android.widget.Spinner]. It provides a simple way to customize the layout
 * of each item in the dropdown.
 *
 * @param context The application [Context].
 * @param items The list of items to display in the [android.widget.Spinner].
 * @param layoutResId The layout resource ID for each item in the dropdown.
 *   Defaults to `R.layout.view_dropdown_item`.
 * @param T The type of items in the list.
 *
 * @constructor Creates a new [SpinnerItemAdapter] with the specified context,
 *   items, and layout resource ID.
 */
class SpinnerItemAdapter<T>(
    private val context: Context,
    private val items: List<T>,
    @LayoutRes private val layoutResId: Int = R.layout.view_dropdown_item
) : ArrayAdapter<T>(context, layoutResId, items) {

    /**
     * Gets a View that displays the data at the specified position in the data set.
     *
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: View.inflate(context, layoutResId, null)
        view.id = position
        val item = getItem(position)
        (view as TextView).text = item.toString()
        view.contentDescription = item.toString()
        return view
    }

    companion object {
        /**
         * Creates a [SpinnerItemAdapter] from a string array resource.
         *
         * This factory method provides a convenient way to create a
         * [SpinnerItemAdapter] from a string array defined in your resources.
         *
         * @param context The application [Context].
         * @param arrayResId The string array resource ID.
         * @param layoutResId The layout resource ID for each item in the
         *   dropdown. Defaults to `R.layout.view_dropdown_item`.
         * @return A new [SpinnerItemAdapter] populated with the strings from the
         *   array resource.
         */
        fun createFromResource(
            context: Context,
            @ArrayRes arrayResId: Int,
            @LayoutRes layoutResId: Int = R.layout.view_dropdown_item
        ) = SpinnerItemAdapter(
            context,
            context.resources.getStringArray(arrayResId).toList(),
            layoutResId
        )
    }
}
