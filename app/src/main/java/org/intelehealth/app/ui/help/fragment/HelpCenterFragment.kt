package org.intelehealth.app.ui.help.fragment

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.resource.R as ResourceR
import org.intelehealth.app.databinding.FragmentHelpCenterBinding
import org.intelehealth.app.model.help.FAQItem
import org.intelehealth.app.model.help.YoutubeVideoItem
import org.intelehealth.app.ui.help.adapter.FAQAdapter
import org.intelehealth.app.ui.help.adapter.YoutubeVideoAdapter
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.common.extensions.setupHorizontalLinearView
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.extensions.startWhatsappIntent
import org.intelehealth.common.ui.viewholder.BaseViewHolder

/**
 * Created by Vaghela Mithun R. on 10-01-2025 - 17:33.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Fragment for displaying the Help Center screen.
 *
 * This fragment shows a list of frequently asked questions (FAQs) and a
 * list of most searched YouTube videos. It also provides a button to
 * contact support via WhatsApp.
 *
 * The fragment uses [YoutubeVideoAdapter] to display the YouTube videos and
 * [FAQAdapter] to display the FAQs. It also interacts with [UserViewModel]
 * to get the user's name for the support message.
 *
 * This fragment implements [BaseViewHolder.ViewHolderClickListener] to handle
 * click events from the RecyclerView items.
 */
@AndroidEntryPoint
class HelpCenterFragment : Fragment(R.layout.fragment_help_center), BaseViewHolder.ViewHolderClickListener {
    private lateinit var binding: FragmentHelpCenterBinding
    private val userViewModel by viewModels<UserViewModel>()

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been restored in to the view.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHelpCenterBinding.bind(view)
        bindVideoAdapter()
        bindFAQAdapter()
        setHelpSupportClickListener()
    }

    /**
     * Sets the click listener for the help support button.
     *
     * When the button is clicked, it calls [sendNeedSupportMessage] to
     * initiate sending a message to support via WhatsApp.
     */
    private fun setHelpSupportClickListener() {
        binding.fabHelpSupport.setOnClickListener { sendNeedSupportMessage() }
    }

    /**
     * Sends a message to support via WhatsApp.
     *
     * This function retrieves the user's name using [UserViewModel] and
     * then starts a WhatsApp intent to send a pre-filled message to the
     * support number.
     */
    private fun sendNeedSupportMessage() {
        userViewModel.getUserName {
            startWhatsappIntent(
                getString(ResourceR.string.content_support_mobile_no_1),
                getString(ResourceR.string.content_help_whatsapp_string, it)
            )
        }
    }

    /**
     * Binds the [YoutubeVideoAdapter] to the RecyclerView.
     *
     * This function creates a new [YoutubeVideoAdapter] with a list of
     * sample YouTube videos and sets it as the adapter for the
     * `rvHelpMostSearchedVideos` RecyclerView. It also sets the
     * [viewHolderClickListener] to handle click events.
     */
    private fun bindVideoAdapter() {
        val adapter = YoutubeVideoAdapter(requireContext(), YoutubeVideoItem.generateYoutubeVideoList())
        adapter.viewHolderClickListener = this
        binding.rvHelpMostSearchedVideos.setupHorizontalLinearView(adapter)
    }

    /**
     * Binds the [FAQAdapter] to the RecyclerView.
     *
     * This function creates a new [FAQAdapter] with a list of sample FAQs
     * and sets it as the adapter for the `rvHelpFAQ` RecyclerView. It also
     * sets the [viewHolderClickListener] to handle click events and adds an item decoration.
     */
    private fun bindFAQAdapter() {
        val adapter = FAQAdapter(requireContext(), FAQItem.generateFaqList())
        adapter.viewHolderClickListener = this
        binding.rvHelpFAQ.setupLinearView(adapter, true)
    }

    /**
     * Handles click events from the RecyclerView items.
     *
     * This function is called when a view in a RecyclerView item is clicked.
     * It checks the view's ID to determine the action to take:
     * - If the clicked view is `btnExpandableQuestion`, it toggles the
     *   expanded state of the corresponding FAQ item.
     * - If the clicked view is `sivVideoThumbnail`, it navigates to the
     *   YouTube video player.
     *
     * @param view The clicked view.
     * @param position The position of the clicked item in the RecyclerView.
     */
    override fun onViewHolderViewClicked(view: View?, position: Int) {
        view ?: return
        if (view.id == R.id.btnExpandableQuestion) {
            val faqItem = (binding.rvHelpFAQ.adapter as FAQAdapter).getItem(position)
            faqItem.isExpanded = !faqItem.isExpanded // Toggle the expanded state
            (binding.rvHelpFAQ.adapter as FAQAdapter).notifyItemChanged(position)
        } else if (view.id == R.id.sivVideoThumbnail) {
            navigateToYoutubeVideoPlayer(view, position)
        }
    }

    /**
     * Navigates to the YouTube video player.
     *
     * This function retrieves the [YoutubeVideoItem] at the given position
     * and navigates to the `YoutubeVideoPlayer` fragment, passing the video ID
     * as an argument. It also uses a shared element transition for a smooth
     * animation.
     *
     * @param view The clicked view (the video thumbnail).
     * @param position The position of the video item in the RecyclerView.
     */
    private fun navigateToYoutubeVideoPlayer(view: View, position: Int) {
        val videoItem = (binding.rvHelpMostSearchedVideos.adapter as YoutubeVideoAdapter).getItem(position)
        // Open video player activity
        HelpCenterFragmentDirections.actionHelpToYoutubeVideoPlayer(videoItem.videoId).also { direction ->
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(), view, view.transitionName
            ).apply {
                ActivityNavigatorExtras(this).also {
                    findNavController().navigate(direction, it)
                }
            }
        }
    }
}
