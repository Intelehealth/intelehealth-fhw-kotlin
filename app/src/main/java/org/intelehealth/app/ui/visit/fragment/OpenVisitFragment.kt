package org.intelehealth.app.ui.visit.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentOpenVisitBinding
import org.intelehealth.app.ui.visit.viewmodel.OpenVisitViewModel
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewmodel.BaseViewModel

/**
 * Created by Vaghela Mithun R. on 11-07-2025 - 12:05.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class OpenVisitFragment : BaseProgressFragment(R.layout.fragment_open_visit) {
    override val viewModel: OpenVisitViewModel by viewModels()
    private lateinit var binding: FragmentOpenVisitBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOpenVisitBinding.bind(view)
    }
}
