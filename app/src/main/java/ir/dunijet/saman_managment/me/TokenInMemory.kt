package ir.dunijet.saman_managment.me

object TokenInMemory {

    var username: String? = null
        private set

    var token: String? = null
        private set

    fun refreshToken(username: String? , newToken: String? ) {
        TokenInMemory.username = username
        token = newToken
    }

}