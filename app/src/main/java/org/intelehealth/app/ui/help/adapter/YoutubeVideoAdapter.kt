package org.intelehealth.app.ui.help.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.app.databinding.RowItemHelpMostSearchedVideoBinding
import org.intelehealth.app.model.help.YoutubeVideoItem
import org.intelehealth.app.ui.help.viewholder.YoutubeVideoViewHolder
import org.intelehealth.common.ui.adapter.BaseRecyclerViewAdapter

/**
 * Created by Vaghela Mithun R. on 18-02-2025 - 14:42.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * [RecyclerView.Adapter] for displaying a list of [YoutubeVideoItem] objects.
 *
 * This adapter is responsible for creating and binding [YoutubeVideoViewHolder]
 * instances to display YouTube video items in a [RecyclerView]. It extends
 * [BaseRecyclerViewAdapter] to provide common functionality for RecyclerView adapters.
 *
 * @property context The [Context] used for inflating layouts.
 * @property videoItems The [MutableList] of [YoutubeVideoItem] objects to display.
 */
class YoutubeVideoAdapter(
    context: Context,
    videoItems: MutableList<YoutubeVideoItem>
) : BaseRecyclerViewAdapter<YoutubeVideoItem>(context, videoItems) {

    /**
     * Creates a new [YoutubeVideoViewHolder] instance.
     *
     * This method inflates the layout for a single YouTube video item and
     * returns a new [YoutubeVideoViewHolder] to hold it.
     *
     * @param parent The [ViewGroup] into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new [YoutubeVideoViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RowItemHelpMostSearchedVideoBinding.inflate(inflater)
        return YoutubeVideoViewHolder(binding)
    }

    /**
     * Binds a [YoutubeVideoItem] to a [YoutubeVideoViewHolder].
     *
     * This method is called to update the contents of a [YoutubeVideoViewHolder]
     * to reflect the item at the given position.
     *
     * @param holder The [RecyclerView.ViewHolder] which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as YoutubeVideoViewHolder).bind(getItem(position))
    }
}
