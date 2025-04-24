package org.intelehealth.common.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.intelehealth.common.R

/**
 * Created by Tanvir Hasan on 24-04-25
 * Email : mhasan@intelehealth.org
 **/

class VisitDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_visit_details, container, false)
    }
}