import java.nio.channels.ClosedSelectorException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZGLDataServer {
    //连接数据库的通用语句,跟着网上的访着写就好,第一个是数据库的端口号和连接数据库的名字
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ZGL_JavaStudentManager;" +
            "encrypt=false;";
    private static final String DataUser = "sa";  // 修改为你的数据库用户名
    private static final String Datapassword = "123456";  // 修改为你的数据库密码
    private  Connection conn;
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 加载驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 用户相关操作示例
    public boolean checkUserExists(String uid) {//获取用户的id
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";//数据库语句查询含有uid用户名的记录,
        //这里是查询COUNT(*),如果有该用户,会返回1,无则返回0,因为用户名是主键,只能是1
        try (Connection conn = DriverManager.getConnection(URL, DataUser, Datapassword);//连接数据库
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, uid);//表示第一个占位符,传进uid,既用户名
            ResultSet rs = ps.executeQuery();//返回结果传给rs

            if (rs.next()) {//如果有用户,则为真,返回宠护
                int count = rs.getInt(1);
                return count>0;//因为count是1大于0,返回真,把结果真返回到服务器,
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;//若没有,则为假;
    }

    public boolean addUser(String uid, String password) {

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, DataUser, Datapassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, uid);
            ps.setString(2, password);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            //无论如何都会执行finally
            try
            {
                if (conn != null)
                {
                    conn.close();//关闭数据库,另外try再添加个防止中断程序
                    System.out.println("数据库关闭成功");
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }


        }
        return false;
    }

    // 学生相关操作示例
    public boolean addStudent(String sid, String name, String gender, int age,String courses) {
        String sql = "INSERT INTO students (student_id, name, gender, age,class_name) VALUES (?, ?, ?, ?,?)";
        try (Connection conn = DriverManager.getConnection(URL, DataUser, Datapassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sid);
            ps.setString(2, name);
            ps.setString(3, gender);
            ps.setInt(4, age);
            ps.setString(5, courses);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            //无论如何都会执行finally
            try
            {
                if (conn != null)
                {
                    conn.close();//关闭数据库,另外try再添加个防止中断程序
                    System.out.println("数据库关闭成功");
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }


        }
        return false;
    }
    //这里是之前用的返回的数组
    public  String[] selectStudent(String sid)
    {
        String sql = "SELECT * FROM students WHERE student_id=?";
        String[] str=new String[5];
        try (Connection conn = DriverManager.getConnection(URL, DataUser, Datapassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sid);
         ResultSet rs = ps.executeQuery();//返回的结果
            if (rs.next()) {
                String id = rs.getString("student_id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                int age = rs.getInt("age");
                String  stuage= Integer.toString(age);
                String classname = rs.getString("class_name");

                 str[0]=id;
                str[1]=name;
                 str[2]=gender;
                 str[3]=stuage;
                str[4]=classname;

            }


       } catch (SQLException e) {
            e.printStackTrace();
        }

        return str; // 如果出错或查不到，认为不存在
    }

    //
    public boolean studentExists(String sid) {
        String sql = "SELECT COUNT(*) FROM students WHERE student_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, DataUser, Datapassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sid);
            ResultSet rs = ps.executeQuery();//返回的结果

            if (rs.next()) {
                int count = rs.getInt(1);
                return count>0; // 如果数量大于 0，说明学号存在
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            //无论如何都会执行finally
            try
            {
                if (conn != null)
                {
                    conn.close();//关闭数据库,另外try再添加个防止中断程序
                    System.out.println("数据库关闭成功");
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }


        }
        return false; // 如果出错或查不到，认为不存在
    }

    // 查询某用户名对应的密码
    public String getPasswordByUsername(String username) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (
                Connection conn = DriverManager.getConnection(URL, DataUser, Datapassword);
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 用户不存在或查询失败
    }

    public List<String> SelectStu(String sid) {
        List<String> result = new ArrayList<>();//创建一个返回结果的列表,里面存的都是字符串元素
        String sql = "SELECT * FROM students WHERE sid = ?";
        try (Connection conn = DriverManager.getConnection(URL, DataUser, Datapassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String info = "学号：" + rs.getString("sid") +
                        " 姓名：" + rs.getString("sname") +
                        " 性别：" + rs.getString("sgender") +
                        " 年龄：" + rs.getInt("sage")+
                        "课程名称"+rs.getString("class_name");
                result.add(info);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }


}
