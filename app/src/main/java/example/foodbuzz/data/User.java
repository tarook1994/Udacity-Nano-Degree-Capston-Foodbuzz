package example.foodbuzz.data;

/**
 * Created by DELL on 4/23/2017.
 */

public class User {


    public String username;
    public String email;
    public String phone;
    public String photoUrl;
    public String historyLinks;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String photoUrl,String phone) {
        this.username = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.historyLinks = "null";
        this.phone = phone;
    }
}
