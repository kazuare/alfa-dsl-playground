import Decision.*

enum class CombineAlgorithm(val evaluate: (evaluateables: List<Evaluateable>, attributes: Attributes) -> Decision) {
    FirstApplicable({ evaluateables, attributes -> combineBase(evaluateables, attributes, NotApplicable, listOf(Deny, Permit)) }),
    PermitUnlessDeny({ evaluateables, attributes -> combineBase(evaluateables, attributes, Permit, listOf(Deny)) }),
    DenyUnlessPermit({ evaluateables, attributes -> combineBase(evaluateables, attributes, Deny, listOf(Permit)) })
}

fun combineBase(evaluateables: List<Evaluateable>, attributes: Attributes, initialDecision: Decision, stopDecisions: List<Decision>): Decision {
    for (item in evaluateables) {
        val result = item.evaluate(attributes)
        if (stopDecisions.contains(result)) {
            return result
        }
    }
    return initialDecision
}