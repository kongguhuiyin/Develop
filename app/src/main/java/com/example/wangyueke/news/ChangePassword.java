package com.example.wangyueke.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.wangyueke.news.MainActivity.str1;

public class ChangePassword extends AppCompatActivity {

    private EditText input1;
    private EditText input2;
    private EditText input3;
    private TextView txt;
    Button button1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.change_password_layout );
        input2 = (EditText) findViewById(R.id.old_Password );
        input3 = (EditText) findViewById(R.id.new_Password );
        txt = (TextView)findViewById(R.id.Account );
        button1 = (Button) findViewById(R.id.ChangePasswordButton );
        txt.setText(MainActivity.str1);   //提前显示账户
        button1.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                // 1.加载JDBC驱动
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    System.out.println("加载JDBC驱动成功");
                } catch (ClassNotFoundException e) {
                    System.out.println("加载JDBC驱动失败");
                    return;
                }
                final Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ResultSet rSet = null;
                        // 反复尝试连接，直到连接成功后退出循环
                        while (!Thread.interrupted()) {
                            try {
                                Thread.sleep(100);  // 每隔0.1秒尝试连接
                                System.out.println("尝试中..." + toString());
                            } catch (InterruptedException e) {
                                System.out.println("好像不太行" + toString());
                            }
                            // 2.设置好IP/端口/数据库名/用户名/密码等必要的连接信息
                            int port = 3306;
                            String dbName = "person";
                            String url = "jdbc:mysql://" + MainActivity.ip + ":" + port + "/" + dbName; // 构建连接mysql的字符串
                            String user = "root";
                            String password = "123";
                            // 3.连接JDBC
                            Connection conn = null;
                            try {
                                //conn = DriverManager.getConnection(serverUrl, user, password);
                                conn = DriverManager.getConnection(url, user, password);
                                System.out.println("远程连接成功!");
                                //conn.close();
                                //return;
                            } catch (SQLException e) {
                                System.out.println("远程连接失败!");
                            }
                            if (conn != null) {
                                try {
                                    String pass = "";
                                    String str2 = input2.getText().toString();     //旧密码
                                    String str3 = input3.getText().toString();     //新密码
                                    String SQL = "select * from personinfo where name = ?;";//从personinfo表中查找所用数据，限制条件为（where后面为限制条件）name = ?
                                    PreparedStatement find = conn.prepareStatement(SQL); //PreparedStatement用于执行带或不带参数的预编译SQL语句
                                    find.setString(1, MainActivity.str1);//调用setString方法给sql中的第一个问号赋值，这里x为变量。即查找personinfo表中限制条件为表中的name = x的所有行，所以返回的结果就是name = x 的所用行数据。
                                    ResultSet rs = find.executeQuery();//返回PreparedStatement语句执行的结果
                                    while (rs.next()) {
                                        //通过rs的get方法得到指针指向当前行的password字段对应的值
                                        pass = rs.getString("password");
                                    }
                                    if (str2.equals("")) {     //有一项为空。                                        Looper.prepare();
                                        Looper.prepare();
                                        Toast.makeText(ChangePassword.this, "旧密码不能为空！", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    } else if (str3.equals("")) {
                                        Looper.prepare();
                                        Toast.makeText(ChangePassword.this, "新密码不能为空！", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    } else {
                                        if (str2.equals(pass)) {     //旧密码匹配成功
                                            //写修改语句
                                            SQL = "UPDATE personinfo SET PASSWORD = ? WHERE NAME = ?;";
                                            PreparedStatement update = conn.prepareStatement(SQL);
                                            update.setString(1, str3);//调用setString方法给sql中的第一个问号赋值
                                            update.setString(2, MainActivity.str1);
                                            update.executeUpdate();
                                            update.close();

                                            Looper.prepare();
                                            Toast.makeText(ChangePassword.this, "修改成功！请牢记您的密码。", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent();
                                            intent.setClass(ChangePassword.this, setting.class);
                                            startActivity(intent);
                                            Looper.loop();
                                        } else {       //旧密码不匹配
                                            Looper.prepare();
                                            Toast.makeText(ChangePassword.this, "旧密码错误！", Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }
                                    }
                                } catch (SQLException e) {
                                    System.out.println("createStatement error");
                                }
                                try {
                                    conn.close();
                                    System.out.println("关闭连接成功");
                                    break ;
                                } catch (SQLException e) {
                                    System.out.println("关闭连接失败");
                                }
                            }
                        }
                    }
                });
                thread.start();
            }
        });
    }
}