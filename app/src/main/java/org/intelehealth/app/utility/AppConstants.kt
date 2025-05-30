package org.intelehealth.app.utility

import android.content.Context
import org.intelehealth.app.BuildConfig

/**
 * Created by Vaghela Mithun R. on 31-12-2024 - 17:08.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

const val KEY_FORCE_UPDATE_VERSION_CODE = "force_update_version_code"
const val JSON_FOLDER = "Engines"
const val JSON_FOLDER_UPDATE = "Engines_Update"
const val CONFIG_FILE_NAME = "config.json"
const val ASSET_FILE_CONSENT = "consent.json"
const val TRIAGING_RULE_FILE_NAME = "triaging_referral_rules.json"
const val KEY_RESULTS = "results"
const val IND_COUNTRY_CODE = "91"
// The length of the mobile number for India.
const val IND_MOBILE_LEN = 10
// The length of the mobile number for other countries.
const val OTHER_MOBILE_LEN = 15
const val PWD_MIN_LENGTH = 5
const val MAX_NAME_LENGTH = 25
const val INTELEHEALTH_WEB_LINK = "https://intelehealth.org/"
const val PERSON_IMAGE_BASE_PATH = "${BuildConfig.SERVER_URL}/openmrs/ws/rest/v1/personimage/"
const val POSTAL_CODE_LEN = 6

/**
 * The name of the folder within the application's assets directory that
 * contains JSON files for the triaging engine.
 *
 * This constant is used to specify the location of JSON data files that are
 * bundled with the application. These files might contain configuration data,
 * default settings, or other structured information used by the triaging engine.
 */
fun getAppPlayStoreUrl(context: Context): String {
    return "https://play.google.com/store/apps/details?id=" + context.applicationContext.packageName
}

/**
 * The name of the folder within the application's assets directory that
 * contains JSON files for the triaging engine.
 *
 * This constant is used to specify the location of JSON data files that are
 * bundled with the application. These files might contain configuration data,
 * default settings, or other structured information used by the triaging engine.
 */
fun getAppMarketUrl(context: Context): String {
    return "market://details?id=" + context.applicationContext.packageName
}
