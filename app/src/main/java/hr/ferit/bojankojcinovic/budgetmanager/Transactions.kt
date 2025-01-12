package hr.ferit.bojankojcinovic.budgetmanager

data class Transactions(
    var type: String? = null,
    var name: String? = null,
    var amount: Double? = null,
    var note: String? = null,
    var userId: String? = null,
    var id: String = ""
)
