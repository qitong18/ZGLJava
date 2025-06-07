import java.io.PipedReader;
import java.io.Serializable;
public class ZGL_Users extends LandRFather{


        public ZGL_Users(String username, String password) {
            super(username, password);
        }


    //封装数据


    public String getUid() {
        return username;
    }

    public String getUpassword() {
        return password;
    }

}
