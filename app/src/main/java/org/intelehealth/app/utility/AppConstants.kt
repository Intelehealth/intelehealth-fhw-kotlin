package org.intelehealth.app.utility

import android.content.Context

/**
 * Created by Vaghela Mithun R. on 31-12-2024 - 17:08.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

const val KEY_FORCE_UPDATE_VERSION_CODE = "force_update_version_code"
const val JSON_FOLDER = "Engines"
const val JSON_FOLDER_Update = "Engines_Update"
const val CONFIG_FILE_NAME = "config.json"
const val ASSET_FILE_CONSENT = "consent.json"
const val KEY_RESULTS = "results"
const val IND_COUNTRY_CODE = "91"


fun getAppPlayStoreUrl(context: Context): String {
    return "https://play.google.com/store/apps/details?id=" + context.applicationContext.packageName
}

fun getAppMarketUrl(context: Context): String {
    return "market://details?id=" + context.applicationContext.packageName
}