package hr.ferit.bojankojcinovic.aplikacijaaa

data class Transactions(
    var name: String? = null,
    var amount: Double? = null,
    var note: String? = null,
    var userId: String? = "",
    var id: String = ""
)
