package org.intelehealth.app.ui.help.viewholder

import com.github.ajalt.timberkt.Timber
import org.intelehealth.app.databinding.RowItemHelpMostSearchedVideoBinding
import org.intelehealth.app.model.help.YoutubeVideoItem
import org.intelehealth.common.ui.viewholder.BaseViewHolder

/**
 * Created by Vaghela Mithun R. on 18-02-2025 - 14:44.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * [RecyclerView.ViewHolder] for displaying a [YoutubeVideoItem] in a [RecyclerView].
 *
 * This ViewHolder is responsible for binding a [YoutubeVideoItem] to the
 * corresponding layout (`RowItemHelpMostSearchedVideoBinding`) and displaying
 * its data. It extends [BaseViewHolder] to provide common functionality for
 * ViewHolders.
 *
 * @property binding The [RowItemHelpMostSearchedVideoBinding] instance for this ViewHolder.
 */
class YoutubeVideoViewHolder(private val binding: RowItemHelpMostSearchedVideoBinding) : BaseViewHolder(binding.root) {

    /**
     * Binds a [YoutubeVideoItem] to the ViewHolder's layout.
     *
     * This method updates the layout with the data from the provided
     * [YoutubeVideoItem] and executes any pending data binding updates.
     * It also logs the JSON representation of the [YoutubeVideoItem] for debugging purposes.
     *
     * @param youtubeVideoItem The [YoutubeVideoItem] to bind to the layout.
     */
    fun bind(youtubeVideoItem: YoutubeVideoItem) {
        binding.youtubeVideoItem = youtubeVideoItem
        binding.executePendingBindings()
        Timber.d { youtubeVideoItem.toJson() }
    }
}
