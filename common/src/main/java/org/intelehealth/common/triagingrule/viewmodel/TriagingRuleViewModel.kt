package org.intelehealth.common.triagingrule.viewmodel

import android.content.res.AssetManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.triagingrule.model.rules.Formulas
import org.intelehealth.common.triagingrule.model.rules.ReferralRules
import org.intelehealth.common.triagingrule.model.rules.TriagingReferralRule
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import javax.inject.Inject
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

/**
 * Created by Vaghela Mithun R. on 28-01-2025 - 11:28.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class TriagingRuleViewModel @Inject constructor(private val assetManager: AssetManager) : BaseViewModel() {
    private val mutableRule: MutableLiveData<TriagingReferralRule> = MutableLiveData()
    val triagingRuleData = mutableRule.hide()

    private val mutableResult: MutableLiveData<ReferralRules> = MutableLiveData()
    val triagingResultData = mutableResult.hide()

    private val engine: ScriptEngine = ScriptEngineManager().getEngineByName("rhino")

    fun loadTriagingRuleData(fileName: String) {
        // Load triaging rule data from asset
        viewModelScope.launch {
            val consent = assetManager.open(fileName).bufferedReader().use { it.readText() }
            val data = Gson().fromJson(consent, TriagingReferralRule::class.java)
            mutableRule.postValue(data)
            buildTriagingRuleData(data)
        }
    }

    private fun buildTriagingRuleData(rule: TriagingReferralRule) {
        // Build triaging rule data
        viewModelScope.launch {
            launch { replaceExpressionKeyWithFieldsValues(rule) }
            launch {
                rule.formulas.forEach {
                    // Evaluate expression
                    val result = evaluateExpression(it.expression)
                    it.result = result.toInt()
                    withContext(Dispatchers.IO) {
                        // Evaluate conditions
                        evaluateConditions(rule.referralRules, it)
                    }
                }
            }
        }
    }

    private fun replaceExpressionKeyWithFieldsValues(rule: TriagingReferralRule) {
        // Replace key with values
        rule.fields.forEach {
            rule.formulas.forEach { formula ->
                if (formula.expression.contains(it.key)) {
                    formula.expression = formula.expression.replace(it.key, it.value.toString())
                }
            }
        }
    }

    private fun evaluateConditions(referralRules: ArrayList<ReferralRules>, it: Formulas) {
        // Evaluate conditions
        referralRules.forEach { referralRule ->
            referralRule.condition = referralRule.condition.replace("result", it.result.toString())
            val condition = engine.eval(referralRule.condition) as Boolean
            if (condition) {
                Timber.d { "Condition : ${referralRule.condition}" }
                Timber.d { "Referral Rule : $referralRule" }
                mutableResult.postValue(referralRule)
                return@forEach
            }
        }
    }

    private fun evaluateExpression(expression: String): Double {
        // Evaluate Expression
        val result = engine.eval(expression)
        Timber.d { "Result : $result" }
        return result as Double
    }
}