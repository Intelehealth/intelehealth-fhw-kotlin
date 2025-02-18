package org.intelehealth.app.model.help

import androidx.annotation.StringRes
import com.google.gson.Gson
import org.intelehealth.resource.R

/**
 * Created by Vaghela Mithun R. on 18-02-2025 - 13:47.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Represents a YouTube video item with its details.
 *
 * This data class holds information about a YouTube video, including its video ID,
 * a description (as a string resource), and a thumbnail URL. It also provides
 * methods to generate an iframe URL for embedding the video, convert the object
 * to JSON, and generate a list of sample YouTube video items.
 *
 * @property videoId The unique identifier of the YouTube video.
 * @property description The string resource ID for the video's description.
 * @property thumbnailUrl The URL of the video's thumbnail. Defaults to a
 *                        standard YouTube thumbnail URL based on the video ID.
 */
data class YoutubeVideoItem(
    val videoId: String,
    @StringRes val description: Int,
    val thumbnailUrl: String = "https://img.youtube.com/vi/$videoId/0.jpg"
) {

    /**
     * Generates an iframe URL for embedding the YouTube video.
     *
     * The generated iframe URL includes autoplay enabled.
     *
     * @return The iframe URL as a [String].
     */
    fun getIframeUrl(): String = "<iframe width=\"100%\" height=\"100%\" " +
            "src=\"https://www.youtube.com/embed/$videoId?autoplay=1\" " +
            "frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" " +
            "allowfullscreen></iframe>"

    /**
     * Converts this [YoutubeVideoItem] object to a JSON string.
     *
     * @return The JSON representation of the object as a [String].
     */
    fun toJson(): String = Gson().toJson(this)

    companion object {
        /**
         * Generates a list of sample [YoutubeVideoItem] objects.
         *
         * This function provides a predefined list of YouTube videos with their
         * corresponding descriptions.
         *
         * @return A [MutableList] of [YoutubeVideoItem] objects.
         */
        fun generateYoutubeVideoList(): MutableList<YoutubeVideoItem> = mutableListOf<YoutubeVideoItem>().apply {
            add(YoutubeVideoItem("TqNiRWOBNTs", R.string.content_treat_mild_fever))
            add(YoutubeVideoItem("LCG6eJ0j-Cg", R.string.content_what_is_anemia))
            add(YoutubeVideoItem("qbDHSwMOYg4", R.string.content_treat_cough_at_home))
            add(YoutubeVideoItem("E0UAHVoqcm0", R.string.content_benefits_of_walking))
        }
    }
}
