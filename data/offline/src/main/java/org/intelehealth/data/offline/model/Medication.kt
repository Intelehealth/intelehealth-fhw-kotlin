package org.intelehealth.data.offline.model

import android.os.Parcelable
import com.github.ajalt.timberkt.Timber
import kotlinx.parcelize.Parcelize
import org.intelehealth.common.model.ListBindItem

/**
 * Created by Vaghela Mithun R. on 10-07-2025 - 13:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Parcelize
data class Medication(
    var name: String? = null,
    var dosage: String? = null,
    var frequency: String? = null,
    var timing: String? = null,
    var days: String? = null,
    var remarks: String? = null,
) : Parcelable, ListBindItem {
    override fun toString(): String {
        return "Medication(name=$name, dosage=$dosage, frequency=$frequency, days=$days, remarks=$remarks)"
    }

    fun isValid(): Boolean {
        return !name.isNullOrEmpty() && !dosage.isNullOrEmpty() && !frequency.isNullOrEmpty()
    }

    fun isEmpty(): Boolean {
        return name.isNullOrEmpty() && dosage.isNullOrEmpty() && frequency.isNullOrEmpty() &&
                days.isNullOrEmpty() && remarks.isNullOrEmpty()
    }

    fun isNotEmpty(): Boolean {
        return !isEmpty()
    }

    companion object {
        fun stringToMedication(value: String): Medication {
            val parts = value.split(":").map { it.trim() }
            return Medication(
                name = parts.getOrNull(0)?.trim(),
                dosage = parts.getOrNull(1)?.trim(),
                days = parts.getOrNull(2)?.trim(),
                timing = parts.getOrNull(3)?.trim(),
                remarks = parts.getOrNull(4)?.trim(),
                frequency = parts.getOrNull(5)?.trim(),
            ).apply {
                Timber.d { "Medication => $this" }
            }
        }

        fun dummyMedicationList(): List<Medication> {
            return listOf(
                Medication(
                    name = "Paracetamol",
                    dosage = "500mg",
                    frequency = "Twice a day",
                    days = "5 days",
                    remarks = "Take after food"
                ),
                Medication(
                    name = "Amoxicillin",
                    dosage = "250mg",
                    frequency = "Thrice a day",
                    days = "7 days",
                    remarks = "Complete the course"
                ),
                Medication(
                    name = "Ibuprofen",
                    dosage = "200mg",
                    frequency = "Once a day",
                    days = "3 days",
                    remarks = "For pain relief"
                )
            )
        }
    }
}
