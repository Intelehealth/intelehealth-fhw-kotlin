package org.intelehealth.common.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

/**
 * Created by Vaghela Mithun R. on 23-01-2025 - 14:30.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class MenuFragment(@LayoutRes layoutId: Int): Fragment(layoutId), MenuProvider {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}