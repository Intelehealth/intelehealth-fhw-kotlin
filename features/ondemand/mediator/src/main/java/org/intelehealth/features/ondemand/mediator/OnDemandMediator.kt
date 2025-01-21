package org.intelehealth.features.ondemand.mediator

import android.content.Context
import android.util.Log

/**
 * Created by Vaghela Mithun R. on 04-10-2024 - 18:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

const val TAG = "OnDemandMediator"
const val VIDEO_CALL_IMPL_CLASS = "org.intelehealth.app.feature.video.impl.VideoCallMediatorImpl"
const val CHAT_ROOM_IMPL_CLASS = "org.intelehealth.feature.chat.impl.ChatRoomMediatorImpl"
const val TEST_IMPL_CLASS = "org.intelehealth.dynamicfeature.TestModuleImpl"
inline fun <reified T> createInstance(implClass: String): T? {
    try {
        return Class.forName(implClass).getConstructor().newInstance() as T
    } catch (e: ClassNotFoundException) {
        Log.e(TAG, "Implementor class not found", e)
        return null
    }
}