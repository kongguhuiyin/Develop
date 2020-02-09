package com.example.wangyueke.news;

import android.app.AppComponentFactory;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class News extends AppCompatActivity {

    View view = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.news_layout );


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
                //获得发送来的数据
                Intent intent = getIntent();
                String str = intent.getStringExtra("STR");
                System.out.println("阿拉啦啦啦str = " + str);
                /*-----------------------连接数据库获取url---------------------------*/
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
                    String dbName = "news";
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
                        String URL = "";
                        System.out.println("-----------------------1");
                        try {
                            java.sql.Statement statement = conn.createStatement();
                            String SQL = "select * from newslist where number = ?;";
                            PreparedStatement find = conn.prepareStatement(SQL);
                            find.setString(1, str);
                            ResultSet rs = find.executeQuery();
                            while (rs.next()) {
                                URL = rs.getString("NewsUrl");
                            }

                            final String finalURL = URL;
                            News.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //获得控件
                                    WebView webView = (WebView) findViewById(R.id.webview );
                                    //访问网页
                                    //webView.loadUrl("https://new.qq.com/omn/TWF20191/TWF2019121600818200.html");
                                    webView.loadUrl(finalURL);
                                    WebSettings settings = webView.getSettings();
                                    settings.setUseWideViewPort(true);
                                    settings.setLoadWithOverviewMode(true);
                                    settings.setDomStorageEnabled(true);
                                    settings.setJavaScriptEnabled(true);
                                    //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
                                    webView.setWebViewClient(new WebViewClient(){
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            //使用WebView加载显示url
                                            view.loadUrl(url);
                                            //返回true
                                            return true;
                                        }
                                    });
                                }
                            });

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