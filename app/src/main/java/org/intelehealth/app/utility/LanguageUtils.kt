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
class LanguageUtils @Inject constructor(private val assetManager: AssetManager) {
    companion object {
        private const val STATE_DISTRICT_JSON = "state_district_tehsil.json"
        private const val PROVINCE_AND_CITIES_JSON = "province_and_cities.json"
    }

    fun getLocalLang(): String {
        return Locale.getDefault().language
    }


    fun getState(state: String): StateData? {
        return parseStatesJson().stateDataList.find { it.state == state }
    }


    fun getProvince(province: String): String? {
        return getProvincesAndCities().provinces.find { it == province }
    }


    fun getCity(city: String): String? {
        return getProvincesAndCities().cities.find { it == city }
    }


    fun getStateList(): List<StateData>? {
        return parseStatesJson().stateDataList
    }


    fun parseStatesJson(): StateDistMaster {
        val jsonObject = assetManager.open(STATE_DISTRICT_JSON).bufferedReader().use { it.readText() }
        return Gson().fromJson(
            jsonObject, StateDistMaster::class.java
        )
    }

    /**
     * specially for Kazakhstan
     */

    fun getProvincesAndCities(): ProvincesAndCities {
        val jsonObject = assetManager.open(PROVINCE_AND_CITIES_JSON).bufferedReader().use { it.readText() }
        return Gson().fromJson(
            jsonObject, ProvincesAndCities::class.java
        )
    }


    fun getDistrict(state: StateData?, district: String): DistData? {
        return state?.distDataList?.find { it.name == district }
    }


    fun getBlock(district: DistData?, block: String?): Block? {
        return block?.let { return@let district?.blocks?.find { it.name == block } }
    }


    fun getGramPanchayat(block: Block?, gramPanchayat: String?): GramPanchayat? {
        return gramPanchayat?.let { return@let block?.gramPanchayats?.find { it.name == gramPanchayat } }
    }


    fun getVillage(gramPanchayat: GramPanchayat?, village: String?): Village? {
        return village?.let { return@let gramPanchayat?.villages?.find { it.name == village } }
    }


    fun getStateLocal(state: StateData): String {
        if (getLocalLang().equals("hi")) return state.stateHindi
        return state.state
    }


    fun getDistrictLocal(district: DistData): String {
        if (getLocalLang().equals("hi")) return district.nameHindi
        return district.name
    }


    fun getBlockLocal(block: Block): String {
        if (getLocalLang().equals("hi")) return block.nameHindi ?: block.name ?: ""
        return block.name ?: ""
    }


    fun getGramPanchayatLocal(gramPanchayat: GramPanchayat): String {
        if (getLocalLang().equals("hi")) return gramPanchayat.nameHindi ?: gramPanchayat.name ?: ""
        return gramPanchayat.name ?: ""
    }


    fun getVillageLocal(village: Village): String {
        if (getLocalLang().equals("hi")) return village.nameHindi ?: village.name ?: ""
        return village.name ?: ""
    }


    fun getSpecificLocalResource(context: Context, locale: String): Resources {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale(locale))
        return context.createConfigurationContext(configuration).resources
    }


    fun getLocalValueFromArray(
        context: Context, dbString: String, @ArrayRes arrayResId: Int
    ): String {
        return if (Locale.getDefault().language.equals("en").not()) {
            val array = context.resources.getStringArray(arrayResId)
            val index = getSpecificLocalResource(context, "en").getStringArray(arrayResId).indexOf(dbString)
            return if (index > 0) array[index]
            else ""
        } else dbString
    }
}