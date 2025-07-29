package org.intelehealth.app.ui.notification.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentNotificationListBinding
import org.intelehealth.app.databinding.FragmentPrescriptionListBinding
import org.intelehealth.app.ui.notification.adapter.NotificationAdapter
import org.intelehealth.app.ui.notification.viewmodel.NotificationViewModel
import org.intelehealth.app.ui.prescription.adapter.PrescriptionAdapter
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionViewModel
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.CommonConstants
import java.util.LinkedList

@AndroidEntryPoint
class NotificationListFragment() : BaseProgressFragment(R.layout.fragment_notification_list), BaseViewHolder.ViewHolderClickListener {

    private lateinit var binding: FragmentNotificationListBinding
    private lateinit var adapter: NotificationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationListBinding.bind(view)

        initNotificationListView()
        getNotifidata()
        observePagingData()
    }

    fun getNotifidata(){
        viewModel.notifications.observe(viewLifecycleOwner, Observer {list ->
            list ?: return@Observer
            viewModel.handleResponse(list) { data ->
                adapter.updateItems(data.toMutableList())

            }
        })
    }

    private fun initNotificationListView() {
        adapter = NotificationAdapter(requireActivity(), LinkedList())
        adapter.viewHolderClickListener = this
        binding.rvNotificationList.setupLinearView(adapter, false)
    }

    override val viewModel: NotificationViewModel by viewModels<NotificationViewModel>()

    private fun observePagingData() {

        viewModel.notificationLiveData.observe(viewLifecycleOwner) {
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

    override fun onViewHolderViewClicked(view: View?, position: Int) {
        view ?: return
        if (view.id == org.intelehealth.common.R.id.btnViewMore) {
            adapter.isLoading = true
            viewModel.loadPage()
            return
        }
    }
}