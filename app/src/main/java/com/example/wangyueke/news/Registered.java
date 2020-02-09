package com.example.wangyueke.news;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Registered extends AppCompatActivity {
    Button button1 = null;
    private EditText input1;
    private EditText input2;
    private EditText input3;
    private EditText input4;
    private RadioGroup group;
    private RadioButton rad1,rad2,rad3,rad4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.registered_layout);
        getSupportActionBar().hide();//隐藏标题栏
        button1 = (Button)findViewById(R.id.registered );
        input1 = (EditText) findViewById(R.id.text1);
        input2 = (EditText) findViewById(R.id.mimakuang );
        input3 = (EditText) findViewById(R.id.remimakuang );
        input4 = (EditText) findViewById(R.id.emailKuang );
        try {    //加载JDBC驱动
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("加载JDBC驱动成功");
        } catch (ClassNotFoundException e) {
            System.out.println("加载JDBC驱动失败");
            return;
        }
        button1.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                String root = input1.getText().toString();     //账户
                String pass = input2.getText().toString();     //密码
                String repass = input3.getText().toString();   //重复密码
                System.out.println("root    password     repassword");
                System.out.println("    " + root + "   " + pass + "   " + repass);
                if (root.equals("") || pass.equals("") || repass.equals("")) {
                    Toast.makeText(Registered.this, "账户或密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    if(pass.equals(repass)) {
                        new Thread(new Runnable() {     //创建新线程
                            @Override
                            public void run() {
                                System.out.println("-----------------------进入新线程");
                                int port = 3306;
                                String dbName = "person";
                                String url = "jdbc:mysql://" + MainActivity.ip + ":" + port
                                        + "/" + dbName; // 构建连接mysql的字符串
                                String user = "root";
                                String password = "123";
                                // 3.连接JDBC
                                Connection conn = null;
                                try {
                                    conn = DriverManager.getConnection(url, user, password);
                                    System.out.println("------------------------远程连接成功!");
                                    //conn.close();
                                    //return;
                                } catch (SQLException e) {
                                    System.out.println("------------------------远程连接失败!");
                                }
                                String root = input1.getText().toString();     //账户
                                String pass = input2.getText().toString();     //密码
                                String email = input4.getText().toString();    //邮箱
                                String sex = "";
                                group = (RadioGroup)findViewById(R.id.radio1);
                                rad1 = (RadioButton)findViewById(R.id.but1);
                                rad2 = (RadioButton)findViewById(R.id.but2);

                                int count = group.getChildCount();
                                System.out.println("-------count="+ count);

                                for(int i = 0 ;i < count;i++){
                                    RadioButton rb = (RadioButton)group.getChildAt(i);
                                    System.out.println("-------rb="+ rb.getText());
                                    if(rb.isChecked()){
                                        sex = rb.getText().toString();
                                        break;
                                    }
                                }
                                if (conn != null) {
                                    String sql = "insert into personinfo (name, sex, email, password) values (?,?,?,?);";
                                    try {
                                        PreparedStatement ins = conn.prepareStatement(sql);
                                        ins.setString(1, root);
                                        ins.setString(2, sex);
                                        ins.setString(3, email);
                                        ins.setString(4, pass);
                                        ins.executeUpdate();
                                        System.out.println("-----------------------更新");
                                        conn.close();
                                        ins.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }).start();
                        Toast.makeText(Registered.this,"注册成功,返回登陆", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setClass(Registered.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Registered.this,"两次密码不一致，请重新输入。", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
