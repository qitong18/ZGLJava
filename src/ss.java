import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class ss {
        public static void main(String[] args) {
            ZGLDataServer server = new ZGLDataServer();
            System.out.println("尝试连接数据库...");

            String name = "jk";
            boolean exists = server.checkUserExists(name);

            if (exists) {
                System.out.println("数据库连接成功，用户 " + name + " 存在！");
            } else {
                System.out.println("数据库连接成功，但用户 " + name + " 不存在！");
            }
        }
    }


