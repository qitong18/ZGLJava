import org.omg.CORBA.PUBLIC_MEMBER;

public class ZGL_Register extends  LandRFather{

    public ZGL_Register(String username, String    password) {
        super(username, password);
    }
    public String getUid() {
        return username;
    }

    public String getUpassword() {
        return password;
    }
}
