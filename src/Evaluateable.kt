
typealias ResponseEvaluator = Attributes.() -> Boolean?
typealias TargetEvaluator = Attributes.() -> Boolean

// unsolved questions
// how to do mandatory parameters

open class Evaluateable(private val target: TargetEvaluator, private val evaluateAction: ResponseEvaluator) {
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

class Rule(val ruleType: RuleType, target: TargetEvaluator) : Evaluateable(target, { ruleType == RuleType.Permit })

abstract class PolicyOrSet(
    algorithm: CombineAlgorithm,
    target: TargetEvaluator,
    evaluateables: List<Evaluateable>
) : Evaluateable(target, { algorithm.evaluate(evaluateables, this) })

class Policy(
    algorithm: CombineAlgorithm,
    target: TargetEvaluator = { true },
    evaluateables: List<Rule> = listOf()
) : PolicyOrSet(algorithm, target, evaluateables)

class PolicySet(
    algorithm: CombineAlgorithm,
    target: TargetEvaluator = { true },
    evaluateables: List<PolicyOrSet> = listOf()
) : PolicyOrSet(algorithm, target, evaluateables)
