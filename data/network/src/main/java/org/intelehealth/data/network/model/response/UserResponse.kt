package org.intelehealth.data.network.model.response

import org.intelehealth.common.service.BaseResponse

/**
 * Created by Vaghela Mithun R. on 27-01-2025 - 18:30.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class UserResponse<R>(
    var success: Boolean = false,
) : BaseResponse<String, R>()