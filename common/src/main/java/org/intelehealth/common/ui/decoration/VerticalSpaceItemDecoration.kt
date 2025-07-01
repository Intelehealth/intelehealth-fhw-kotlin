package org.intelehealth.common.ui.decoration

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Vaghela Mithun R. on 01-07-2025 - 16:58.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class VerticalSpaceItemDecoration(private val verticalSpace: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: android.graphics.Rect,
        view: android.view.View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = verticalSpace
    }
}
