package org.intelehealth.common.triagingrule.model.rules

import com.google.gson.annotations.SerializedName


data class TriagingReferralRule(

    @SerializedName("fields") var fields: ArrayList<Fields> = arrayListOf(),
    @SerializedName("formulas") var formulas: ArrayList<Formulas> = arrayListOf(),
    @SerializedName("referral_rules") var referralRules: ArrayList<ReferralRules> = arrayListOf()

)