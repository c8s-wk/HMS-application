package Info;

public class User {
    protected String userID;
    protected String password;
    protected String role;

    // Constructor
    public User(String userID, String password, String role) {
        this.userID = userID;
        this.password = password;
        this.role = role;
    }

    // Getter for userID
    public String getUserID() {
        return userID;
    }

    // Getter for role
    public String getRole() {
        return role;
    }

    // Getter for password (this is the method you are missing)
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Setter for role
    public void setRole(String role) {
        this.role = role;
    }

    // Login method
    public boolean Login(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Change password method
    public boolean changePassword(String oldPassword, String newPassword) {
        if (Login(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    // Display user information
    public void displayUserInfo() {
        System.out.println("User ID: " + userID);
        System.out.println("Role: " + role);
    }
}
