import java.io.Serializable;

public class LandRFather implements Serializable {
    protected String username;
    protected String password;

    public LandRFather(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // getter 和 setter 略
}
