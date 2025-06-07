import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ZGLClient {

    //上传数据到服务器
    public static Object  sendObjectToServer(Object obj) //使用Object通用类型参数
    {   //因为这个方法中只是参数不同,这里可以用OBject统一参数,
        // 也能用重载的方法实现,基于我的少写代码设计原则,我就用Object这种办法
        //这里的Client是将客户端的数据通过流来传到服务器当中,这里在客户端和服务端之间插了一个Client类,
        //这个类的好处,一来可以封装数据
        //二来也能做一个效验,看传进来的参数是否符合规格,而不是直接发到服务器端,从而减轻服务器的压力,
        try (//端口8888.localhost,电脑的IP,也可写127.0.0.1环回地址

                Socket socket = new Socket("localhost", 8888);//连接到服务器
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());//发送数据到服务器

        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())//接收服务器返回的数据
        )
        {//执行try的语句
            out.writeObject(obj);//发送数据
            out.flush();
            //发送之后in.readObject();会进到阻塞状态,等待服务器传回的数据
            Object response = in.readObject();

            return response;//返回这个结果到客户端中
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //服务器传回信息给客户端

}
