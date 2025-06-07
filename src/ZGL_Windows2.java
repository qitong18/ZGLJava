import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.bcel.internal.generic.PUSH;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//我学了一天JFrame我才知道有GUI来搭建窗体组件这个功能,我人嘛了,
//用JFrame除了麻烦,没别的了,下面的代码量就能看出 JFrame编写窗口程序的麻烦和缺点,强烈不推荐
//在此过程中结合书本和学习网上的编写的窗口程序,然后辛辛苦苦写出来的,
public class ZGL_Windows2 extends JFrame {
    //主函数入口
    public static void main(String[] args) {
        new ZGL_Windows2();//调用构造函数
    }
    public  String name;
    private JLabel currentUserLabel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    public ZGL_Windows2() {
        //构造函数,保证实例化时能通过构造函数初始化
        setTitle("学生班级管理系统第二个客户端");//标题
        setSize(600, 400);//设置的窗口大小
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 添加三个界面
        mainPanel.add(createLoginPanel(), "login");//登录面板
        mainPanel.add(createRegisterPanel(), "register");//注册面板
        mainPanel.add(createMainPanel(), "main");//主界面面板,这个主界面包含两个子界面
        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    //创建登录面板
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("用户名:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("密码:");
        JPasswordField passField = new JPasswordField(15);
        JButton loginBtn = new JButton("登录");
        loginBtn.addActionListener(e -> {
            if(userField.getText().isEmpty() || passField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入完整");
                return;
            }
            String username = userField.getText();
            String password = new String(passField.getPassword());
            //传给用户类
            ZGL_Users userData = new ZGL_Users(username, password);
            Object Loginresult= ZGLClient.sendObjectToServer(userData);//再上传到服务器
            //后续要在这里添加数据库到服务器返回的结果
            //下面这条判断语句要改成是否与返回来的用户是否存在,而且密码正确
            if (Loginresult != null)
            {
                int result = (int) Loginresult;
                System.out.println("结果数值为"+result);
                if (result == 1) {
                    JOptionPane.showMessageDialog(this, "登录成功！");
                    name=username;
                    currentUserLabel.setText("当前用户：" + name);
                    cardLayout.show(mainPanel, "main"); // 切换到主界面
                }
                else if (result == 2) {
                    JOptionPane.showMessageDialog(this, "用户不存在");
                }
                else if (result == 0) {
                    JOptionPane.showMessageDialog(this, "密码错误");
                }
                else {
                    JOptionPane.showMessageDialog(this, "未知错误1");
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this, "未知错误2");
            }

        });

        JButton toRegisterBtn = new JButton("注册");

        // 第一行 用户名
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(userField, gbc);

        // 第二行 密码
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(passField, gbc);

        // 第三行 按钮
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel();
        btnPanel.add(loginBtn);
        btnPanel.add(toRegisterBtn);
        panel.add(btnPanel, gbc);

        // 注册按钮切换界面
        toRegisterBtn.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        return panel;
    }

    //创建注册面板
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("用户名:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("密码:");
        JPasswordField passField = new JPasswordField(15);
        JLabel confirmLabel = new JLabel("确认密码:");
        JPasswordField confirmField = new JPasswordField(15);

        JButton registerBtn = new JButton("注册");
        JButton backBtn = new JButton("返回");

        // 布局
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(confirmLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(confirmField, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel();
        btnPanel.add(registerBtn);
        btnPanel.add(backBtn);
        panel.add(btnPanel, gbc);

        // 注册按钮逻辑
        registerBtn.addActionListener(e -> {
            String Rusername = userField.getText();
            String pwd1 = new String(passField.getPassword());
            String pwd2 = new String(confirmField.getPassword());
            if (userField.getText().isEmpty() || passField.getText().isEmpty()  || confirmField.getText().isEmpty() )
            {
                JOptionPane.showMessageDialog(this, "请填完！");
                return;
            }
            if (!pwd1.equals(pwd2)) {

                JOptionPane.showMessageDialog(this, "两次密码不一致！");
                return;
            }

            else {
                ZGL_Register userData = new ZGL_Register(Rusername, pwd2);
                Object Registerresult =ZGLClient.sendObjectToServer(userData);//再上传到服务器
                if (Registerresult != null)
                {
                    int result = (int) Registerresult;
                    System.out.println("结果数值为"+result);
                    if (result == 1) {
                        JOptionPane.showMessageDialog(this, "注册成功");

                    } else if (result == 0) {
                        JOptionPane.showMessageDialog(this, "用户已存在,请更换用户名");
                    } else {
                        JOptionPane.showMessageDialog(this, "未知错误,请重新尝试");
                    }
                }

            }
        });

        // 返回按钮 → 切换回登录界面
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        return panel;
    }
    //创建主界面面板
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 顶部显示当前用户
        currentUserLabel = new JLabel("当前用户："+name, SwingConstants.CENTER);
        currentUserLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        mainPanel.add(currentUserLabel, BorderLayout.NORTH);

        // 创建选项卡面板
        JTabbedPane tabPane = new JTabbedPane();

        // 添加两个子页面
        tabPane.addTab("查询学生", createFirstPanel());
        tabPane.addTab("添加学生", createSecondPanel());

        mainPanel.add(tabPane, BorderLayout.CENTER);

        return mainPanel;
    }
    //第一个子界面
    private JPanel createFirstPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel idLabel = new JLabel("学号：");
        JTextField idField = new JTextField(20);
        inputPanel.add(idLabel);
        inputPanel.add(idField);

        JButton queryButton = new JButton("查询学生信息");
        queryButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        queryButton.addActionListener(e -> {//
            // 查询按钮的事件,
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请添加学号");
                return;
            }
            String id = idField.getText();
            ZGL_StuidSelect userData = new ZGL_StuidSelect(id);
            Object Selectresult =ZGLClient.sendObjectToServer(userData);
            if (Selectresult != null)
            {
                String[] result = (String[]) Selectresult;
                resultArea.setText("    学号:"+result[0]+
                        "    姓名："+result[1]+"    性别："+
                        result[2]+
                        "    年龄："+result[3]+"    课程："+result[4]);
            }
            else {
                JOptionPane.showMessageDialog(this, "学号不存在");
                return;
            }


            /*String id = idField.getText().trim();

            if (id.equals("202401")) {
                resultArea.setText("学号：202401    姓名：张三    性别：男    年龄：20    课程：Java, 数据库, 算法");
            } else {
                resultArea.setText("未找到该学号对应的学生信息。");
            }*/
            //String Sid=idField.getText();
            //ZGL_Student
            //查询学生事件
        });

        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(queryButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(scrollPane);

        return panel;
    }
    //第二个子界面
    private JPanel createSecondPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField genderField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField classnameField = new JTextField(20);

        panel.add(new JLabel("学号："));
        panel.add(idField);
        panel.add(Box.createVerticalStrut(5));

        panel.add(new JLabel("姓名："));
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(5));

        panel.add(new JLabel("性别："));
        panel.add(genderField);
        panel.add(Box.createVerticalStrut(5));

        panel.add(new JLabel("年龄："));
        panel.add(ageField);
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("课程名："));
        panel.add(classnameField);
        panel.add(Box.createVerticalStrut(10));

        JButton addButton = new JButton("添加学生");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton.addActionListener(e -> {
            if (idField.getText().isEmpty()||nameField.getText().isEmpty()||genderField.getText().isEmpty()
                    ||ageField.getText().isEmpty()||classnameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请补充完信息");
                return;
            }
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String gender = genderField.getText().trim();
            String age = ageField.getText().trim();
            int Age=Integer.parseInt(age);
            String classname = classnameField.getText().trim();
            ZGL_Student Student=new ZGL_Student(id,name,gender,Age,classname);
            Object repesent= ZGLClient.sendObjectToServer(Student);
            if(repesent!=null)
            {
                int result = (int) repesent;
                if (result == 1) {

                    JOptionPane.showMessageDialog(this, "学生信息添加成功");
                }
                else if (result == 0) {
                    JOptionPane.showMessageDialog(this, "学生信息添加失败,学号已存在,请重新填写学号");
                }
                else {
                    JOptionPane.showMessageDialog(this, "未知错误1");
                }

            }



            //添加学生事件
        });

        panel.add(addButton);
        return panel;
    }




}


