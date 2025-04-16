package org.intelehealth.app.ui.onboarding.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentIntroSliderBinding
import org.intelehealth.app.model.IntroContent

/**
 * Created by Tanvir Hasan on 25-03-24
 * Email: mhasan@intelehealth.org
 */
private const val ARG_PARAM1 = "param1"

/**
 * A fragment that displays a single introduction slide.
 *
 * This fragment is responsible for rendering the content of an individual
 * introduction slide. It receives the slide content as a serializable
 * [IntroContent] object via its arguments and binds it to the views in its layout.
 */
@AndroidEntryPoint
class IntroSlideFragment : Fragment(R.layout.fragment_intro_slider) {
    private lateinit var binding: FragmentIntroSliderBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroSliderBinding.bind(view)
        arguments?.let {
            val content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(ARG_PARAM1, IntroContent::class.java)
            } else {
                @Suppress("DEPRECATION") (it.getSerializable(ARG_PARAM1) as IntroContent)
            }

            binding.content = content
        }
    }

    companion object {
        private const val ARG_PARAM1 = "intro_content"

        /**
         * Creates a new instance of [IntroSlideFragment] with the given content.
         *
         * @param introContent The [IntroContent] object representing the content
         *                     to display in the slide.
         * @return A new instance of [IntroSlideFragment] with the content set as
         *         arguments.
         */
        @JvmStatic
        fun newInstance(introContent: IntroContent) = IntroSlideFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, introContent)
            }
        }
    }
}
