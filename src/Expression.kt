
sealed class Expression

class BooleanExpression: Expression() {
    operator fun not() = BooleanExpression()
    infix fun equals(x: Boolean) = BooleanExpression()
}

class StringExpression: Expression() {
    infix fun equals(x: StringExpression) = BooleanExpression()
    infix fun equals(x: String) = BooleanExpression()
}

val True = BooleanExpression()