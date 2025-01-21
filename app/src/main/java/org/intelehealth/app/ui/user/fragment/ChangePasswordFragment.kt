package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentChangePasswordBinding

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ChangePasswordFragment: Fragment(R.layout.fragment_change_password) {
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangePasswordBinding.bind(view)
    }
}