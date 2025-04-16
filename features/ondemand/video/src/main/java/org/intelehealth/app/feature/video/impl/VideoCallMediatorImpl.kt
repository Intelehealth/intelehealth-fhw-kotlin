package org.intelehealth.app.feature.video.impl

import android.content.Context
import android.content.Intent
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import org.intelehealth.app.BuildConfig
import org.intelehealth.fcm.utils.NotificationHandler.isAppInForeground
import org.intelehealth.features.ondemand.mediator.listener.VideoCallMediator
import org.intelehealth.app.feature.video.model.CallArgs
import org.intelehealth.app.feature.video.ui.activity.IDACallLogActivity
import org.intelehealth.app.feature.video.utils.CallHandlerUtils
import org.intelehealth.app.feature.video.utils.CallIntentUtils
import org.intelehealth.app.feature.video.utils.CallMode
import org.intelehealth.app.feature.video.utils.CallType
import org.intelehealth.common.extensions.fromJson

/**
 * Created by Vaghela Mithun R. on 26-09-2024 - 18:12.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class VideoCallMediatorImpl : VideoCallMediator {
    override fun onIncomingCall(context: Context?, data: HashMap<String, String>) {
        context?.let {
//            val sessionManager = SessionManager(context)
            Gson().fromJson<CallArgs>(Gson().toJson(data)).apply {
//                nurseName = sessionManager.chwname
                callType = CallType.VIDEO
                url = BuildConfig.LIVE_KIT_URL
                socketUrl = BuildConfig.SOCKET_URL + "?userId=" + nurseId + "&name=" + nurseName
//                PatientsDAO().getPatientName(roomId).apply {
//                    patientName = get(0).name
//                }
            }.also { arg ->
                Timber.tag(TAG).d("onMessageReceived: $arg")
                if (isAppInForeground()) {
                    arg.callMode = CallMode.INCOMING
                    CallHandlerUtils.saveIncomingCall(context, arg)
                    context.startActivity(CallIntentUtils.getCallActivityIntent(arg, context))
                } else {
                    CallHandlerUtils.operateIncomingCall(it, arg)
                }
            }
        }
    }

    override fun startCallLogActivity(context: Context?) {
        Intent(context, IDACallLogActivity::class.java).apply {
            context?.startActivity(this)
        }
    }

    companion object {
        const val TAG = "VideoCallListenerImpl"
    }
}