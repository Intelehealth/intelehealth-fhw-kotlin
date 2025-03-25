package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentUserProfileBinding
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.common.ui.fragment.StateFragment

/**
 * Created by Vaghela Mithun R. on 25-03-2025 - 17:50.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class UserProfileFragment : StateFragment(R.layout.fragment_user_profile) {
    override val viewModel: UserViewModel by viewModels<UserViewModel>()
    private lateinit var binding: FragmentUserProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserProfileBinding.bind(view)
    }
}
