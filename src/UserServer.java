import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

public class UserServer {
        private static final int PORT = 8888; // 服务器监听的端口号
        //private static final HashSet<String> ZGL_JavaStudentManager =new HashSet<>(); // 模拟的数据库，用于存储用户名
        private static final ZGLDataServer dataServer = new ZGLDataServer();
       public static void main(String[] args) {
           try {
                ServerSocket serverSocket = new ServerSocket(PORT); // 创建服务端 socket，监听指定端口
               System.out.println("服务器已启动，等待客户端连接...");
                while (true) {
                    Socket clientSocket = serverSocket.accept(); // 阻塞等待客户端连接
                    System.out.println("有客户端连接进来：" + clientSocket.getInetAddress());

                    // 每当有客户端连接，就开一个新线程处理，避免阻塞其他连接
                    new Thread(() -> ClientSocketStart(clientSocket)).start();//每当有客户端库连接,就执行一个新线程
                }
           } catch (IOException e) {
                e.printStackTrace(); // 捕获服务器异常
           }
        }

        // 处理每个客户端连接的逻辑
        private static void ClientSocketStart(Socket socket) {
            try (
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());   // 用于接收客户端发来的对象
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()) // 用于向客户端发送结果
            ) {
                Object obj = in.readObject(); // 从客户端接收一个对象（可能是用户或学生）,在未接收到会处于阻塞状态

                // 判断是否是用户对象//注册类型
                if (obj instanceof ZGL_Register ) {//这里需要用instanceof判定传进来的类型是否Users
                    ZGL_Register Regis = (ZGL_Register) obj;//强转
                    String Regisname = Regis.getUid();;//获取用户名
                    String Regispassword=Regis.getUpassword().trim();;//获取用户密码
                    System.out.println("接收到注册用户：" + Regisname);
                    if (dataServer.checkUserExists(Regisname)) {//如果存在就是返回真,返回真就是用户存在,
                        out.writeObject(0); // 用户已存在//发给客户端去处理,

                        System.out.println("ZGL服务器信息:用户名已存在");
                    } else {
                        //如果不存在,则执行数据库访问器中的添加用户信息语句
                        dataServer.addUser(Regisname,Regispassword);
                        System.out.println("成功添加到数据库");
                        out.writeObject(1); // 把注册成功信息,返回给客户端

                        out.flush();//刷新发送
                        System.out.println("ZGL服务器信息:注册成功，用户名为"+Regisname+"密码为:"+Regispassword);//服务器终端显示注册成功
                    }
                }
                //登录类型

               else   if (obj instanceof ZGL_Users ) {//这里需要用instanceof判定传进来的类型是否Users还是register
                    ZGL_Users users = (ZGL_Users) obj;//强转
                    String username = users.getUid();//获取用户名
                    String userpassword=users.getUpassword();//获取用户密码
                    System.out.println("接收到登录消息用户：" + username);
                    if (dataServer.checkUserExists(username)) {
                        if(dataServer.getPasswordByUsername(username).equals(userpassword)) {
                            out.writeObject(1); // 用户已存在//发给客户端去处理,
                            out.flush();//刷新发送
                            System.out.println("ZGL服务器信息:登录成功.用户:"+username+"密码:"+userpassword);
                        }
                        else {
                            out.writeObject(0);
                            System.out.println("ZGL服务器信息:用户存在,密码不正确");
                        }

                    } else {
                        //如果不存在,则执行数据库访问器中的添加用户信息语句
                        dataServer.addUser(username,userpassword);
                        out.writeObject(2); // 把注册成功信息,返回给客户端

                        System.out.println("ZGL服务器信息:用户不存在，");//服务器终端显示注册成功
                    }
                }
                else if(obj instanceof ZGL_StuidSelect) {
                    ZGL_StuidSelect stuid = (ZGL_StuidSelect) obj;
                    String Studentid=stuid.getSid();//
                    System.out.println("接收到查询学生的ID");
                    String[] result= dataServer.selectStudent(Studentid);
                    if (result[0]!=null) {
                        out.writeObject(result);//返回一个数组
                        out.flush();
                        System.out.println("ZGL服务器信息:学号存在,查询成功");
                    }
                    else {
                        out.writeObject(null);
                        out.flush();
                        System.out.println("ZGL服务器信息:查询失败,学号不存在");

                    }
                }
                // 如果以后支持 Student 类，也可以在这里加处理逻辑
                //这里是是否要添加学生信息
                else if (obj instanceof ZGL_Student ) {//这里需要用instanceof判定传进来的类型是否student

                    ZGL_Student student = (ZGL_Student) obj;//强转类型
                    String Studentid=student.getSid();//再获取学生的学号
                    System.out.println("接收到添加学生的ID");
                    if (dataServer.studentExists(Studentid)) {//如果数据访问库中的studentExists
                        //这里应该是学生表.上面注册那里是用户表
                        out.writeObject(0);//返回给客户端的信息
                        out.flush();//刷新发送
                        System.out.println("ZGL服务器信息:学号已存在");
                    }
                    else {
                        boolean success = dataServer.addStudent(
                                student.getSid(),
                                student.getSname(),
                                student.getSgender(),
                                student.getSage(),
                                student.getScourses()
                        ); // 插入新学生

                        if (success) {
                            out.writeObject(1);
                            out.flush();//刷新发送
                            System.out.println("ZGL服务器信息:添加"+student.getSid()+"成功");
                        }
                        else {
                            out.writeObject(0);
                            out.flush();//刷新发送
                            System.out.println("ZGL服务器信息: 添加 " + student.getSid() + " 失败");
                        }

                    }


                }


            } catch (Exception e) {
                e.printStackTrace(); // 捕获异常（比如对象解析失败）
            }
        }

//
        }




