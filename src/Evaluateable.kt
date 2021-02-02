typealias ResponseEvaluator = Attributes.() -> Boolean?
typealias TargetEvaluator = Attributes.() -> Boolean

// unsolved questions
// how to do mandatory parameters

@DslMarker
annotation class AlfaDslEntity

@AlfaDslEntity
open class Evaluateable(var target: TargetEvaluator, var evaluateAction: ResponseEvaluator) {
    fun evaluate(attributes: Attributes): Boolean? {
        if (!target(attributes)) {
            return null
        }
        return evaluateAction(attributes)
    }
}

enum class RuleType {
    Permit,
    Deny;
}

class Rule(var ruleType: RuleType, target: TargetEvaluator? = null) :
    Evaluateable(target ?: { true }, { ruleType == RuleType.Permit })

abstract class PolicyOrSet(
    val name: String,
    algorithm: CombineAlgorithm,
    target: TargetEvaluator,
    evaluateables: List<Evaluateable>
) : Evaluateable(target, { algorithm.evaluate(evaluateables, this) })

class Policy(
    name: String,
    algorithm: CombineAlgorithm,
    target: TargetEvaluator = { true },
    val evaluateables: MutableList<Rule> = mutableListOf()
) : PolicyOrSet(name, algorithm, target, evaluateables) {

    fun rule(ruleType: RuleType, init: Rule.() -> Unit = {}) = evaluateables.add(Rule(ruleType).apply(init))
}

class PolicySet(
    name: String,
    algorithm: CombineAlgorithm,
    target: TargetEvaluator = { true },
    val evaluateables: MutableList<PolicyOrSet> = mutableListOf()
) : PolicyOrSet(name, algorithm, target, evaluateables) {

    fun policy(name: String, algorithm: CombineAlgorithm, init: Policy.() -> Unit = {}) = evaluateables.add(Policy(name, algorithm).apply(init))

    fun policySet(name: String, algorithm: CombineAlgorithm, init: PolicySet.() -> Unit = {}) = evaluateables.add(PolicySet(name, algorithm).apply(init))
}

fun policySet(name: String, algorithm: CombineAlgorithm, init: PolicySet.() -> Unit = {}) = PolicySet(name, algorithm).apply(init)
