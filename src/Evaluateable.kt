
// unsolved questions
// how to do mandatory parameters

@DslMarker
annotation class AlfaDslEntity

enum class CombineAlgorithm {
    FirstApplicable,
    PermitUnlessDeny,
    DenyUnlessPermit
}


@AlfaDslEntity
open class Evaluateable(var target: BooleanExpression) {
    val user = UserAttributes(
        StringExpression(),
        StringExpression(),
        BooleanExpression())
}

enum class RuleType {
    Permit,
    Deny;
}

class Rule(var ruleType: RuleType, target: BooleanExpression? = null) :
    Evaluateable(target ?: True)

abstract class PolicyOrSet(
    val name: String,
    algorithm: CombineAlgorithm,
    target: BooleanExpression
) : Evaluateable(target)

class Policy(
    name: String,
    algorithm: CombineAlgorithm,
    target: BooleanExpression = True,
    val evaluateables: MutableList<Rule> = mutableListOf()
) : PolicyOrSet(name, algorithm, target) {

    fun rule(ruleType: RuleType, init: Rule.() -> Unit = {}) = evaluateables.add(Rule(ruleType).apply(init))
}

class PolicySet(
    name: String,
    algorithm: CombineAlgorithm,
    target: BooleanExpression = True,
    val evaluateables: MutableList<PolicyOrSet> = mutableListOf()
) : PolicyOrSet(name, algorithm, target) {

    fun policy(name: String, algorithm: CombineAlgorithm, init: Policy.() -> Unit = {}) = evaluateables.add(Policy(name, algorithm).apply(init))

    fun policySet(name: String, algorithm: CombineAlgorithm, init: PolicySet.() -> Unit = {}) = evaluateables.add(PolicySet(name, algorithm).apply(init))
}

fun policySet(name: String, algorithm: CombineAlgorithm, init: PolicySet.() -> Unit = {}) = PolicySet(name, algorithm).apply(init)
