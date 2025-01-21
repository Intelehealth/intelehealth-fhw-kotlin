package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentLoginBinding

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:26.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class LoginFragment: Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
    }
}