typealias ResponseEvaluator = Attributes.() -> Decision
typealias TargetEvaluator = Attributes.() -> Boolean

// unsolved questions
// how to do mandatory parameters
// method overloading in order to avoid lambdas and use something analyzeable instead
// support decisions

enum class Decision {
    Permit,
    Deny,
    NotApplicable;
}

@DslMarker
annotation class AlfaDslEntity

@AlfaDslEntity
open class Evaluateable(var target: TargetEvaluator, var evaluateAction: ResponseEvaluator) {
    fun evaluate(attributes: Attributes): Decision {
        if (!target(attributes)) {
            return Decision.NotApplicable
        }
        return evaluateAction(attributes)
    }
}

enum class RuleType {
    Permit,
    Deny;
}

class Rule(var ruleType: RuleType, target: TargetEvaluator? = null) :
    Evaluateable(target ?: { true }, { if(ruleType == RuleType.Permit) Decision.Permit else Decision.Deny })

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
