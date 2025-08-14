package org.intelehealth.app.ui.language.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.app.databinding.LanguageListItemViewUi2Binding
import org.intelehealth.app.databinding.RowItemLanguageBinding
import org.intelehealth.common.ui.adapter.BaseRecyclerViewAdapter
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.config.room.entity.ActiveLanguage

/**
 * Created by Vaghela Mithun R. on 15-04-2024 - 14:12.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * RecyclerView adapter for displaying a list of selectable languages.
 *
 * This adapter extends [BaseRecyclerViewAdapter] to manage a list of
 * [ActiveLanguage] objects. It provides functionality to display language
 * items and visually indicate the currently selected language.
 *
 * @param context The context used to inflate the item layout.
 * @param languages The initial list of languages to display.
 */
class LanguageAdapter(context: Context, languages: List<ActiveLanguage>) :
    BaseRecyclerViewAdapter<ActiveLanguage>(context, languages.toMutableList()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RowItemLanguageBinding.inflate(inflater, parent, false).let {
            LanguageViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LanguageViewHolder) {
            viewHolderClickListener?.let { holder.setViewClickListener(it) }
            holder.bind(getItem(position))
        }
    }

    /**
     * Selects a language at the specified position and updates the UI.
     *
     * This method deselects all other languages in the list, marks the language
     * at the given position as selected, and notifies the adapter to refresh the
     * displayed items.
     *
     * @param position The position of the language to select.
     * @param language The [ActiveLanguage] object to select.
     */
    fun select(position: Int, language: ActiveLanguage) {
        getList().forEach { it.selected = false }
        language.selected = true
        getList().toMutableList()[position] = language
        notifyItemRangeChanged(0, itemCount)
    }

    fun setDefaultLang(lang: String) {
        getList().forEach { activeLanguage ->
            activeLanguage.selected = activeLanguage.code == lang
        }
        notifyItemRangeChanged(0, itemCount)
    }
}

/**
 * ViewHolder for displaying a selectable language item in a RecyclerView.
 *
 * This ViewHolder binds an [ActiveLanguage] object to the views in
 * `RowItemLanguageBinding`, allowing the user to select a language. It handles
 * updating the UI to reflect the selected state of the language.
 *
 * @param binding The data binding object for the language item layout.
 */
class LanguageViewHolder(private val binding: RowItemLanguageBinding) :
    BaseViewHolder(binding.root) {

    /**
     * Binds an [ActiveLanguage] object to the views in the item layout.
     *
     * This method sets the language data on the binding object, configures the
     * click listener for the item, and updates the UI to reflect whether the
     * language is currently selected.
     *
     * @param language The [ActiveLanguage] object to bind to the views.
     */
    fun bind(language: ActiveLanguage) {
        binding.layoutRbChooseLanguage.tag = language
        binding.layoutRbChooseLanguage.setOnClickListener(this)
        binding.layoutRbChooseLanguage.isSelected = language.selected
        binding.rbChooseLanguage.isChecked = language.selected
        binding.language = language
    }
}
