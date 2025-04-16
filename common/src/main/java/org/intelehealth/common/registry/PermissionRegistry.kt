package org.intelehealth.common.registry

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.ajalt.timberkt.Timber
import javax.inject.Inject


/**
 * Created by Vaghela Mithun R. on 04/03/21.
 * vaghela.mithun@gmail.com
 */

//fun Map<String, Boolean>.allGranted(): Boolean {
//    if (keys.isEmpty()) return false
//
//    keys.forEach {
//        if (get(it) == false) return false
//    }
//
//    return true
//}

class PermissionRegistry @Inject constructor(
    val context: Context, registry: ActivityResultRegistry
) {
    private var granted = MutableLiveData<Boolean>()
    private val grantedData: LiveData<Boolean> get() = granted

    private val getPermission = registry.register(REGISTRY_KEY, RequestPermission()) { result ->
        if (result) granted.postValue(true)
    }

    private val multiPermission = registry.register(REGISTRY_KEY, RequestMultiplePermissions()) {
        Timber.d { "Permission granted : $it" }
        granted.postValue(it.all { permission -> permission.value })
    }

    fun requestPermission(permission: String): LiveData<Boolean> {
        if (!checkPermission(permission)) getPermission.launch(permission)
        else granted.value = true
        return grantedData
    }

    fun requestPermissions(permissions: Array<String>): LiveData<Boolean> {
        val temp = ArrayList<String>()

        permissions.filter { !checkPermission(it) }.forEach { temp.add(it) }

        if (temp.isEmpty()) granted.value = true
        else multiPermission.launch(temp.toTypedArray())

        return grantedData
    }

    private fun checkPermission(permission: String): Boolean {
        val res: Int = ContextCompat.checkSelfPermission(context, permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    fun removePermissionObserve(owner: LifecycleOwner) {
        grantedData.removeObservers(owner)
        granted = MutableLiveData();
    }

    companion object {
        private const val REGISTRY_KEY = "permission_registry_key"
        const val CAMERA = Manifest.permission.CAMERA
    }
}
