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
class AlphabetsInputFilter : InputFilter {
    override fun filter(
        charSequence: CharSequence,
        start: Int,
        end: Int,
        spanned: Spanned?,
        i2: Int,
        i3: Int
    ): CharSequence {
        var keepOriginal = true
        val sb = StringBuilder(end - start)
        for (i in start until end) {
            val c = charSequence[i]
            if (isCharAllowed(c))  // put your condition here
                sb.append(c)
            else keepOriginal = false
        }
        if (keepOriginal) return charSequence
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

    private fun isCharAllowed(c: Char): Boolean { // This allows only alphabets.
        return Character.isLetter(c) || Character.isSpaceChar(c) || (c.code in 0x0900..0x097F) // Unicode range for Devanagari (Marathi, Hindi, etc.);
    }
}
