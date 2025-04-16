package org.intelehealth.common.extensions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import org.intelehealth.common.registry.PermissionRegistry

// Extension function to check if all required permissions are granted
fun ComponentActivity.requestNeededPermissions(onPermissionsGranted: (() -> Unit)? = null) {

    val neededPermissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_PHONE_STATE,
    ).toMutableList().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_IMAGES)
//            add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            add(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.USE_FULL_SCREEN_INTENT)
        }
    }.toTypedArray()

    val registry = PermissionRegistry(this, this.activityResultRegistry)
    registry.requestPermissions(neededPermissions).observe(this) {
        if (it) onPermissionsGranted?.invoke()
        else showToast("Permissions are required to proceed")
    }
}
