package org.intelehealth.data.provider.notification

import org.intelehealth.common.data.BaseDataSource
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.data.network.KEY_PAGE
import org.intelehealth.data.network.KEY_SIZE

import org.intelehealth.data.network.KEY_USERID
import org.intelehealth.data.network.RestClient
import javax.inject.Inject

class NotificationDataSource @Inject constructor(
    private val restClient: RestClient, networkHelper: NetworkHelper
) : BaseDataSource(networkHelper = networkHelper) {

    fun fetchAllnotification(bearerToken: String, userId: String, page: String, size: String) = getResult {
        restClient.fetchAllNotifications(bearerToken,
            hashMapOf(
                KEY_USERID to userId, KEY_PAGE to page, KEY_SIZE to size
            )
        )
    }
}