package com.example.wangyueke.news;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindPassword  extends AppCompatActivity {
    Button button1 = null;
    private EditText input1;
    private EditText input2;
    private TextView txt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.findpassword_layout);
        getSupportActionBar().hide();//隐藏标题栏
        initEvent();
        input1 = (EditText) findViewById(R.id.zhanghaokuang );
        input2 = (EditText) findViewById(R.id.codekuang );
        txt = (TextView)findViewById(R.id.massage );
        button1 = (Button)findViewById(R.id.findPassButton );
        button1.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                String str1 = input1.getText().toString();     //账户
                String str2 = input2.getText().toString();     //验证码
                if(str1.equals("")) {      //判断账户框是否为空
                    //Looper.prepare();
                    Toast.makeText(FindPassword.this,"账户不能为空！", Toast.LENGTH_SHORT).show();
                    //Looper.loop();
                } else {
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
                                    String pass = null;
                                    String code = "asd";
                                    System.out.println("-----------------------1");
                                    try {
                                        String str1 = input1.getText().toString();     //账户
                                        String str2 = input2.getText().toString();     //验证码
                                        String temp = " ";
                                        String SQL = "select * from personinfo where name = ?;";//从personinfo表中查找所用数据，限制条件为（where后面为限制条件）user_id = ?
                                        PreparedStatement find = conn.prepareStatement(SQL); //PreparedStatement用于执行带或不带参数的预编译SQL语句
                                        find.setString(1, str1);//调用setString方法给sql中的第一个问号赋值，这里x为变量。即查找course表中限制条件为表中的name = x的所有行，所以返回的结果就是name = x 的所用行数据。
                                        ResultSet rs = find.executeQuery();//返回PreparedStatement语句执行的结果
                                        while (rs.next()) {
                                            //通过rs的get方法得到指针指向当前行的password字段对应的值
                                            pass = rs.getString("password");    //为啥这里返回的pass值有空格啊
                                        }
                                        //pass = pass.replace(" ","");      //字符串只有一个空格的时候不能用？为啥呀
                                    /*if(pass.equals(temp)) {
                                        System.out.println("--------pass=" + pass + "&&");
                                        pass = "";
                                    }*/
                                        System.out.println("--------pass=" + pass + "||");
                                        if(pass != null) {
                                            System.out.println("--------------------pass不为空");
                                            if(str2.equals(code)) {     //判断验证码是否正确
                                                txt.setText("您的密码是：" + pass + "   请牢记您的密码！");
                                            } else {
                                                Looper.prepare();
                                                Toast.makeText(FindPassword.this,"验证码错误！", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }
                                        } else {
                                            System.out.println("--------------------pass为空");
                                            txt.setText("您输入的账号不存在！");
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
            }
        });
    }
    /*------------------------------------第一个弹窗开始------------------------------------*/
    private void initEvent() {
        findViewById(R.id.send ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("  验证码是：asd");
        builder.setPositiveButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    /*------------------------------------第一个弹窗结束------------------------------------*/
}
