package com.example.wangyueke.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.wangyueke.news.MainActivity.ip;

public class Browse extends AppCompatActivity {

    private TabHost myTabHost;		//定义TabHost
    private int[] layRes = { R.id.headlines ,R.id.China ,R.id.science ,R.id.finance ,R.id.mypage  };//定义内嵌布局管理器ID
    String str11 = MainActivity.str1;
    String str22 = MainActivity.str2;
    private TextView txt1,txt2,txt3;
    private Button but1 = null;
    View view4,view5,view6,view7,view8,view9,view10 = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.browse_layout) ;	//调用默认布局管理器
        txt1 = (TextView)findViewById(R.id.nicheng );
        txt2 = (TextView)findViewById(R.id.xinbie );
        txt3 = (TextView)findViewById(R.id.youxiang );
        but1 = (Button)findViewById(R.id.setting );

        view4 = (View) findViewById(R.id.view4 );
        view5 = (View) findViewById(R.id.view5 );
        view6 = (View) findViewById(R.id.view6 );
        view7 = (View) findViewById(R.id.view7 );
        view8 = (View) findViewById(R.id.view8 );
        view9 = (View) findViewById(R.id.view9 );
        view10 = (View) findViewById(R.id.view10 );
        view4.setOnClickListener(new ViewListener());
        view5.setOnClickListener(new ViewListener());
        view6.setOnClickListener(new ViewListener());
        view7.setOnClickListener(new ViewListener());
        view8.setOnClickListener(new ViewListener());
        view9.setOnClickListener(new ViewListener());
        view10.setOnClickListener(new ViewListener());

        getSupportActionBar().hide();//隐藏标题栏
        but1.setOnClickListener(new View.OnClickListener( ) {         //注册按钮
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Browse.this, setting.class);
                startActivity(intent);
            }
        });
//        view1.setOnClickListener(new View.OnClickListener( ) {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(Browse.this, News.class);
//                startActivity(intent);
//            }
//        });


        this.myTabHost = (TabHost) super.findViewById(R.id.tabhost); //取得TabHost对象
        this.myTabHost.setup() ;							//建立TabHost对象
        TabHost.TabSpec myTab1 = myTabHost.newTabSpec("头条");	//定义TabSpec
        myTab1.setIndicator("头条") ;			    			//设置选项文字
        myTab1.setContent(this.layRes[0]) ;				//设置显示的组件
        this.myTabHost.addTab(myTab1) ;					//增加选项
        TabHost.TabSpec myTab2 = myTabHost.newTabSpec("中国");	//定义TabSpec
        myTab2.setIndicator("中国") ;			    			//设置选项文字
        myTab2.setContent(this.layRes[1]) ;				//设置显示的组件
        this.myTabHost.addTab(myTab2) ;					//增加选项
        TabHost.TabSpec myTab3 = myTabHost.newTabSpec("科技");	//定义TabSpec
        myTab3.setIndicator("科技") ;			    			//设置选项文字
        myTab3.setContent(this.layRes[2]) ;				//设置显示的组件
        this.myTabHost.addTab(myTab3) ;					//增加选项
        TabHost.TabSpec myTab4 = myTabHost.newTabSpec("财经");	//定义TabSpec
        myTab4.setIndicator("财经") ;		 			//设置选项文字
        myTab4.setContent(this.layRes[3]) ;				//设置显示的组件
        this.myTabHost.addTab(myTab4) ;					//增加选项
        TabHost.TabSpec myTab5 = myTabHost.newTabSpec("我的");	//定义TabSpec
        myTab5.setIndicator("我的") ;		 			//设置选项文字
        myTab5.setContent(this.layRes[4]) ;				//设置显示的组件
        this.myTabHost.addTab(myTab5) ;					//增加选项

        /*-----------------------------------获取数据库信息-----------------------------------*/
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
                    String url = "jdbc:mysql://" + MainActivity.ip + ":" + port + "/" + dbName; // 构建连接mysql的字符串
                    String user = "root";
                    String password = "123";
                    Connection conn = null;
                    try {
                        conn = DriverManager.getConnection(url, user, password);
                        System.out.println("远程连接成功!");
                    } catch (SQLException e) {
                        System.out.println("远程连接失败!");
                    }
                    if (conn != null) {
                        String sql = "SELECT * FROM personinfo";
                        String pass = null;
                        String email = null;
                        String sex = null;
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
                            String SQL = "select * from personinfo where name = ?;";//从personinfo表中查找所用数据，限制条件为（where后面为限制条件）user_id = ?
                            PreparedStatement find = conn.prepareStatement(SQL); //PreparedStatement用于执行带或不带参数的预编译SQL语句
                            find.setString(1, str11);//调用setString方法给sql中的第一个问号赋值，这里x为变量。即查找course表中限制条件为表中的name = x的所有行，所以返回的结果就是name = x 的所用行数据。
                            ResultSet rs = find.executeQuery();//返回PreparedStatement语句执行的结果
                            while (rs.next()) {
                                //通过rs的get方法得到指针指向当前行的password字段对应的值
                                pass = rs.getString("password");
                                email = rs.getString("email");
                                sex = rs.getString("sex");
                            }
                            txt1.setText(str11);
                            if(sex == null) {
                                txt2.setText("  ");
                            } else if(sex.equals("男")) {
                                txt2.setText(sex + " ♂");
                            } else {
                                txt2.setText(sex + " ♀");
                            }
                            txt3.setText(email);
                            System.out.println("--------str11 = " + str11);

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
    class ViewListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent();
            String str = "";
            Intent intent1 = new Intent();
            switch(v.getId()){
                case R.id.view4 :
                    intent1.putExtra("STR","1");
                    intent1.setClass(Browse.this, News.class);
                    startActivity(intent1);
                    break;
                case R.id.view5 :
                    intent1.putExtra("STR","2");
                    intent1.setClass(Browse.this, News.class);
                    startActivity(intent1);
                    break;
                case R.id.view6 :
                    intent1.putExtra("STR","3");
                    intent1.setClass(Browse.this, News.class);
                    startActivity(intent1);
                    break;
                case R.id.view7 :
                    intent1.putExtra("STR","4");
                    intent1.setClass(Browse.this, News.class);
                    startActivity(intent1);
                    break;
                case R.id.view8 :
                    intent1.putExtra("STR","5");
                    intent1.setClass(Browse.this, News.class);
                    startActivity(intent1);
                    break;
                case R.id.view9 :
                    intent1.putExtra("STR","6");
                    intent1.setClass(Browse.this, News.class);
                    startActivity(intent1);
                    break;
                case R.id.view10 :
                    intent1.putExtra("STR","7");
                    intent1.setClass(Browse.this, News.class);
                    startActivity(intent1);
                    break;
            }
        }
    }
}
