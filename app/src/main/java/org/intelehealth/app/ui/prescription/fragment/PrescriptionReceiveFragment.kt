package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPrescriptionPendingBinding
import org.intelehealth.app.databinding.FragmentPrescriptionReceiveBinding


class PrescriptionReceiveFragment : Fragment(R.layout.fragment_prescription_receive) {
    lateinit var binding: FragmentPrescriptionReceiveBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionReceiveBinding.bind(view)
    }
}