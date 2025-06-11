package org.intelehealth.app.ui.visit.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.model.CommonHeaderSection
import org.intelehealth.common.model.ListItemFooter
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.CommonConstants
import org.intelehealth.data.offline.entity.VisitDetail
import org.intelehealth.resource.R
import java.util.LinkedList
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 10-06-2025 - 14:19.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
open class VisitViewModel @Inject constructor() : BaseViewModel() {
    var offset = 0

    var otherSectionAdded = false
    var addMoreSectionAdded = false

    fun generatePrescriptionListWithHeaderSection(
        visitDetails: List<VisitDetail>
    ): List<ListItemHeaderSection> {
        val listWithSection = LinkedList<ListItemHeaderSection>()
        val today = visitDetails.filter { it.section == CommonConstants.TODAY }
        val yesterday = visitDetails.filter { it.section == CommonConstants.YESTERDAY }
        val thisWeek = visitDetails.filter { it.section == CommonConstants.THIS_WEEK }
        val thisMonth = visitDetails.filter { it.section == CommonConstants.THIS_MONTH }
        val other = visitDetails.filter { it.section == CommonConstants.OTHER }

        if (today.isEmpty() && yesterday.isEmpty() && thisWeek.isEmpty() && thisMonth.isEmpty() && other.isEmpty()) {
            return emptyList()
        }

        if (today.isNotEmpty()) {
            listWithSection.add(CommonHeaderSection(R.string.lbl_today_visits))
            listWithSection.addAll(today)
        }

        if (yesterday.isNotEmpty()) {
            listWithSection.add(CommonHeaderSection(R.string.lbl_yesterday_visits))
            listWithSection.addAll(yesterday)
        }

        if (thisWeek.isNotEmpty()) {
            listWithSection.add(CommonHeaderSection(R.string.lbl_this_week_visits))
            listWithSection.addAll(thisWeek)
        }

        if (thisMonth.isNotEmpty()) {
            listWithSection.add(CommonHeaderSection(R.string.lbl_this_month_visits))
            listWithSection.addAll(thisMonth)
        }

        if (other.isNotEmpty()) {
            listWithSection.add(CommonHeaderSection(R.string.lbl_this_other_visits))
            listWithSection.addAll(other)
            if (!otherSectionAdded) {
                otherSectionAdded = true
            }
        }

        if (!addMoreSectionAdded) {
            addMoreSectionAdded = true
            listWithSection.add(ListItemFooter())
        }

        return listWithSection
    }

    fun resetReceivedPrescriptionData() {
        offset = 0
        otherSectionAdded = false
        addMoreSectionAdded = false
    }
}
