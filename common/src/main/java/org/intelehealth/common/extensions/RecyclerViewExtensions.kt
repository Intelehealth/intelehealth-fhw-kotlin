package org.intelehealth.common.extensions

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.common.ui.decoration.HorizontalSpaceItemDecoration
import org.intelehealth.common.ui.decoration.VerticalSpaceItemDecoration

/**
 * Extension function for [RecyclerView] to set up a linear layout manager and
 * an adapter, with an optional item decoration.
 *
 * This function simplifies the process of configuring a [RecyclerView] to
 * display items in a linear fashion. It sets the [LinearLayoutManager],
 * optionally adds an item decoration, and sets the provided adapter.
 *
 * @param adapter The [RecyclerView.Adapter] to use for the [RecyclerView].
 * @param hasItemDecoration `true` if an item decoration should be added,
 *                          `false` otherwise. Defaults to `false`.
 */
fun RecyclerView.setupLinearView(
    adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
    hasItemDecoration: Boolean = false
) {
    layoutManager = LinearLayoutManager(this.context)
    if (hasItemDecoration) setItemDecoration()
    this.adapter = adapter
}

/**
 * Extension function for [RecyclerView] to set up a horizontal linear layout manager and
 * an adapter, with an optional item decoration.
 *
 * This function simplifies the process of configuring a [RecyclerView] to
 * display items in a horizontal linear fashion. It sets the [LinearLayoutManager]
 * with horizontal orientation, optionally adds an item decoration, and sets the provided adapter.
 *
 * @param adapter The [RecyclerView.Adapter] to use for the [RecyclerView].
 * @param hasItemDecoration `true` if an item decoration should be added,
 *                          `false` otherwise. Defaults to `false`.
 */
fun RecyclerView.setupHorizontalLinearView(
    adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
    hasItemDecoration: Boolean = false
) {
    layoutManager = LinearLayoutManager(this.context).apply {
        orientation = LinearLayoutManager.HORIZONTAL
    }
    if (hasItemDecoration) setItemDecoration()
    this.adapter = adapter
}

/**
 * Adds a [DividerItemDecoration] to the [RecyclerView] to visually separate items.
 *
 * This extension function simplifies the process of adding a divider between
 * items in a [RecyclerView]. It creates a [DividerItemDecoration] with the
 * specified orientation and adds it to the [RecyclerView].
 *
 * @param orientation The orientation of the divider. Can be either
 *                    [DividerItemDecoration.VERTICAL] or
 *                    [DividerItemDecoration.HORIZONTAL]. Defaults to
 *                    [DividerItemDecoration.VERTICAL].
 */
fun RecyclerView.setItemDecoration(orientation: Int = DividerItemDecoration.VERTICAL) {
    addItemDecoration(DividerItemDecoration(this.context, orientation))
}

fun RecyclerView.setSpaceItemDecoration(
    space: Int,
    orientation: Int = DividerItemDecoration.VERTICAL
) {
    if (orientation == DividerItemDecoration.VERTICAL)
        addItemDecoration(VerticalSpaceItemDecoration(space))
    else
        addItemDecoration(HorizontalSpaceItemDecoration(space))
}

/**
 * Extension function for [RecyclerView] to set up a chat list layout.
 *
 * This function configures a [RecyclerView] to display a chat-like list,
 * where new items are added at the bottom and the list automatically scrolls
 * to the latest item. It sets the [LinearLayoutManager] with `stackFromEnd`
 * set to `true` and `reverseLayout` set to `false`.
 *
 * @param adapter The [RecyclerView.Adapter] to use for the [RecyclerView].
 */
fun RecyclerView.setupChatList(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    layoutManager = LinearLayoutManager(this.context).apply {
        stackFromEnd = true
        reverseLayout = false
    }
    this.adapter = adapter
}
