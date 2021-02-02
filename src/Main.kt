import CombineAlgorithm.*
import RuleType.*

fun main() {
    val attributes = Attributes(
        login = "user@mail.com",
        active = true,
        function = "support"
    )

    val root = policySet("Root", FirstApplicable) {
        policySet("NestedPolicy", FirstApplicable) {
            policy("ActualPolicy", DenyUnlessPermit) {
                target = { active == true && function == "support" }
                rule(Permit) {
                    target = { login == "user@mail.com" }
                }
            }
        }
    }

    print(root.evaluate(attributes))
}
