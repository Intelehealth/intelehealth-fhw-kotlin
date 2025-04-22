package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPrescriptionPendingBinding

class PrescriptionPendingFragment : Fragment(R.layout.fragment_prescription_pending) {
    lateinit var binding: FragmentPrescriptionPendingBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionPendingBinding.bind(view)
    }
}