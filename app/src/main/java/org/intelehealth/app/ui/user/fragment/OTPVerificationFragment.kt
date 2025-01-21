package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import org.intelehealth.app.R
import android.view.View
import org.intelehealth.app.databinding.FragmentOtpVerificationBinding

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class OTPVerificationFragment: Fragment(R.layout.fragment_otp_verification) {
    private lateinit var binding: FragmentOtpVerificationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOtpVerificationBinding.bind(view)
    }
}