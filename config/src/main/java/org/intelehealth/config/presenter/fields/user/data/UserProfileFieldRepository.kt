package org.intelehealth.config.presenter.fields.user.data

import org.intelehealth.config.presenter.fields.user.UserConfigKey
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 27-03-2025 - 17:35.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class UserProfileFieldRepository @Inject constructor() {
    fun getUserProfileFieldConfig() = UserConfigKey.buildUserInfoConfig()
}
