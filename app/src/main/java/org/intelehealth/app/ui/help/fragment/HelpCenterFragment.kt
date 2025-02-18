package org.intelehealth.app.ui.help.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentHelpCenterBinding
import org.intelehealth.app.model.help.FAQItem
import org.intelehealth.app.model.help.YoutubeVideoItem
import org.intelehealth.app.ui.help.adapter.FAQAdapter
import org.intelehealth.app.ui.help.adapter.YoutubeVideoAdapter
import org.intelehealth.common.extensions.setupHorizontalLinearView
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.ui.viewholder.BaseViewHolder

/**
 * Created by Vaghela Mithun R. on 10-01-2025 - 17:33.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class HelpCenterFragment : Fragment(R.layout.fragment_help_center), BaseViewHolder.ViewHolderClickListener {
    private lateinit var binding: FragmentHelpCenterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHelpCenterBinding.bind(view)
        bindVideoAdapter()
        bindFAQAdapter()
    }

    private fun bindVideoAdapter() {
        binding.rvHelpMostSearchedVideos.setupHorizontalLinearView(
            YoutubeVideoAdapter(requireContext(), YoutubeVideoItem.generateYoutubeVideoList())
        )
    }

    private fun bindFAQAdapter() {
        val adapter = FAQAdapter(requireContext(), FAQItem.generateFaqList())
        adapter.viewHolderClickListener = this
        binding.rvHelpFAQ.setupLinearView(adapter, true)
    }

    override fun onViewHolderViewClicked(view: View?, position: Int) {
        view ?: return
        if (view.id == R.id.btnExpandableQuestion) {
            val faqItem = (binding.rvHelpFAQ.adapter as FAQAdapter).getItem(position)
            faqItem.isExpanded = !faqItem.isExpanded
            (binding.rvHelpFAQ.adapter as FAQAdapter).notifyItemChanged(position)
        }
    }
}
