package org.intelehealth.app.model.address

/**
 * Represents a collection of provinces and cities.
 *
 * This data class is used to store lists of provinces and cities,
 * providing a convenient way to manage and access these related sets of data.
 *
 * @property provinces An [ArrayList] of [String] representing the names of provinces.
 * @property cities An [ArrayList] of [String] representing the names of cities.
 */
data class ProvincesAndCities(
    var provinces: ArrayList<String> = arrayListOf(),
    var cities: ArrayList<String> = arrayListOf()
)
