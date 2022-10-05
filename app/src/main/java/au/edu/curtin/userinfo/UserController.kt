package au.edu.curtin.userinfo

class UserController {

    private lateinit var userList: List<User>

    fun setUserList(data: List<User>) {
        this.userList = data
    }
}