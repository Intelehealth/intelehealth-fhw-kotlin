package org.intelehealth.common.inputfilter

import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils

/**
 * Created by Vaghela Mithun R. on 07-05-2025 - 16:42.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class AlphaNumericInputFilter : InputFilter {
    //filter input for name fields
    override fun filter(
        charSequence: CharSequence,
        start: Int,
        end: Int,
        spanned: Spanned?,
        i2: Int,
        i3: Int
    ): CharSequence? {
        var keepOriginal = true
        val sb = StringBuilder(end - start)
        for (i in start..<end) {
            val c = charSequence[i]
            if (isCharAllowed(c))  // put your condition here
                sb.append(c)
            else keepOriginal = false
        }
        if (keepOriginal) return null
        else {
            if (charSequence is Spanned) {
                val sp = SpannableString(sb)
                TextUtils.copySpansFrom(charSequence, start, sb.length, null, sp, 0)
                return sp
            } else {
                return sb
            }
        }
    }

    private fun isCharAllowed(c: Char): Boolean { // This allows only number and alphabets.
        return Character.isLetterOrDigit(c) || Character.isSpaceChar(c) || (c.code >= 0x0900 && c.code <= 0x097F) // Unicode range for Devanagari (Marathi, Hindi, etc.);
    }
}
