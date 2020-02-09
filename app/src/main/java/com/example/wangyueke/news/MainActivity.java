package com.example.wangyueke.news;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText input1;
    private EditText input2;
    Button button1 = null;
    Button button2 = null;
    Button button3 = null;
    public static String str1 = null;     //账户
    public static String str2 = null;     //密码
    public static String ip = "192.168.43.82";     //密码

    private Context context;
    //public static String serverUrl = "http://211.69.252.173:8080//";
    public static String serverUrlName = "http://192.168.43.82:8080/app/personName.jsp";
    public static String serverUrlPass = "http://192.168.43.82:8080/app/personPassword.jsp";

    //@SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_main);

        /*context = getApplicationContext();
        ArrayList<String> arrayList = new ArrayList();

        getDatasync();

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                List<Person> list = JSON.parseArray(bundle.getString("json"), Person.class);
                if (list != null) {
                    for (Person p : list) {
                        Log.d("list", (String) JSON.toJSON(p));
                    }
                }
            }
        };*/

       /*final Map<String, String> InfoMap = new HashMap<String, String>();*/
        input1 = (EditText) findViewById(R.id.text1);
        input2 = (EditText) findViewById(R.id.text2);
        button1 = (Button)findViewById(R.id.login);
        button2 = (Button)findViewById(R.id.registered );
        button3 = (Button)findViewById(R.id.findPass );
        //text1 = (TextView) findViewById(R.id.denglu );
        //text1.setTypeface(Typeface.createFromAsset(getAssets(),"fff.TTF"));//更换字体
        //text1.setEnabled(false);
        button2.setOnClickListener(new View.OnClickListener( ) {         //注册按钮
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Registered.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener( ) {         //注册按钮
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, FindPassword.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                getMassage();
                // 1.加载JDBC驱动
//                try {
//                    Class.forName("com.mysql.jdbc.Driver");
//                    System.out.println("加载JDBC驱动成功");
//                } catch (ClassNotFoundException e) {
//                    System.out.println("加载JDBC驱动失败");
//                    return;
//                }
//                final Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ResultSet rSet = null;
//                        // 反复尝试连接，直到连接成功后退出循环
//                        while (!Thread.interrupted()) {
//                            try {
//                                Thread.sleep(100);  // 每隔0.1秒尝试连接
//                                System.out.println("尝试中..." + toString());
//                            } catch (InterruptedException e) {
//                                System.out.println("好像不太行" + toString());
//                            }
//                            // 2.设置好IP/端口/数据库名/用户名/密码等必要的连接信息
//                            int port = 3306;
//                            String dbName = "person";
//                            String url = "jdbc:mysql://" + MainActivity.ip + ":" + port + "/" + dbName; // 构建连接mysql的字符串
//
//                            String user = "root";
//                            String password = "123";
//                            // 3.连接JDBC
//                            Connection conn = null;
//                            try {
//                                //conn = DriverManager.getConnection(serverUrl, user, password);
//                                conn = DriverManager.getConnection(url, user, password);
//                                System.out.println("远程连接成功!");
//                                //conn.close();
//                                //return;
//                            } catch (SQLException e) {
//                                System.out.println("远程连接失败!");
//                            }
//                            if (conn != null) {
//                                String sql = "SELECT * FROM personinfo";
//                                String pass = "";
//                                System.out.println("-----------------------1");
//                                try {
//                                    // 创建用来执行sql语句的对象
//                                    java.sql.Statement statement = conn.createStatement();
//                                    System.out.println("-----------------------2");
//                                    // 执行sql查询语句并获取查询信息
//                                    rSet = statement.executeQuery(sql);
//                                    // 迭代打印出查询信息
//                                    System.out.println("    人员信息");
//                                    while (rSet.next()) {
//                                        System.out.println(rSet.getString("id") + "\t" + rSet.getString("name") + "\t"
//                                                + rSet.getString("sex") + "\t" + rSet.getString("email") + "\t" + rSet.getString("password"));
//                                        //InfoMap.put(rSet.getString("name"), rSet.getString("password"));
//                                    }
//                                    str1 = input1.getText().toString();     //账户
//                                    str2 = input2.getText().toString();     //密码
//                                    String SQL = "select * from personinfo where name = ?;";//从personinfo表中查找所用数据，限制条件为（where后面为限制条件）name = ?
//                                    PreparedStatement find = conn.prepareStatement(SQL); //PreparedStatement用于执行带或不带参数的预编译SQL语句
//                                    find.setString(1, str1);//调用setString方法给sql中的第一个问号赋值，这里x为变量。即查找personinfo表中限制条件为表中的name = x的所有行，所以返回的结果就是name = x 的所用行数据。
//                                    ResultSet rs = find.executeQuery();//返回PreparedStatement语句执行的结果
//                                    while (rs.next()) {
//                                        //通过rs的get方法得到指针指向当前行的password字段对应的值
//                                        pass = rs.getString("password");
//                                    }
//                                    System.out.println("--------str1 = " + str1);
//                                    if(str1.equals("") || str2.equals("")) {
//                                        Looper.prepare();
//                                        Toast.makeText(MainActivity.this,"账号或密码不能为空！", Toast.LENGTH_SHORT).show();
//                                        Looper.loop();
//                                    } else {
//                                        if(str2.equals(pass)) {
//                                            Looper.prepare();
//                                            Toast.makeText(MainActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent();
//                                            intent.setClass(MainActivity.this, Browse.class);
//                                            startActivity(intent);
//                                            Looper.loop();
//                                        } else {
//                                            Looper.prepare();
//                                            Toast.makeText(MainActivity.this,"账号或密码错误！", Toast.LENGTH_SHORT).show();
//                                            Looper.loop();
//                                        }
//                                    }
//                                } catch (SQLException e) {
//                                    System.out.println("createStatement error");
//                                }
//                                try {
//                                    conn.close();
//                                    System.out.println("关闭连接成功");
//                                    break ;
//                                } catch (SQLException e) {
//                                    System.out.println("关闭连接失败");
//                                }
//                            }
//                        }
//                    }
//                });
//                thread.start();
            }
        });

        /*Bundle bundle = new Bundle();
        Message message=new Message();
        bundle.putSerializable("arrayList:",arrayList);*/
    }
    public void getMassage() {

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.Q )
            @Override
            public void run() {
                try {
                    List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                    //Map<String,String> map = new HashMap<>();
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url(serverUrlName)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response;//得到Response 对象
                    response = client.newCall(request).execute();
                    String[] name = {};
                    String[] pass = {};
                    if (response.isSuccessful()) {
                        Log.d("Name", "response.code()==" + response.code());
                        String json = response.body().string();    //final
                        Log.d("Name", "res==" + json);
                        json = json.replace("<html>", "");
                        json = json.replace("</html>", "");
                        String temp = json.replace("\n", " ");
                        name = temp.split("\\s+");
                        for (String ss : name) {
                            System.out.println(ss);
                        }
                    }

                    OkHttpClient client1 = new OkHttpClient();//创建OkHttpClient对象
                    Request request1 = new Request.Builder()
                            .url(serverUrlPass)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response1;//得到Response 对象
                    response1 = client1.newCall(request1).execute();
                    if (response.isSuccessful()) {
                        Log.d("Pass", "response.code()==" + response1.code());
                        String json1 = response1.body().string();    //final
                        Log.d("Pass", "res==" + json1);
                        json1 = json1.replace("<html>","");
                        json1 = json1.replace("</html>","");
                        String temp1 = json1.replace("\n"," ");
                        pass = temp1.split("\\s+");
                        for(String ss : pass){
                            System.out.println(ss);
                        }
                    }
                    String password = null;
                    str1 = input1.getText().toString();     //账户
                    str2 = input2.getText().toString();     //密码
                    for(int i=0; i<name.length; i++) {
                        String str = name[i];
                        if(str.equals(str1)) {
                            password = pass[i];
                            System.out.println("密码：" + pass[i]);
                        }
                    }

                    if(str1.equals("") || str2.equals("")) {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this,"账号或密码不能为空！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        if(str2.equals(password)) {
                            Looper.prepare();
                            Toast.makeText(MainActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, Browse.class);
                            startActivity(intent);
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(MainActivity.this,"账号或密码错误！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /*@SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            List<Map<String,Object>> listitem = (List<Map<String, Object>>) bundle.getSerializable("name");
//            System.out.println(Bundle.getSerializable("name"));

        }
    };*/

    /*public void getDatasync(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url(serverUrl + "/test")//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {
                        Log.d("kwwl","response.code()=="+response.code());
                        Log.d("kwwl","response.message()=="+response.message());
                        final String json = response.body().string();
                        Log.d("kwwl","res=="+json);

                        //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                input1.setText(json);
                            }
                        });
//                        Handler mainHandler = new Handler(Looper.getMainLooper());
//                        final Response finalResponse = response;
//                        mainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                //已在主线程中，可以更新UI
//                                try {
//                                    Toast.makeText(MainActivity.this, finalResponse.body().string(), Toast.LENGTH_SHORT).show();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });

//                        Bundle bundle=new Bundle();
//                        Message msg=new Message();
//                        bundle.putString("json", response.body().string());
//                        msg.setData(bundle);
//                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public class GetDataThread extends Thread {
        //重点
        private Handler handler ;
        private String type;
        //传入两个参数，第一个是用于通信的handler，第二个是动态类型
        public GetDataThread(Handler h,String type){
            this.handler = h ;
            this.type = type;
        }
    };*/

}
