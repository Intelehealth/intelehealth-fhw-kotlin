package org.intelehealth.app.ui.patient.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientListBinding
import org.intelehealth.app.ui.patient.adapter.PatientAdapter
import org.intelehealth.app.ui.patient.viewmodel.FindPatientViewModel
import org.intelehealth.common.extensions.changeToWhiteOverlayTheme
import org.intelehealth.common.extensions.setSpaceItemDecoration
import org.intelehealth.common.extensions.setupHorizontalLinearView
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.model.RecentItem
import org.intelehealth.common.ui.adapter.RecentHistoryAdapter
import org.intelehealth.common.ui.fragment.StateFragment
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.data.offline.entity.RecentHistory
import org.intelehealth.data.offline.entity.VisitDetail
import java.util.LinkedList
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 27-05-2025 - 12:39.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@AndroidEntryPoint
class FindPatientFragment : StateFragment(
    R.layout.fragment_patient_list
), BaseViewHolder.ViewHolderClickListener, SearchView.OnQueryTextListener {

    override val viewModel: FindPatientViewModel by viewModels()
    private lateinit var binding: FragmentPatientListBinding
    private lateinit var adapter: PatientAdapter
    private lateinit var recentItemAdapter: RecentHistoryAdapter
    private var searchQuery: String = ""
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPatientListBinding.bind(view)
//        bindProgressView(binding.progressView)
        initRecyclerView()
        initRecentSearchHistoryView()
        observePageData()
        observePatientData()
        observeRecentSearchHistory()
        viewModel.searchPatient(searchQuery)
        binding.btnClearRecentSearch.setOnClickListener { clearAllRecentSearchHistory() }
    }

    private fun initRecentSearchHistoryView() {
        RecentHistoryAdapter(
            requireContext(),
            arrayListOf(),
            hasCloseIcon = false
        ).apply {
            recentItemAdapter = this
            this.viewHolderClickListener = this@FindPatientFragment
            binding.rvRecentSearchList.setupHorizontalLinearView(this)
            binding.rvRecentSearchList.setSpaceItemDecoration(
                space = resources.getDimensionPixelSize(ResourceR.dimen.std_8dp),
                orientation = DividerItemDecoration.HORIZONTAL
            )
        }
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
                binding.noPatientFoundBlock.root.isVisible = false
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

    private fun observeRecentSearchHistory() {
        viewModel.getRecentSearchHistory().observe(viewLifecycleOwner) { history ->
            history ?: return@observe
            Timber.d { "Recent search history: $history" }
            if (history.isNotEmpty()) {
                recentItemAdapter.updateItems(history.toMutableList())
                binding.groupRecentSearchList.isVisible = true
            } else binding.groupRecentSearchList.isVisible = false
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu)
        (menu.findItem(R.id.action_search).actionView)?.let {
            searchView = it.findViewById(R.id.menu_item_search)
            searchView.changeToWhiteOverlayTheme()
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
        } else if (view.id == org.intelehealth.common.R.id.chipRecentHistory) {
            // Handle close button click in recent search history
            val item = view.tag as? RecentItem ?: return
            Timber.d { "Close clicked for item: $item" }
            if (item is RecentHistory) {
                viewModel.updateRecentSearchHistory(item)
                searchQuery = item.value
                searchView.setQuery(searchQuery, false)
                viewModel.searchPatient(searchQuery)
            }
            return
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchView.clearFocus() // Clear focus after submitting the query
        searchQuery = query ?: ""
        viewModel.searchPatient(searchQuery)
        if (searchQuery.isNotEmpty()) viewModel.addRecentSearchHistory(searchQuery)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { if (it.isEmpty()) viewModel.searchPatient("") }
        return true
    }

    private fun clearAllRecentSearchHistory() {
        viewModel.clearRecentSearchHistory()
        recentItemAdapter.updateItems(mutableListOf())
        binding.groupRecentSearchList.isVisible = false
    }

    /**
     * Called when the data fetch fails.
     *
     * Hides the data state group and shows the "no patient found" block when the data fetch fails.
     *
     * @param reason The reason for the failure, which can be logged or displayed to the user.
     */
    override fun onFailed(reason: String) {
        super.onFailed(reason)
        adapter.clear()
        binding.noPatientFoundBlock.root.isVisible = true
    }
}
