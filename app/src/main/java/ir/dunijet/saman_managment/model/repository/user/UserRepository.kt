package ir.dunijet.saman_managment.model.repository.user

interface UserRepository {

    // online
    fun signUp(name: String, username: String, password: String): String
    fun signIn(username: String, password: String): String

    // offline
    fun signOut()
    fun loadToken()

    fun saveToken(newToken: String)
    fun getToken(): String?

    fun saveUserName(username: String)
    fun getUserName(): String?

}