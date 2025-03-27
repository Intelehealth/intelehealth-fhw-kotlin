package org.intelehealth.config.presenter.fields.user

import org.intelehealth.app.ui.patient.config.UserInfoConfig
import org.intelehealth.config.room.entity.PatientRegistrationFields

/**
 * Created by Vaghela Mithun R. on 27-03-2025 - 13:56.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
object UserConfigKey {
    //PERSONAL
    const val PROFILE_PHOTO = "u_profile_photo"
    const val USER_NAME = "u_first_name"
    const val FIRST_NAME = "u_first_name"
    const val MIDDLE_NAME = "u_middle_name"
    const val LAST_NAME = "u_last_name"
    const val GENDER = "u_gender"
    const val DOB = "u_date_of_birth"
    const val AGE = "u_age"
    const val PHONE_NUM = "u_phone_number"
    const val EMAIL = "u_guardian_name"

    @JvmStatic
    fun buildUserInfoConfig(): UserInfoConfig = UserInfoConfig().apply {
        generateUserProfileFiledConfig().forEach {
            when (it.idKey) {
                FIRST_NAME -> firstName = it
                MIDDLE_NAME -> middleName = it
                LAST_NAME -> lastName = it
                DOB -> dob = it
                AGE -> age = it
                PHONE_NUM -> phone = it
                PROFILE_PHOTO -> profilePic = it
                GENDER -> gender = it
                EMAIL -> email = it
            }
        }
    }

    @JvmStatic
    private fun generateUserProfileFiledConfig() = arrayListOf<PatientRegistrationFields>().apply {
        add(PatientRegistrationFields(1, "User", "", FIRST_NAME, true, false, true))
        add(PatientRegistrationFields(1, "User", "", MIDDLE_NAME, true, false, true))
        add(PatientRegistrationFields(1, "User", "", LAST_NAME, true, false, true))
        add(PatientRegistrationFields(1, "User", "", DOB, true, false, true))
        add(PatientRegistrationFields(1, "User", "", AGE, true, false, true))
        add(PatientRegistrationFields(1, "User", "", PROFILE_PHOTO, false, true, true))
        add(PatientRegistrationFields(1, "User", "", PHONE_NUM, false, true, true))
        add(PatientRegistrationFields(1, "User", "", GENDER, false, false, true))
        add(PatientRegistrationFields(1, "User", "", EMAIL, false, true, true))
    }
}
