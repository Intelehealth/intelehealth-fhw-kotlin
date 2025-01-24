package org.intelehealth.app.model

import android.content.Context
import java.io.Serializable
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 25-04-2024 - 11:11.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
enum class ViewType {
    ONE, TWO, THREE
}

data class IntroContent(
    val imageUrl: String? = null,
    val isResourceImage: Boolean,
    val resId: Int = 0,
    val title: String,
    val content: String
) : Serializable {
    companion object {
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