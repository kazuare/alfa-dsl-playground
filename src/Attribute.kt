
data class Attributes(val user: UserAttributes)

// this class should be extended by the users {
data class UserAttributes(val login: StringExpression = StringExpression(),
                      val function: StringExpression = StringExpression(),
                      val active: BooleanExpression = BooleanExpression())
// }
