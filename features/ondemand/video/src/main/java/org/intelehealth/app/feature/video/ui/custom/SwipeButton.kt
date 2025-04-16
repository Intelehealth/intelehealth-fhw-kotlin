package org.intelehealth.app.feature.video.ui.custom

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.github.ajalt.timberkt.Timber
import org.intelehealth.app.feature.video.R
import org.intelehealth.app.feature.video.databinding.AnimCallActionButtonBinding

/**
 * Created by Vaghela Mithun R. on 19-12-2023 - 19:14.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SwipeButton : FrameLayout, View.OnTouchListener {
    private var viewY = 0f
    private var bounceAnimator: ObjectAnimator = ObjectAnimator()
    private lateinit var binding: AnimCallActionButtonBinding
    lateinit var swipeEventListener: SwipeEventListener
    private var btnIcon: Int = R.drawable.ic_call_accept
    private var btnColor: Int = R.color.colorIncomingCall
    private var btnSize: Int = org.intelehealth.resource.R.dimen.fabDefaultSize
    private var animDelay: Int = 500
    private var animDistance: Float = -150f
    private var arrowVisibility = true

    interface SwipeEventListener {
        fun onTap(view: View)
        fun onReleased(view: View)
        fun onSwipe(view: View)
        fun onCompleted(view: View)
    }

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        obtainAttrs(context, attrs, defStyleAttr)
    }

    private fun obtainAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        btnSize = resources.getDimensionPixelSize(org.intelehealth.resource.R.dimen.fabDefaultSize)
        context.obtainStyledAttributes(attrs, R.styleable.SwipeButton, defStyleAttr, 0).apply {
            btnIcon = getResourceId(R.styleable.SwipeButton_btnIcon, btnIcon)
            btnColor = getResourceId(R.styleable.SwipeButton_btnColor, btnColor)
            btnSize = getResourceId(R.styleable.SwipeButton_btnSize, btnSize)
            animDelay = getInteger(R.styleable.SwipeButton_animDelay, animDelay)
            animDistance = getFloat(R.styleable.SwipeButton_animDistance, animDistance)
            arrowVisibility = getBoolean(R.styleable.SwipeButton_arrowVisibility, arrowVisibility)
            init(context)
        }.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context) {
        LayoutInflater.from(context).let {
            binding = AnimCallActionButtonBinding.inflate(it, this, false)
            binding.hint = context.resources.getString(R.string.call_swipe_up)
            startInfiniteBounceAnimation()
            binding.fabAction.setOnTouchListener(this)

            ColorStateList.valueOf(ContextCompat.getColor(context, btnColor)).apply {
                binding.fabAction.backgroundTintList = this
            }
            binding.fabAction.setImageDrawable(ContextCompat.getDrawable(context, btnIcon))
            binding.fabAction.customSize = resources.getDimensionPixelOffset(btnSize)
            binding.swipeUpIndicator.isVisible = arrowVisibility
            addView(binding.root)
        }
    }

    private fun startInfiniteBounceAnimation() {
        bounceAnimator = ObjectAnimator.ofFloat(binding.fabAction, "translationY", animDistance, 0f)
        bounceAnimator.interpolator = BounceInterpolator()
        bounceAnimator.startDelay = animDelay.toLong()
        bounceAnimator.duration = ANIM_DURATION
        bounceAnimator.repeatCount = ValueAnimator.INFINITE
        bounceAnimator.repeatMode = ValueAnimator.REVERSE
        bounceAnimator.start()
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        view ?: return false
        event?.let {
            if (viewY == 0f) viewY = view.y
            val newY = viewY.coerceAtMost(it.rawY)
            val swipe = viewY - newY
            when (it.action) {
                MotionEvent.ACTION_MOVE -> onActionMove(view, newY, swipe)
                MotionEvent.ACTION_UP -> onActionUp(view, swipe)
                MotionEvent.ACTION_DOWN -> onActionDown(view)
            }

        }
        return true
    }

    private fun onActionMove(view: View, newY: Float, swipe: Float) {
        view.animate().y(newY).setDuration(0).alpha(MAX_ALPHA - (swipe / MIN_SWIPE_DISTANCE) + MIN_ALPHA).start()
        if (::swipeEventListener.isInitialized) swipeEventListener.onSwipe(this)
        if (swipe > MIN_SWIPE_DISTANCE) binding.tvSwipeHint.text = resources.getString(R.string.release_now)
    }

    private fun onActionUp(view: View, swipe: Float) {
        Timber.tag(TAG).d("ACTION_UP")
        if (swipe > MIN_SWIPE_DISTANCE) {
            Timber.tag(TAG).d("$swipe > $MIN_SWIPE_DISTANCE")
            if (::swipeEventListener.isInitialized) swipeEventListener.onCompleted(this)
        } else {
            if (::swipeEventListener.isInitialized) swipeEventListener.onReleased(this)
        }

        view.animate().alpha(MAX_ALPHA).y(viewY).scaleX(SCALE).scaleY(SCALE).setDuration(0).start()
        binding.tvSwipeHint.isInvisible = true
        binding.swipeUpIndicator.isVisible = arrowVisibility
        bounceAnimator.start()
    }

    private fun onActionDown(view: View) {
        binding.hint = context.resources.getString(R.string.call_swipe_up)
        binding.tvSwipeHint.isInvisible = false
        binding.swipeUpIndicator.isVisible = false
        bounceAnimator.pause()
        view.animate().scaleX(MAX_SCALE).scaleY(MAX_SCALE).setDuration(0).start()
        if (::swipeEventListener.isInitialized) swipeEventListener.onTap(this)
    }

//    private fun complete(swiped: Float) {
//        if (swiped > MIN_SWIPE_DISTANCE) {
//            if (::swipeEventListener.isInitialized) swipeEventListener.onCompleted(this)
//        }
//    }

    companion object {
        const val TAG = "AnimCallActionButton"
        const val MIN_SWIPE_DISTANCE = 250f
        const val ANIM_DURATION = 2500L
        const val MAX_SCALE = 1.2f
        const val SCALE = 1.0f
        const val MAX_ALPHA = 1f
        const val MIN_ALPHA = 0.2f
    }
}
