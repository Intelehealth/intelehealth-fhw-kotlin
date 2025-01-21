package org.intelehealth.installer.downloader

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import org.intelehealth.installer.helper.DownloadProgressNotificationHelper

/**
 * Created by Vaghela Mithun R. on 09-10-2024 - 20:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

const val TAG = "dynamic_module_manager"

class DynamicModuleDownloadManager private constructor(context: Context) {

    companion object {
        @Volatile
        private var instance: DynamicModuleDownloadManager? = null

        @JvmStatic
        fun getInstance(context: Context): DynamicModuleDownloadManager = instance ?: synchronized(this) {
            instance ?: DynamicModuleDownloadManager(context).also { instance = it }
        }
    }

    private var mySessionId = 0
    private var callback: DynamicDeliveryCallback? = null

    private val listener by lazy {
        SplitInstallStateUpdatedListener { state -> handleInstallStates(state) }
    }

    private val splitInstallManager by lazy {
        SplitInstallManagerFactory.create(context)
    }

//    private val downloadProgressHelper by lazy {
//        DownloadProgressNotificationHelper.getInstance(context)
//    }

    fun isModuleDownloaded(moduleName: String): Boolean {
        println("${TAG}=>isModuleDownloaded=>$moduleName")
        return splitInstallManager.installedModules.contains(moduleName)
    }

    fun requestToDownloadModule(module: String, isActive: Boolean, onInactive: () -> Unit) {
        if (isActive.not()) onInactive.invoke()
        else if (isActive && isModuleDownloaded(module).not()) downloadDynamicModule(module)
        else onInactive.invoke()
    }

    fun showDownloadingNotification() {
//        downloadProgressHelper.setTitle("Intelehealth")
//        downloadProgressHelper.setContent("Downloading...")
//        downloadProgressHelper.startNotifying()

        object : CountDownTimer(10000, 1000) {
            override fun onTick(p0: Long) {
                val progress = 100 - ((p0 * 100) / 10000).toInt()
                Log.e("FeatureDownloadService ", "Interval $progress ==> $p0")
//                downloadProgressHelper.updateProgress(progress)
            }

            override fun onFinish() {
//                downloadProgressHelper.setContent("Download complete")
//                downloadProgressHelper.completeProgress()
            }

        }.start()
    }

    private fun initNotification() {
        println("${TAG}=>initNotification")
//        downloadProgressHelper.setTitle("Intelehealth")
//        downloadProgressHelper.setContent("Downloading...")
//        downloadProgressHelper.startNotifying()
    }

    fun downloadDynamicModule(moduleName: String) {
        val request = SplitInstallRequest.newBuilder().addModule(moduleName).build()
        println("${TAG}=>downloadDynamicModule=>$moduleName")

        splitInstallManager.startInstall(request).addOnSuccessListener { sessionId ->
            mySessionId = sessionId
        }.addOnFailureListener { e ->
            Log.d(TAG, "Exception: $e")
            handleInstallFailure((e as SplitInstallException).errorCode)
        }
    }

    fun downloadDynamicModules(modules: List<String>) {
        val builder = SplitInstallRequest.newBuilder()
        modules.forEach {
            if (!isModuleDownloaded(it)) {
                builder.addModule(it)
            }
        }

        splitInstallManager.startInstall(builder.build()).addOnSuccessListener { sessionId ->
            mySessionId = sessionId
        }.addOnFailureListener { e ->
            Log.d(TAG, "Exception: $e")
            handleInstallFailure((e as SplitInstallException).errorCode)
        }
    }

    fun registerListener(callback: DynamicDeliveryCallback?) {
        this.callback = callback
        splitInstallManager.registerListener(listener)
    }

    fun unregisterListener() {
        splitInstallManager.unregisterListener(listener)
    }

    /** Install all features deferred. */
    fun installAllFeaturesDeferred(modules: List<String>, callback: DynamicDeliveryCallback?) {

//        splitInstallManager.sessionStates.addOnSuccessListener {
//            println("${TAG}=>sessionStates=>Success")
//        }.addOnFailureListener {
//            println("${TAG}=>sessionStates=>Failure")
//        }.addOnCanceledListener {
//            println("${TAG}=>sessionStates=>Canceled")
//        }.addOnCompleteListener {
//            println("${TAG}=>sessionStates=>Complete")
//        }
//        val modules = listOf(moduleKotlin, moduleJava, moduleAssets, moduleNative)
        initNotification()
        splitInstallManager.deferredInstall(modules).addOnSuccessListener {

        }.addOnFailureListener { e ->
            Log.d(TAG, "Exception: $e")
            handleInstallFailure((e as SplitInstallException).errorCode)
        }

    }

    /** Request uninstall of all features. */
    fun requestUninstall() {

        Log.d(
            TAG, "Requesting uninstall of all modules. This will happen at some point in the future."
        )

        val installedModules = splitInstallManager.installedModules.toList()
        splitInstallManager.deferredUninstall(installedModules).addOnSuccessListener {
            Log.d(TAG, "Uninstalling $installedModules")
        }
    }

    /** Request uninstall of all features. */
    fun requestUninstall(modules: List<String>) {
        splitInstallManager.deferredUninstall(modules).addOnSuccessListener {
            Log.d(TAG, "Uninstalling $modules")
            println("${TAG}=>Uninstalling $modules")
        }.addOnCompleteListener {
            println("${TAG}=>Uninstalling $modules completed")
        }.addOnFailureListener {
            println("${TAG}=>Uninstalling $modules failed due to ${it.message}")
        }
    }


    private fun handleInstallFailure(errorCode: Int) {
        when (errorCode) {
            SplitInstallErrorCode.NETWORK_ERROR -> {
                println("${TAG}=>NETWORK_ERROR")
                callback?.onFailed("No internet found")
//                cancelNotificationWithMessage("No internet found")
            }

            SplitInstallErrorCode.MODULE_UNAVAILABLE -> {
                println("${TAG}=>MODULE_UNAVAILABLE")
                callback?.onFailed("Module unavailable")
//                cancelNotificationWithMessage("Module unavailable")
            }

            SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> {
                println("${TAG}=>ACTIVE_SESSIONS_LIMIT_EXCEEDED")
                callback?.onFailed("Active session limit exceeded")
//                cancelNotificationWithMessage("Active session limit exceeded")
            }

            SplitInstallErrorCode.INSUFFICIENT_STORAGE -> {
                println("${TAG}=>INSUFFICIENT_STORAGE")
                callback?.onFailed("Insufficient storage")
//                cancelNotificationWithMessage("Insufficient storage")
            }

            SplitInstallErrorCode.PLAY_STORE_NOT_FOUND -> {
                println("${TAG}=>PLAY_STORE_NOT_FOUND")
                callback?.onFailed("Google Play Store Not Found!")
//                cancelNotificationWithMessage("Google Play Store Not Found!")
            }

            else -> {
                println("${TAG}=>Something went wrong! Try again later")
                callback?.onFailed("Something went wrong! Try again later")
//                cancelNotificationWithMessage("Something went wrong! Try again later")
            }
        }
    }

    private fun handleInstallStates(state: SplitInstallSessionState) {
        if (state.sessionId() == mySessionId) {
            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    if (state.bytesDownloaded() > 0 && state.totalBytesToDownload() > 0) {
                        val percentage = (state.bytesDownloaded() * 100) / state.totalBytesToDownload()
                        println("${TAG}=>DOWNLOADING percentage => $percentage")
                        callback?.onDownloading(percentage.toInt())
//                        downloadProgressHelper.updateProgress(percentage.toInt())
                    }
                    println("${TAG}=>DOWNLOADING totalBytesToDownload => ${state.totalBytesToDownload()}")
                    println("${TAG}=>DOWNLOADING bytesDownloaded => ${state.bytesDownloaded()}")
                }

                SplitInstallSessionStatus.DOWNLOADED -> {
                    println("${TAG}=>DOWNLOADED")
                    callback?.onDownloadCompleted()
//                    downloadProgressHelper.setContent("Installing...")
//                    downloadProgressHelper.completeProgress()
                }

                SplitInstallSessionStatus.INSTALLED -> {
                    println("${TAG}=>INSTALLED")
                    Log.d(TAG, "Dynamic Module downloaded")
                    callback?.onInstallSuccess()
//                    cancelNotificationWithMessage("Installation complete")
                }

                SplitInstallSessionStatus.FAILED -> {
                    println("${TAG}=>FAILED=> code ${state.errorCode()}")
                    callback?.onFailed("Installation failed")
//                    cancelNotificationWithMessage("Installation failed")
                    handleInstallFailure(state.errorCode())
                }

                SplitInstallSessionStatus.CANCELED -> {
                    println("${TAG}=>CANCELED")
                    callback?.onFailed("Installation Cancelled")
//                    cancelNotificationWithMessage("Installation Cancelled")
                }

                SplitInstallSessionStatus.CANCELING -> {
                    println("${TAG}=>CANCELING")
//                    cancelNotificationWithMessage("Installation Cancelled")
                }

                SplitInstallSessionStatus.INSTALLING -> {
                    println("${TAG}=>INSTALLING")
//                    cancelNotificationWithMessage("Installing...")
                }

                SplitInstallSessionStatus.PENDING -> {
                    println("${TAG}=>PENDING=> code ${state.errorCode()}")
//                    cancelNotificationWithMessage("Pending")
                }

                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    println("${TAG}=>REQUIRES_USER_CONFIRMATION")
//                    cancelNotificationWithMessage("Require user confirmation")
                }

                SplitInstallSessionStatus.UNKNOWN -> {
                    println("${TAG}=>UNKNOWN")
//                    cancelNotificationWithMessage("Unknown=>${state.errorCode()}")
                }
            }
        }
    }

    private fun cancelNotificationWithMessage(message: String) {
//        downloadProgressHelper.setContent(message)
//        downloadProgressHelper.startNotifying()
//        downloadProgressHelper.cancelWithDelay(1000)
    }
}