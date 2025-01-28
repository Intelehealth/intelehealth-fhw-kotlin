package org.intelehealth.app.model.address

data class ProvincesAndCities(
    var provinces: ArrayList<String> = arrayListOf(),
    var cities: ArrayList<String> = arrayListOf()
)