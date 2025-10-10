package org.intelehealth.common.state

sealed class ListItem {
    data class Header(val title: String) : ListItem()
    data class Notification<T>(val data: T?) : ListItem()
}