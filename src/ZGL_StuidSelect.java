import java.io.Serializable;
public class ZGL_StuidSelect implements Serializable {
    private String sid;

    public ZGL_StuidSelect(String Stuid) {
        this.sid = Stuid;
    }

    public String getSid() {
        return sid;
    }
}
