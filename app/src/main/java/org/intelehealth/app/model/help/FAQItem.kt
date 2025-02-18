package org.intelehealth.app.model.help

import androidx.annotation.StringRes
import org.intelehealth.resource.R

/**
 * Created by Vaghela Mithun R. on 18-02-2025 - 14:06.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Represents a Frequently Asked Question (FAQ) item.
 *
 * This data class holds a question and its corresponding answer, both
 * represented as string resource IDs. It also provides a method to generate
 * a list of sample FAQ items.
 *
 * @property question The string resource ID for the FAQ question.
 * @property answer The string resource ID for the FAQ answer.
 */
data class FAQItem(
    @StringRes val question: Int,
    @StringRes val answer: Int,
    var isExpanded: Boolean = false
) {
    companion object {
        /**
         * Generates a list of sample [FAQItem] objects.
         *
         * This function provides a predefined list of frequently asked questions
         * and their corresponding answers.
         *
         * @return A [MutableList] of [FAQItem] objects.
         */
        fun generateFaqList(): MutableList<FAQItem> = mutableListOf<FAQItem>().apply {
            add(FAQItem(R.string.content_how_intelehealth_work, R.string.content_how_intelehealth_work_ans))
            add(FAQItem(R.string.content_why_intelehealth_exist, R.string.content_why_intelehealth_exist_ans))
            add(FAQItem(R.string.content_how_intelehealth_help, R.string.content_how_intelehealth_help_ans))
            add(FAQItem(R.string.content_how_to_register, R.string.content_how_to_register_ans))
            add(FAQItem(R.string.content_how_to_add_new_visit, R.string.content_how_to_add_new_visit_ans))
            add(FAQItem(R.string.content_how_to_book_an_appointment, R.string.content_how_to_book_an_appointment_ans))
        }
    }
}
