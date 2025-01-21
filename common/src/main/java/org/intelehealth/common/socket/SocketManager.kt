package org.intelehealth.common.socket

import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.Socket.EVENT_CONNECT
import io.socket.client.Socket.EVENT_DISCONNECT
import io.socket.client.SocketIOException
import io.socket.emitter.Emitter
import org.intelehealth.common.socket.user.OnlineUser
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vaghela Mithun R. on 08-06-2023 - 18:47.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Singleton
open class SocketManager @Inject constructor() {
    var socket: Socket? = null
    private var emitterListeners: MutableList<((event: String) -> Emitter.Listener)> = arrayListOf()
    var emitterListener: ((event: String) -> Emitter.Listener)? = null
        set(value) {
            field = value
            value?.let { emitterListeners.add(it) }
        }

    var activeUsers = HashMap<String, OnlineUser>()
    private var isCallTimeUp = false

    fun removeListener(listener: (event: String) -> Emitter.Listener) {
        emitterListeners.removeAt(emitterListeners.indexOf(listener))
    }

    open fun connect(url: String?) {
        if (isConnected()) return
        try {
            url?.let {
                socket = IO.socket(url)
                socket?.on(EVENT_CONNECT, emitter(EVENT_CONNECT))
                socket?.on(EVENT_DISCONNECT, emitter(EVENT_DISCONNECT))
                socket?.on(EVENT_IS_READ, emitter(EVENT_IS_READ))
                socket?.on(EVENT_UPDATE_MESSAGE, emitter(EVENT_UPDATE_MESSAGE))
                socket?.on(EVENT_IP_ADDRESS, emitter(EVENT_IP_ADDRESS))
                socket?.on(EVENT_BYE, emitter(EVENT_BYE))
                socket?.on(EVENT_NO_ANSWER, emitter(EVENT_NO_ANSWER))
                socket?.on(EVENT_CREATED, emitter(EVENT_CREATED))
                socket?.on(EVENT_FULL, emitter(EVENT_FULL))
                socket?.on(EVENT_JOIN, emitter(EVENT_JOIN))
                socket?.on(EVENT_JOINED, emitter(EVENT_JOINED))
                socket?.on(EVENT_READY, emitter(EVENT_READY))
                socket?.on(EVENT_LOG, emitter(EVENT_LOG))
                socket?.on(EVENT_MESSAGE, emitter(EVENT_MESSAGE))
                socket?.on(EVENT_CALL, emitter(EVENT_CALL))
                socket?.on(EVENT_ALL_USER, emitter(EVENT_ALL_USER))
                socket?.on(EVENT_CREATE_OR_JOIN_HW, emitter(EVENT_CREATE_OR_JOIN_HW))
                socket?.on(EVENT_CALL_REJECT_BY_DR, emitter(EVENT_CALL_REJECT_BY_DR))
                socket?.on(EVENT_CALL_CANCEL_BY_DR, emitter(EVENT_CALL_CANCEL_BY_DR))
                socket?.on(EVENT_MSG_DELIVERED, emitter(EVENT_MSG_DELIVERED))
                socket?.on(EVENT_CALL_TIME_UP, emitter(EVENT_CALL_TIME_UP))
                socket?.connect() ?: Timber.e { "Socket is null" }
            } ?: Timber.e { "Socket url must not be empty" }
        } catch (e: SocketIOException) {
            Timber.e { "Invalid Socket url" }
        }
    }

    open fun emitter(event: String) = Emitter.Listener {
        val json: String? = Gson().toJson(it)
        if (event == EVENT_CALL_TIME_UP) {
            isCallTimeUp = true
        }

        when (event) {
            EVENT_ALL_USER -> {
                json?.let { array -> parseAndSaveToLocal(JSONArray(array)); }
            }

            else -> {
                if (isCallTimeUp && event == EVENT_CALL_CANCEL_BY_DR) return@Listener
                invokeListeners(event, it)
            }
        }
    }

    private fun invokeListeners(event: String, args: Any?) {
        emitterListeners.forEach { it.invoke(event).call(args) }
    }

    private fun parseAndSaveToLocal(jsonArray: JSONArray) {
        activeUsers.clear()
        if (jsonArray.length() > 0) {
            validValues(jsonArray) { array ->
                array?.let { parseJson(it) }
            }
        }
    }

    private fun parseJson(array: JSONArray) {
        if (array.length() > 0) {
            for (i in 0 until array.length()) {
                validPairs(array.getJSONObject(i)) {
                    it?.let { json -> saveActiveUsers(json) }
                }
            }
        }
    }

    private fun saveActiveUsers(json: JSONObject) {
        val activeUser = Gson().fromJson(json.toString(), OnlineUser::class.java)
        activeUser?.let {
            activeUser.uuid?.let { it1 ->
                activeUsers.put(it1, activeUser)
            };
        }
    }

    private fun validValues(jsonArray: JSONArray, block: (JSONArray?) -> Unit) {
        if (jsonArray.length() > 0 && jsonArray.getJSONObject(0).has("values")) block.invoke(
            jsonArray.getJSONObject(0).getJSONArray("values")
        )
        else block.invoke(null)
    }

    private fun validPairs(json: JSONObject, block: (JSONObject?) -> Unit) {
        if (json.length() > 0 && json.has("nameValuePairs")) block.invoke(json.getJSONObject("nameValuePairs"))
        else block.invoke(null)
    }

    fun emit(event: String, args: Any? = null) {
        if (isConnected()) {
            socket?.emit(event, args) ?: Timber.e { "$event fail due to socket not connected " }
        } else {
            Timber.e { "$event fail due to socket not connected " }
        }
    }

    fun emitLocalEvent(event: String, args: Any? = null) {
        invokeListeners(event, args)
    }

    open fun disconnect() {
        socket?.off(EVENT_CONNECT)
        socket?.off(EVENT_DISCONNECT)
        socket?.off(EVENT_IS_READ)
        socket?.off(EVENT_UPDATE_MESSAGE)
        socket?.off(EVENT_IP_ADDRESS)
        socket?.off(EVENT_BYE)
        socket?.off(EVENT_NO_ANSWER)
        socket?.off(EVENT_CREATED)
        socket?.off(EVENT_FULL)
        socket?.off(EVENT_JOIN)
        socket?.off(EVENT_JOINED)
        socket?.off(EVENT_READY)
        socket?.off(EVENT_LOG)
        socket?.off(EVENT_MESSAGE)
        socket?.off(EVENT_CALL)
        socket?.off(EVENT_CREATE_OR_JOIN_HW)
        socket?.off(EVENT_ALL_USER)
        socket?.off(EVENT_CALL_REJECT_BY_DR)
        socket?.off(EVENT_CALL_CANCEL_BY_DR)
        socket?.off(EVENT_MSG_DELIVERED)
        socket?.off(EVENT_CALL_TIME_UP)
        socket?.disconnect()
    }

    fun reconnect() = socket?.connect()

    fun isConnected(): Boolean = socket?.connected() ?: false

    fun checkUserIsOnline(id: String) = activeUsers.containsKey(id) && activeUsers.get(id)!!.isOnline()

    fun resetCallTimeUpFlag() {
        isCallTimeUp = false
    }

    fun printSocketStatus() = "Socket connected => ${socket?.connected()}"

    companion object {
        const val TAG = "SocketManager"
        private var socketManager: SocketManager? = null

        @JvmStatic
        var instance = socketManager ?: synchronized(this) {
            socketManager ?: SocketManager().also { socketManager = it }
        }

        const val EVENT_IP_ADDRESS = "ipaddr"
        const val EVENT_BYE = "bye"
        const val EVENT_NO_ANSWER = "no_answer"
        const val EVENT_CREATED = "created"
        const val EVENT_FULL = "full"
        const val EVENT_JOIN = "join"
        const val EVENT_JOINED = "joined"
        const val EVENT_READY = "ready"
        const val EVENT_LOG = "log"
        const val EVENT_MESSAGE = "message"
        const val EVENT_CALL_REJECT_BY_HW = "hw_call_reject"
        const val EVENT_CALL = "call"
        const val EVENT_CREATE_OR_JOIN_HW = "create_or_join_hw"
        const val EVENT_CREATE_OR_JOIN = "create or join"
        const val EVENT_ALL_USER = "allUsers"
        const val EVENT_CALL_REJECT_BY_DR = "dr_call_reject"
        const val EVENT_CALL_CANCEL_BY_HW = "cancel_hw"
        const val EVENT_CALL_CANCEL_BY_DR = "cancel_dr"
        const val EVENT_CALL_TIME_UP = "call_time_up"

        // Local event
        const val EVENT_CALL_HANG_UP = "call_hang_up"

        // Chat message event
        const val EVENT_UPDATE_MESSAGE = "updateMessage"
        const val EVENT_IS_READ = "isread"
        const val EVENT_ACK_MSG_RECEIVED = "ack_msg_received"
        const val EVENT_MSG_DELIVERED = "msg_delivered"

        // Chat event
        const val EVENT_MESSAGE_RECEIVED = "message_received" // message read by receiver
        const val EVENT_MESSAGE_SENT = "message_sent" // sent message
        const val EVENT_MESSAGE_SENT_ACK = "message_sent_ack" // sent message ack
        const val EVENT_MESSAGE_DELIVER = "message_deliver" // message delivered to receiver
        const val EVENT_MESSAGE_READ = "message_read" // sender received
        const val EVENT_MESSAGE_READ_ACK = "message_read_ack" // message mark as read ack
        const val EVENT_CHAT_READ_ACK = "chat_read_ack" // all message mark as read
        const val EVENT_CHAT_READ_ACK_SUCCESS = "chat_read_ack_success" // all message mark as read

        //sender received status, all message read by receiver
        const val EVENT_CHAT_READ = "chat_read"
    }
}