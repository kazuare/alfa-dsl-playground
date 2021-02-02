enum class CombineAlgorithm(val evaluate: (list: List<Evaluateable>, attributes: Attributes) -> Boolean?) {
    FirstApplicable({ list, attributes ->
        var result: Boolean? = null
        for (item in list) {
            result = item.evaluate(attributes)
            if (result != null) {
                break
            }
        }
        result
    }),
    PermitUnlessDeny({ list, attributes ->
        var result = true
        for (item in list) {
            if (item.evaluate(attributes) == false) {
                result = false
                break
            }
        }
        result
    }),
    DenyUnlessPermit({ list, attributes ->
        var result = false
        for (item in list) {
            if (item.evaluate(attributes) == true) {
                result = true
                break
            }
        }
        result
    })
}
