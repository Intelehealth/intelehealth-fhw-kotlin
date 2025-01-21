package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentForgotPasswordBinding

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForgotPasswordBinding.bind(view)
    }
}