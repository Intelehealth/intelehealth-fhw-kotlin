package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientListBinding
import org.intelehealth.app.ui.patient.adapter.PatientAdapter
import org.intelehealth.app.ui.patient.viewmodel.FindPatientViewModel
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.data.offline.entity.VisitDetail
import java.util.LinkedList

/**
 * Created by Vaghela Mithun R. on 27-05-2025 - 12:39.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@AndroidEntryPoint
class FindPatientFragment : BaseProgressFragment(R.layout.fragment_patient_list),
                            BaseViewHolder.ViewHolderClickListener, SearchView.OnQueryTextListener {

    override val viewModel: FindPatientViewModel by viewModels()
    private lateinit var binding: FragmentPatientListBinding
    private lateinit var adapter: PatientAdapter
    private var searchQuery: String = ""
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPatientListBinding.bind(view)
        bindProgressView(binding.progressView)
        initRecyclerView()
        observePageData()
        observePatientData()
        viewModel.searchPatient(searchQuery)
    }

    private fun initRecyclerView() {
        adapter = PatientAdapter(requireContext(), LinkedList())
        adapter.viewHolderClickListener = this
        binding.rvPatientList.setupLinearView(adapter)
    }

    private fun observePatientData() {
        viewModel.patientLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.handleResponse(it) { patients ->
                adapter.updateItems(patients.toMutableList())
            }
        }
    }

    private fun observePageData() {
        viewModel.patientPageLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe
            hideLoading()
            if (it.isEmpty() && adapter.itemCount > 0) {
                adapter.remove(adapter.itemCount - 1) // Remove the footer if no more data
            } else if (adapter.itemCount > 2) {
                adapter.isLoading = false
                adapter.addItemsAt(adapter.itemCount - 1, it) // Add new items before the footer
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu)
        (menu.findItem(R.id.action_search).actionView)?.let {
            searchView = it.findViewById(R.id.menu_item_search)
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.setOnQueryTextListener(this)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }

    override fun onViewHolderViewClicked(view: View?, position: Int) {
        view ?: return
        if (view.id == org.intelehealth.common.R.id.btnViewMore) {
            // Handle view more button click if needed
            adapter.isLoading = true
            viewModel.loadPage(searchQuery)
            return
        } else if (view.id == R.id.cardPatientItem) {
            Timber.d { "clicked position => $position" }
            val item = view.tag as? VisitDetail ?: return
            val patientId = item.patientId ?: return
            Timber.d { "VisitId nav => $patientId" }
//            findNavController().navigate(PrescriptionFragmentDirections.actionNavPrescriptionToVisitDetails(visitId))
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchView.clearFocus() // Clear focus after submitting the query
        searchQuery = query ?: ""
        viewModel.searchPatient(searchQuery)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { if (it.isEmpty()) viewModel.searchPatient("") }
        return true
    }
}
