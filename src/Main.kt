import CombineAlgorithm.*
import RuleType.*

fun main() {
    policySet("Root", FirstApplicable) {
        policySet("NestedPolicy", FirstApplicable) {
            policy("NotApplicablePolicy", DenyUnlessPermit) {
                target = user.function equals "support"
                rule(Deny)
            }

            policy("ActualPolicy", DenyUnlessPermit) {
                target = user.active equals false
                rule(Permit) {
                    target = user.login equals "user@mail.com"
                }
            }
        }
    }
}
