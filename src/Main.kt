import CombineAlgorithm.*
import RuleType.*

fun main() {
    val attributes = Attributes(
        UserAttributes(
            login = "user@mail.com",
            active = true,
            function = "support"
        )
    )

    val root =
        policySet("Root", FirstApplicable) {
            policySet("NestedPolicy", FirstApplicable) {
                policy("NotApplicablePolicy", DenyUnlessPermit) {
                    target = { user.function != "support" }
                    rule(Deny)
                }

                policy("ActualPolicy", DenyUnlessPermit) {
                    target = { user.active == true }
                    rule(Permit) {
                        target = { user.login == "user@mail.com" }
                    }
                }
            }
        }

    print(root.evaluate(attributes))
}
