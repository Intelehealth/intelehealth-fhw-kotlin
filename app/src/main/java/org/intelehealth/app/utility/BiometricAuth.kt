package org.intelehealth.app.utility


/**
 * Created by Vaghela Mithun R. on 03-04-2025 - 19:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Utility class for handling biometric authentication.
 *
 * This class provides methods to check for biometric availability and display a
 * biometric prompt to the user for authentication. It uses the
 * [BiometricManager] and [BiometricPrompt] APIs from the AndroidX library.
 *
 * @property context The application context.
 */
class BiometricAuth @Inject constructor(@ApplicationContext private val context: Context) {

    private val biometricManager = BiometricManager.from(context)

    /**
     * Checks if biometric authentication is available on the device.
     *
     * This method determines whether the device supports strong biometric
     * authentication (such as fingerprint or face recognition) or device
     * credentials (PIN, pattern, or password).
     *
     * @return `true` if biometric authentication is available, `false` otherwise.
     */
    fun isBiometricAvailable(): Boolean {
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
                    or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * Displays a biometric authentication prompt to the user.
     *
     * This method shows a dialog prompting the user to authenticate using their
     * biometric credentials. It takes success and failure callbacks to handle the
     * authentication result.
     *
     * @param activity The [FragmentActivity] used to display the prompt.
     * @param onSuccess A callback to be executed when authentication succeeds.
     * @param onFailure A callback to be executed when authentication fails.
     */
    fun showBiometricPrompt(activity: FragmentActivity, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailure()
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
