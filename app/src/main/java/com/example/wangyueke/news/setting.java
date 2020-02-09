package com.example.wangyueke.news;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class setting extends AppCompatActivity {
    Button button_Exit = null;
    Button button_Delete = null;
    Button button_change = null;
    String str11 = MainActivity.str1;
    String str22 = MainActivity.str2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.setting_layout);    //调用默认布局管理器
        getSupportActionBar().hide();//隐藏标题栏
        button_Exit = (Button) findViewById(R.id.Exit);
        button_Delete = (Button) findViewById(R.id.Delete);
        button_change = (Button) findViewById(R.id.ChangePassword );
        button_change.setOnClickListener(new View.OnClickListener() {     //修改密码
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(setting.this, ChangePassword.class);
                startActivity(intent);
            }
        });
        button_Exit.setOnClickListener(new View.OnClickListener() {     //退出登录
            @Override
            public void onClick(View v) {
                showDialog1();
            }
        });
        button_Delete.setOnClickListener(new View.OnClickListener() {     //退出登录
            @Override
            public void onClick(View v) {
                showDialog2();
            }
        });
    }

    /*---------------------------------“退出”弹窗begin------------------------------------*/
    private void showDialog1() {
        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("          退出？")
                .setIcon(R.drawable.tubiao )
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setClass(setting.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("不退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        builder.show();
    }
    /*----------------------------------“退出”弹窗end------------------------------------*/

    /*-------------------------------“注销账户”弹窗begin---------------------------------*/
    private void showDialog2() {
        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("  确定要删除账户吗？(删除之后需要重新注册哦)")
                .setIcon(R.drawable.tubiao )
                .setPositiveButton("狠心删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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

                                    int port = 3306;
                                    String dbName = "person";
                                    String url = "jdbc:mysql://" + MainActivity.ip + ":" + port
                                            + "/" + dbName; // 构建连接mysql的字符串
                                    String user = "root";
                                    String password = "123";
                                    Connection conn = null;
                                    try {
                                        conn = DriverManager.getConnection(url, user, password);
                                        System.out.println("远程连接成功!");
                                        //conn.close();
                                        //return;
                                    } catch (SQLException e) {
                                        System.out.println("远程连接失败!");
                                    }
                                    if (conn != null) {
                                        String sql = "SELECT * FROM personinfo";
                                        String pass = "";
                                        System.out.println("-----------------------1");
                                        try {
                                            // 创建用来执行sql语句的对象
                                            java.sql.Statement statement = conn.createStatement();
                                            System.out.println("-----------------------2");
                                            // 执行sql查询语句并获取查询信息
                                            rSet = statement.executeQuery(sql);
                                            // 迭代打印出查询信息
                                            System.out.println("    人员信息");
                                            while (rSet.next()) {
                                                System.out.println(rSet.getString("id") + "\t" + rSet.getString("name") + "\t"
                                                        + rSet.getString("sex") + "\t" + rSet.getString("email") + "\t" + rSet.getString("password"));
                                                //InfoMap.put(rSet.getString("name"), rSet.getString("password"));
                                            }
                                            System.out.println("---------str11 = " + str11);
                                            //str11 = "'"+ str11 + "'";
                                            System.out.println("-----new_str11 = " + str11);
                                            String SQL = "delete from personinfo where name = ?;";//从personinfo表中查找所用数据，限制条件为（where后面为限制条件）name = ?
                                            PreparedStatement del = conn.prepareStatement(SQL); //PreparedStatement用于执行带或不带参数的预编译SQL语句
                                            System.out.println("------------------------1----------------------------");
                                            del.setString(1, str11);//调用setString方法给sql中的第一个问号赋值，这里x为变量。即查找personinfo表中限制条件为表中的name = x的所有行，所以返回的结果就是name = x 的所用行数据。
                                            System.out.println("------------------------2----------------------------");
                                            //ResultSet rs = del.executeQuery();//返回PreparedStatement语句执行的结果
                                            del.executeUpdate();
                                            System.out.println("------------------------3----------------------------");
                                            del.close();
                                            Looper.prepare();
                                            Toast.makeText(setting.this,"删除成功！", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent();
                                            intent.setClass(setting.this, MainActivity.class);
                                            startActivity(intent);
                                            Looper.loop();
                                        } catch (SQLException e) {
                                            System.out.println("----------------------deleteStatement error-------------------");
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
                })
                .setNegativeButton("还是算了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        builder.show();
    }
    /*--------------------------------“注销账户”弹窗end-----------------------------------*/
}