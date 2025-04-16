package org.intelehealth.app.ui.specialization

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.intelehealth.config.room.entity.Specialization
import org.intelehealth.config.utility.ResUtils

/**
 * Created by Vaghela Mithun R. on 17-04-2024 - 14:50.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * An ArrayAdapter for displaying a list of specializations in a Spinner.
 *
 * This adapter is used to populate a Spinner with a list of
 * [Specialization] objects. It provides methods for retrieving the count,
 * item, and item ID, as well as a custom implementation for getting the view
 * for each item in the list.
 *
 * @property context The context in which the adapter is being used.
 * @property specializations The list of [Specialization] objects to be
 *                           displayed.
 */
class SpecializationArrayAdapter(
    private val context: Context, private val specializations: List<Specialization>
) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)
    override fun getCount(): Int = specializations.size

    override fun getItem(position: Int): Specialization = specializations[position]

    override fun getItemId(position: Int): Long = position.toLong()

    /**
     * Gets the position of a specialization in the list based on its name.
     *
     * @param value The name of the specialization to search for.
     * @return The position of the specialization in the list, or 0 if not
     *         found.  If multiple specializations have the same name, the
     *         position of the first one encountered will be returned.
     */
    fun getPosition(value: String): Int {
        specializations.forEachIndexed { index, specialization ->
            if (specialization.name == value) return index
        }
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView: View? = view
        val holder: SpecializationViewHolder
        if (convertView == null) {
            // TODO: Consider using a custom layout for the Spinner items
            // instead of the default simple_spinner_dropdown_item.
            convertView = inflater.inflate(
                android.R.layout.simple_spinner_dropdown_item, parent, false
            )

            holder = SpecializationViewHolder(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as SpecializationViewHolder
        }

        holder.bindItem(context, getItem(position))
        return holder.getRootView()
    }
}

/**
 * ViewHolder for displaying a specialization in a Spinner.
 *
 * This ViewHolder is responsible for binding a [Specialization] object to a
 * TextView, which is used to display the specialization's name in a Spinner.
 *
 * @property view The root view of the ViewHolder, which should be a TextView.
 */
class SpecializationViewHolder(view: View) {
    val textView: TextView = view as TextView

    /**
     * Returns the root view of the ViewHolder.
     *
     * @return The TextView representing the specialization.
     */
    fun getRootView() = textView

    /**
     * Binds a specialization to the TextView.
     *
     * This method sets the text of the TextView to the localized name of the
     * specialization, retrieved using [ResUtils] and the specialization's key.
     *
     * @param context The context in which the ViewHolder is being used.
     * @param specialization The [Specialization] object to bind.
     */
    fun bindItem(context: Context, specialization: Specialization) {
        // TODO: Consider using data binding or view binding to avoid direct
        // view manipulation.
        textView.text = ResUtils.getStringResourceByName(context, specialization.sKey)
    }
}
