import java.io.Serializable;

public class ZGL_Student implements Serializable {
    private String sid;
    private String sname;
    private String sgender;
    private int sage;
    private String scourses;
    //封装一下属性
    public ZGL_Student(String id, String name, String gender, int age, String courses) {
        this.sid = id;
        this.sname = name;
        this.sgender = gender;
        this.sage = age;
        this.scourses = courses;
    }

    public String getSid() {
            return sid;
    }
    public String getSname() {
        return sname;
    }
    public String getSgender() {
        return sgender;
    }
    public int getSage() {
        return sage;
    }
    public String getScourses() {
        return scourses;
    }

    // Getter 和 Setter（建议用 IDE 自动生成）
}
