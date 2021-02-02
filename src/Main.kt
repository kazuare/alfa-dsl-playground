
fun main() {
    val attributes = Attributes(
        login = "user@mail.com",
        active = true
    )

    val policy = PolicySet(
        CombineAlgorithm.FirstApplicable,
        evaluateables = listOf(
            PolicySet(
                CombineAlgorithm.FirstApplicable, evaluateables = listOf(
                    Policy(
                        CombineAlgorithm.DenyUnlessPermit,
                        target = { active == true },
                        evaluateables = listOf(
                            Rule(
                                RuleType.Permit,
                                {
                                    login == "user@mail.com"
                                })
                        )
                    )
                )
            )
        )
    )

    print(policy.evaluate(attributes))
}