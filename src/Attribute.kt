
data class Attributes(val user: UserAttributes)

// this class should be extended by the users {
data class UserAttributes(val login: String? = null,
                      val function: String? = null,
                      val active: Boolean? = null)
// }
