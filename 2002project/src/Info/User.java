package Info;

public class User {
    protected String userID;
    protected String password;
    protected String role;
    //private boolean isPasswordChanged;

    //constructor
    public User(String userID, String password, String role){
        this.userID = userID;
        this.password = password;
        this.role = role;
    }

    //getter and setter method
    public String getUserID(){
        return userID;
    }

    public String getRole(){
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }


    //login
    public boolean Login(String inputPassword){
        return this.password.equals(inputPassword);
    }

    //to change password
    public boolean changePassword(String oldPassword, String newPassword) {
        if (Login(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    //to display user details
    public void displayUserInfo() {
        System.out.println("Info.User ID: " + userID);
        System.out.println("Role: " + role);
    }

}
