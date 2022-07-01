package ir.dunijet.saman_managment.me


data class eee(
    val messages: List<Message>
) {
    data class Message(
        val name: String,
        val password: String,
        val username: String
    )
}