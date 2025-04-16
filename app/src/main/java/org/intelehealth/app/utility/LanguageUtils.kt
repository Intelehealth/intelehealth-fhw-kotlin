package org.intelehealth.app.utility

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import androidx.annotation.ArrayRes
import com.google.gson.Gson
import org.intelehealth.app.model.address.Block
import org.intelehealth.app.model.address.DistData
import org.intelehealth.app.model.address.GramPanchayat
import org.intelehealth.app.model.address.ProvincesAndCities
import org.intelehealth.app.model.address.StateData
import org.intelehealth.app.model.address.StateDistMaster
import org.intelehealth.app.model.address.Village
import java.util.Locale
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 26-06-2024 - 20:19.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Utility class for handling language-related operations and parsing JSON data
 * from assets.
 *
 * This class provides methods for:
 * - Determining the current locale's language.
 * - Parsing JSON data for states, districts, provinces, and cities.
 * - Retrieving specific data (StateData, DistData, Block, GramPanchayat, Village)
 *   based on names.
 * - Getting localized names for states, districts, blocks, gram panchayats, and villages.
 * - Handling language-specific resources.
 * - Getting local value from array.
 *
 * @property assetManager The [AssetManager] used to access JSON files in the assets directory.
 */
class LanguageUtils @Inject constructor(private val assetManager: AssetManager) {
    companion object {
        private const val STATE_DISTRICT_JSON = "state_district_tehsil.json"
        private const val PROVINCE_AND_CITIES_JSON = "province_and_cities.json"
    }

    /**
     * Gets the current locale's language code (e.g., "en", "hi").
     *
     * @return The current locale's language code as a [String].
     */
    fun getLocalLang(): String {
        return Locale.getDefault().language
    }

    /**
     * Retrieves a [StateData] object based on the given state name.
     *
     * @param state The name of the state to search for.
     * @return The corresponding [StateData] object, or `null` if not found.
     */
    fun getState(state: String): StateData? {
        return parseStatesJson().stateDataList.find { it.state == state }
    }

    /**
     * Retrieves a province name based on the given province name.
     *
     * @param province The name of the province to search for.
     * @return The province name as a [String], or `null` if not found.
     */
    fun getProvince(province: String): String? {
        return getProvincesAndCities().provinces.find { it == province }
    }

    /**
     * Retrieves a city name based on the given city name.
     *
     * @param city The name of the city to search for.
     * @return The city name as a [String], or `null` if not found.
     */
    fun getCity(city: String): String? {
        return getProvincesAndCities().cities.find { it == city }
    }

    /**
     * Retrieves the list of states.
     *
     * @return The list of [StateData] objects, or `null` if not found.
     */
    fun getStateList(): List<StateData>? {
        return parseStatesJson().stateDataList
    }

    /**
     * Parses the "state_district_tehsil.json" file from assets and returns a
     * [StateDistMaster] object.
     *
     * @return The parsed [StateDistMaster] object.
     */
    fun parseStatesJson(): StateDistMaster {
        val jsonObject = assetManager.open(STATE_DISTRICT_JSON).bufferedReader().use { it.readText() }
        return Gson().fromJson(
            jsonObject, StateDistMaster::class.java
        )
    }

    /**
     * Parses the "province_and_cities.json" file from assets and returns a
     * [ProvincesAndCities] object.
     *
     * This method is specifically for handling data related to Kazakhstan.
     *
     * @return The parsed [ProvincesAndCities] object.
     */
    fun getProvincesAndCities(): ProvincesAndCities {
        val jsonObject = assetManager.open(PROVINCE_AND_CITIES_JSON).bufferedReader().use { it.readText() }
        return Gson().fromJson(
            jsonObject, ProvincesAndCities::class.java
        )
    }

    /**
     * Retrieves a [DistData] object based on the given state and district names.
     *
     * @param state The [StateData] object to search within.
     * @param district The name of the district to search for.
     * @return The corresponding [DistData] object, or `null` if not found.
     */
    fun getDistrict(state: StateData?, district: String): DistData? {
        return state?.distDataList?.find { it.name == district }
    }

    /**
     * Retrieves a [Block] object based on the given district and block names.
     *
     * @param district The [DistData] object to search within.
     * @param block The name of the block to search for.
     * @return The corresponding [Block] object, or `null` if not found.
     */
    fun getBlock(district: DistData?, block: String?): Block? {
        return block?.let { return@let district?.blocks?.find { it.name == block } }
    }

    /**
     * Retrieves a [GramPanchayat] object based on the given block and gram panchayat names.
     *
     * @param block The [Block] object to search within.
     * @param gramPanchayat The name of the gram panchayat to search for.
     * @return The corresponding [GramPanchayat] object, or `null` if not found.
     */
    fun getGramPanchayat(block: Block?, gramPanchayat: String?): GramPanchayat? {
        return gramPanchayat?.let { return@let block?.gramPanchayats?.find { it.name == gramPanchayat } }
    }

    /**
     * Retrieves a [Village] object based on the given gram panchayat and village names.
     *
     * @param gramPanchayat The [GramPanchayat] object to search within.
     * @param village The name of the village to search for.
     * @return The corresponding [Village] object, or `null` if not found.
     */
    fun getVillage(gramPanchayat: GramPanchayat?, village: String?): Village? {
        return village?.let { return@let gramPanchayat?.villages?.find { it.name == village } }
    }

    /**
     * Gets the localized name of a state based on the current locale.
     *
     * @param state The [StateData] object.
     * @return The localized name of the state.
     */
    fun getStateLocal(state: StateData): String {
        if (getLocalLang() == "hi") return state.stateHindi
        return state.state
    }

    /**
     * Gets the localized name of a district based on the current locale.
     *
     * @param district The [DistData] object.
     * @return The localized name of the district.
     */
    fun getDistrictLocal(district: DistData): String {
        if (getLocalLang() == "hi") return district.nameHindi
        return district.name
    }

    /**
     * Gets the localized name of a block based on the current locale.
     *
     * @param block The [Block] object.
     * @return The localized name of the block.
     */
    fun getBlockLocal(block: Block): String {
        if (getLocalLang() == "hi") return block.nameHindi ?: block.name ?: ""
        return block.name ?: ""
    }

    /**
     * Gets the localized name of a gram panchayat based on the current locale.
     *
     * @param gramPanchayat The [GramPanchayat] object.
     * @return The localized name of the gram panchayat.
     */
    fun getGramPanchayatLocal(gramPanchayat: GramPanchayat): String {
        if (getLocalLang() == "hi") return gramPanchayat.nameHindi ?: gramPanchayat.name ?: ""
        return gramPanchayat.name ?: ""
    }

    /**
     * Gets the localized name of a village based on the current locale.
     *
     * @param village The [Village] object.
     * @return The localized name of the village.
     */
    fun getVillageLocal(village: Village): String {
        if (getLocalLang() == "hi") return village.nameHindi ?: village.name ?: ""
        return village.name ?: ""
    }

    /**
     * Gets a [Resources] object with a specific locale.
     *
     * This function allows you to retrieve resources that are localized
     * for a specific language, regardless of the device's current locale.
     * It creates a new [Configuration] with the specified locale and then
     * uses that configuration to create a new [Context] from which the
     * localized resources can be obtained.
     *
     * @param context The application context.
     * @param locale The desired locale code (e.g., "en", "hi").
     * @return A [Resources] object localized for the specified locale.
     */
    fun getSpecificLocalResource(context: Context, locale: String): Resources {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale(locale))
        return context.createConfigurationContext(configuration).resources
    }


    /**
     * Retrieves a localized string from an array resource based on a database string.
     *
     * This function is designed to handle cases where a string value is stored in a
     * database in English, but needs to be displayed in the user's current locale.
     * It checks if the current locale is English. If not, it attempts to find the
     * index of the English string in the English version of the array resource and
     * then uses that index to retrieve the corresponding localized string from the
     * array resource in the current locale.
     *
     * If the current locale is English or if the English string is not found in the
     * English array, it returns the original database string.
     *
     * @param context The application context.
     * @param dbString The string value from the database (in English).
     * @param arrayResId The resource ID of the string array.
     * @return The localized string from the array, or the original `dbString` if
     *         the locale is English or the string is not found.
     */
    fun getLocalValueFromArray(
        context: Context, dbString: String, @ArrayRes arrayResId: Int
    ): String {
        return if (Locale.getDefault().language == "en") {
            dbString
        } else {
            val array = context.resources.getStringArray(arrayResId)
            val index = getSpecificLocalResource(context, "en").getStringArray(arrayResId).indexOf(dbString)
            if (index >= 0) array[index] else ""
        }
    }
}