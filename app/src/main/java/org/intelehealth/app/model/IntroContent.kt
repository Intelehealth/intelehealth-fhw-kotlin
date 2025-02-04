package org.intelehealth.app.model

import android.content.Context
import java.io.Serializable
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 25-04-2024 - 11:11.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Enumerates the different types of views used in the application,
 * specifically for distinguishing between different introductory or onboarding screens.
 *
 * Each value in this enum represents a unique view type, allowing for
 * different content and layouts to be associated with each type.
 *
 * - `ONE`: Represents the first view type, typically the first screen in a sequence.
 * - `TWO`: Represents the second view type, typically the second screen in a sequence.
 * - `THREE`: Represents the third view type, typically the third screen in a sequence.
 */
enum class ViewType {
    ONE, TWO, THREE
}

/**
 * Represents the content for an introductory screen or onboarding slide.
 *
 * This data class holds the information needed to display a single slide in an
 * introduction or onboarding flow. It can represent an image (either from a URL
 * or a local resource), a title, and descriptive content.
 *
 * @property imageUrl The URL of the image to display, if applicable.
 * @property isResourceImage Indicates whether the image is a local resource (true) or from a URL (false).
 * @property resId The resource ID of the image to display, if it's a local resource.
 * @property title The title text for the slide.
 * @property content The descriptive content text for the slide.
 */
data class IntroContent(
    val imageUrl: String? = null,
    val isResourceImage: Boolean,
    val resId: Int = 0,
    val title: String,
    val content: String
) : Serializable {
    companion object {
        /**
         * Retrieves the appropriate [IntroContent] based on the given [ViewType].
         *
         * This function provides a convenient way to get the content for a specific
         * introductory slide. It uses a [ViewType] to determine which slide's content
         * to return. The content includes an image (from resources), a title, and
         * descriptive text, all localized using the provided [Context].
         *
         * @param context The application context, used for accessing resources and strings.
         * @param viewType The [ViewType] indicating which slide's content to retrieve.
         * @return The [IntroContent] for the specified [ViewType].
         */
        @JvmStatic
        fun getContent(context: Context, viewType: ViewType) = when (viewType) {
            ViewType.ONE -> IntroContent(
                isResourceImage = true,
                resId = ResourceR.drawable.onboarding_image_1,
                title = context.getString(ResourceR.string.intro_title_1),
                content = context.getString(ResourceR.string.intro_tagline_1)
            )

            ViewType.TWO -> IntroContent(
                isResourceImage = true,
                resId = ResourceR.drawable.onboarding_image_2,
                title = context.getString(ResourceR.string.intro_title_2),
                content = context.getString(ResourceR.string.intro_tagline_2)
            )

            ViewType.THREE -> IntroContent(
                isResourceImage = true,
                resId = ResourceR.drawable.onboarding_image_3,
                title = context.getString(ResourceR.string.intro_title_3),
                content = context.getString(ResourceR.string.intro_tagline_3)
            )
        }
    }
}