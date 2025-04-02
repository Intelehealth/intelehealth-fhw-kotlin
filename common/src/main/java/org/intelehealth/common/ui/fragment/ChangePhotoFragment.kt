package org.intelehealth.common.ui.fragment

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.content.FileProvider
import com.github.ajalt.timberkt.Timber
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.intelehealth.common.registry.PermissionRegistry
import org.intelehealth.common.registry.PermissionRegistry.Companion.CAMERA
import java.io.File
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 27-03-2025 - 18:01.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

abstract class ChangePhotoFragment(@LayoutRes layoutId: Int) : BaseProgressFragment(layoutId) {

    private val permissionRegistry: PermissionRegistry by lazy {
        PermissionRegistry(requireContext(), requireActivity().activityResultRegistry)
    }

    private val uri: Uri by lazy {
        val file = File(requireContext().filesDir, PROFILE_PICTURE_NAME)
        FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider", // Replace with your app's package name + ".provider"
            file
        )
    }

    open fun requestPermission() {
        permissionRegistry.requestPermission(CAMERA).observe(viewLifecycleOwner) {
            permissionRegistry.removePermissionObserve(viewLifecycleOwner)
            if (it == true) showImagePickerDialog()
            else showPermissionDeniedAlert()
        }
    }

    private fun showImagePickerDialog() {
        arrayOf(
            getString(ResourceR.string.action_take_photo),
            getString(ResourceR.string.action_choose_from_gallery),
            getString(ResourceR.string.action_cancel)
        ).also {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(ResourceR.string.dialog_title_select_image)
                setItems(it) { dialog, which ->
                    when (which) {
                        0 -> cameraIntent.launch(uri)
                        1 -> galleryIntent.launch("image/*")
                        2 -> dialog.dismiss()
                    }
                }
            }.show()
        }
    }

    private val cameraIntent = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        Timber.d { "Camera Image URI: $uri" }
        onProfilePictureSelected(uri)
    }

    private val galleryIntent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        Timber.d { "Gallery Image URI: $it" }
        it?.let { path -> onProfilePictureSelected(path) }
    }

    private fun showPermissionDeniedAlert() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setMessage(ResourceR.string.content_reject_permission_warning)
            setPositiveButton(ResourceR.string.action_retry) { _, _ -> requestPermission() }
            setNegativeButton(ResourceR.string.action_ok) { _, _ -> Timber.d { "Permission declined" } }
        }.show()
    }

    abstract fun onProfilePictureSelected(uri: Uri)

    companion object {
        const val PROFILE_PICTURE_NAME = "profile_picture.jpg"
    }
}
