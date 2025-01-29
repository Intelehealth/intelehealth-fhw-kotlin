package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentChangePasswordBinding
import org.intelehealth.app.databinding.FragmentResetPasswordBinding

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ResetPasswordFragment: Fragment(R.layout.fragment_reset_password) {
    private lateinit var binding: FragmentResetPasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResetPasswordBinding.bind(view)
    }
}