package org.intelehealth.common.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import org.intelehealth.common.ui.custom.CustomImageSpan
import org.intelehealth.resource.R
import org.intelehealth.common.utility.DateTimeResource
import org.intelehealth.common.utility.DateTimeUtils
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Created by Vaghela Mithun R. on 03-08-2023 - 20:36.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
enum class ImageSpanGravity {
    TOP, CENTER, BOTTOM, START, END
}

fun String.toDate(format: String): Date {
    return DateTimeUtils.parseUTCDate(this, format)
}

fun String.toLocalDateFormat(format: String): String {
    return this.toDate(DateTimeUtils.DB_FORMAT).toWeekDays(format)
}

fun String.milliToLogTime(format: String): String {
    val resource = DateTimeResource.getInstance()
    val different = System.currentTimeMillis() - this.toLong()
    val days = TimeUnit.MILLISECONDS.toDays(different)
    val hours = TimeUnit.MILLISECONDS.toHours(different)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(different)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(different)

    return if (days >= 1) Calendar.getInstance().let {
        it.timeInMillis = this.toLong()
        return@let it.time
    }.toWeekDaysWithTime(format)
    else if (hours >= 1) {
        if (hours.toInt() == 1) resource?.getResourceString(R.string.an_hour_ago) ?: "An hour ago"
        else resource?.getResourceString(R.string.hours_ago, "$hours") ?: "$hours hrs ago"
    } else if (minutes >= 1) {
        if (minutes.toInt() == 1) resource?.getResourceString(R.string.a_min_ago) ?: "A min ago"
        else resource?.getResourceString(R.string.mins_ago, "$minutes") ?: "$minutes mins ago"
    } else if (seconds >= 1) {
        if (minutes.toInt() == 1) resource?.getResourceString(R.string.a_second_ago) ?: "A sec ago"
        else resource?.getResourceString(R.string.seconds_ago, "$seconds") ?: "$seconds secs ago"
    } else resource?.getResourceString(R.string.now) ?: "Now"
}

fun Date.toWeekDays(format: String): String {
    val resource = DateTimeResource.getInstance()
    return if (DateTimeUtils.isToday(this)) {
        resource?.getResourceString(R.string.today) ?: "Today"
    } else if (DateTimeUtils.isYesterday(this)) {
        resource?.getResourceString(R.string.yesterday) ?: "Yesterday"
    } else DateTimeUtils.formatToLocalDate(this, format)
}

fun Date.toWeekDaysWithTime(format: String): String {
    val resource = DateTimeResource.getInstance()
    val time = DateTimeUtils.formatToLocalDate(this, DateTimeUtils.TIME_FORMAT)
    return if (DateTimeUtils.isToday(this)) {
        resource?.getResourceString(R.string.today_at, time) ?: "Today at $time"
    } else if (DateTimeUtils.isYesterday(this)) {
        resource?.getResourceString(R.string.yesterday_at, time) ?: "Yesterday at $time"
    } else DateTimeUtils.formatToLocalDate(this, format)
}

fun String.span(@ColorRes colorRes: Int, context: Context) = SpannableString(this).apply {
    setSpan(
        ForegroundColorSpan(
            ContextCompat.getColor(context, colorRes)
        ), 0, this.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

fun String.imageSpan(context: Context, @DrawableRes iconResId: Int, gravity: ImageSpanGravity) =
    SpannableString("  $this").apply {
        ContextCompat.getDrawable(context, iconResId)?.let { icon ->
            icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)

            val imageSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ImageSpan(icon, ImageSpan.ALIGN_CENTER)
            } else {
                CustomImageSpan(icon, CustomImageSpan.ALIGN_CENTER)
            }

            if (gravity == ImageSpanGravity.START) this.setSpan(
                imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            else if (gravity == ImageSpanGravity.END) this.setSpan(
                imageSpan, length, length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } ?: this

        return@apply
    }

fun String.mapWithResourceId(context: Context): String {
    val resId = when (this) {
        "Otp sent successfully!" -> R.string.content_otp_sent_successfully
        "No user exists with this phone number/email/username." -> R.string.content_no_user_exists
        "No phoneNumber/email updated for this username." -> R.string.content_no_phone_number_email_updated
        "No user exists with this username." -> R.string.content_no_user_exists_with_username
        "Otp verified successfully!" -> R.string.content_otp_verified_successfully
        "Otp expired!" -> R.string.content_otp_expired
        "Otp incorrect!" -> R.string.content_otp_incorrect
        "Password reset successful." -> R.string.content_password_reset_successful
        "No user exists!" -> R.string.content_no_user_found
        else -> 0
    }
    if (resId == 0) return this
    return context.resources.getString(resId)
}

fun String.containsDigit(): Boolean {
    return this.any { it.isDigit() }
}