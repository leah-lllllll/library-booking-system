package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
//用户名，预约桌号，落座和离开状态，落座桌号，学习时间长？？？，学院
public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    public ImageView table1;
    public ImageView table2;
    public ImageView table3;
    public ImageView table4;
    public ImageView table5;
    public ImageView sit;
    public ImageView left;
    public ImageView navigation;
    private ImageView recommand;//推荐
    public TextView tv_t_1;//桌子一的人数
    public TextView tv_t_2;
    public TextView tv_t_3;
    public TextView tv_t_4;
    public TextView tv_t_5;
    public TextView my_page;
    public TextView nearby;
    public EditText username;
    public int nb_t_1 = 0;
    public int nb_t_2 = 0;
    public int nb_t_3 = 0;
    public int nb_t_4 = 0;
    public int nb_t_5 = 0;
    private String responseDate;
    private int[] strength;
    private String strName;
    private String lines[];
    private Handler myHandler;
    private Handler Ng_Handler;//导航处理
    private boolean state = false;//落座标记
    private boolean state2 = false;//预约状态
    private String now_table = "0";//目前落座的桌子
    private int ng_table;//导航选择的桌子
    private String mes;//确认从哪个页面点击
    private String comment;
    private String time;

    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    Date starttime;
    Date endtime;
    //final SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    //final SimpleDateFormat simpleDateFormat2;
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");


    //导航始末点
    int begining;
    int ending;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if(! wifi.isWifiEnabled()){
                        if(wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                            wifi.setWifiEnabled(true);
                    }
                    WifiInfo info = wifi.getConnectionInfo();

                    int[] strength = new int[6];
                    for(int i = 0;i<6;i++)
                    {
                        strength[i] = 91;
                    }
                    List<ScanResult> results = wifi.getScanResults();
                    for(ScanResult result:results) {
                        if (result.BSSID.equals("16:69:6c:b9:72:10")) {
                            strength[0] = Math.abs(result.level);
                        } else if (result.BSSID.equals("16:69:6c:b9:70:c6")) {
                            strength[1] = Math.abs(result.level);
                        } else if (result.BSSID.equals("16:69:6c:d6:9c:bc")) {
                            strength[2] = Math.abs(result.level);
                        } else if (result.BSSID.equals("16:69:6c:d6:9d:84")) {
                            strength[3] = Math.abs(result.level);
                        } else if (result.BSSID.equals("16:69:6c:d6:8f:18")) {
                            strength[4] = Math.abs(result.level);
                        } else if (result.BSSID.equals("16:69:6c:d6:94:94")) {
                            strength[5] = Math.abs(result.level);
                        }
                    }
                    for(int i = 0; i < 6;i++){
                        System.out.println(strength[i] + "***********************************");
                    }
                    connect(strength[0],strength[1],strength[2],strength[3],strength[4],strength[5]);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        username = findViewById(R.id.et_username);//非本页
        table1 = findViewById(R.id.table1);
        table2 = findViewById(R.id.table2);
        table3 = findViewById(R.id.table3);
        table4 = findViewById(R.id.table4);
        table5 = findViewById(R.id.table5);
        navigation = findViewById(R.id.navigation);
        recommand = (ImageView)findViewById(R.id.recommand);
        my_page = (TextView)findViewById(R.id.myPage) ;
        nearby = findViewById(R.id.nearby);
        sit = findViewById(R.id.sit);
        left = findViewById(R.id.left);
        tv_t_1 = findViewById(R.id.number1);
        tv_t_2 = findViewById(R.id.number2);
        tv_t_3 = findViewById(R.id.number3);
        tv_t_4 = findViewById(R.id.number4);
        tv_t_5 = findViewById(R.id.number5);
        nearby.setClickable(true);
        recommand.setClickable(true);
        my_page.setClickable(true);
        nearby.setOnClickListener(this);
        my_page.setOnClickListener(this);
        table1.setOnClickListener(this);
        table2.setOnClickListener(this);
        table3.setOnClickListener(this);
        table4.setOnClickListener(this);
        table5.setOnClickListener(this);
        navigation.setOnClickListener(this);
        navigation.setClickable(true);
        recommand.setClickable(true);

        sit.setClickable(true);
        left.setClickable(true);
        sit.setOnClickListener(this);
        left.setOnClickListener(this);
        recommand.setOnClickListener(this);

        //不要在前面加东西，谢谢合作
        starttime = new Date(System.currentTimeMillis());
        Intent intent = getIntent();
        if(intent != null){
            mes = intent.getStringExtra("page");
        }
        if(mes.equals("login")||mes.equals("register")){
            //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
            SharedPreferences sharedPreferences= getSharedPreferences("test",
                    Activity.MODE_PRIVATE);
            // 使用getString方法获得value，注意第2个参数是value的默认值
            strName =sharedPreferences.getString("name", "");
           // Toast.makeText(this, "读取数据如下："+"\n"+"name：" + strName + "\n" ,
                  //  Toast.LENGTH_LONG).show();
        }else {
            SharedPreferences sharedPreferences= getSharedPreferences("test",
                    Activity.MODE_PRIVATE);
            // 使用getString方法获得value，注意第2个参数是value的默认值
            try {
                strName = sharedPreferences.getString("name", "");
                String take_state = sharedPreferences.getString("take_state", "");
                String order_state = sharedPreferences.getString("order_state", "");
                now_table = sharedPreferences.getString("now_table", "");
                String starttime2 = sharedPreferences.getString("starttime", "");
                state = Boolean.valueOf(take_state);
                state2 = Boolean.valueOf(order_state);
                //starttime = new Date(System.currentTimeMillis());
                starttime = simpleDateFormat.parse(starttime2);
                //Toast.makeText(this, "读取数据如下：" + "\n" + "name：" + strName + "\n",
                        //Toast.LENGTH_LONG).show();
            }catch (java.text.ParseException e){

            }
        }

        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = msg.obj.toString();
                int arr[] = new int[6];
                System.out.println(lines);
                for (int i = 1; i < 6; i++) {
                    arr[i] = Integer.parseInt(lines[i]);
                }
                String number = String.valueOf(arr[1]);
                String text = number + "/4";
                tv_t_1.setText(text);
                String number2 = String.valueOf(arr[2]);
                String text2 = number2 + "/4";
                tv_t_2.setText(text2);
                String number3 = String.valueOf(arr[3]);
                String text3 = number3 + "/4";
                tv_t_3.setText(text3);
                String number4 = String.valueOf(arr[4]);
                String text4 = number4 + "/4";
                tv_t_4.setText(text4);
                String number5 = String.valueOf(arr[5]);
                String text5 = number5 + "/4";
                tv_t_5.setText(text5);
            }
        };

        String url = "http://m415i92312.qicp.vip/mydb/tableNum";
        System.out.println("进入连接！！！！！！！");
        System.out.println("进入连接2！！！！！！！");
        //final String startime =   starttime.toString();
        System.out.println("进入连接3！！！！！！！");
        //请求传入的参数
        RequestBody requestBody = new FormBody.Builder()
                .add("username", "lalalalalal")
                .build();
            HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                System.out.println("连接失败！！！！！！！！！！");
                Toast.makeText(StartActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();

            }

            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                System.out.println("连接成功！！！！！！！！！！");
                responseDate = response.body().string();
                judge(responseDate);
                System.out.println(responseDate);
                Looper.loop();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //showToast("自Android 6.0开始需要打开位置权限");
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }
        }
        //落座按钮实现
        final ImageView home_page = findViewById(R.id.sit);
        home_page.setClickable(true);
        home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starttime = new Date(System.currentTimeMillis());
                String str = simpleDateFormat.format(starttime);
                String str2 = simpleDateFormat2.format(starttime);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                });
                thread.start();
            }
        });
    }

    private class MyView extends SurfaceView {
        public MyView(Context context){
            super(context) ;
            setBackgroundResource(R.drawable.home_page2);
        }

    }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == REQUEST_CODE_ACCESS_COARSE_LOCATION) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                    //permission was granted, yay! Do the contacts-related task you need to do.
                    //这里进行授权被允许的处理
                } else {
                    //permission denied, boo! Disable the functionality that depends on this permission.
                    //这里进行权限被拒绝的处理
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

        }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.table1:
                System.out.println("table1!!!!!!!!!!!!!!!!");
                connect4("1");
                break;
            case R.id.table2:
                System.out.println("table2!!!!!!!!!!!!!!!!");
                connect4("2");
                break;
            case R.id.table3:
                System.out.println("table3!!!!!!!!!!!!!!!!");
                connect4("3");
                break;
            case R.id.table4:
                System.out.println("table4!!!!!!!!!!!!!!!!");
                connect4("4");
                break;
            case R.id.table5:
                System.out.println("table5!!!!!!!!!!!!!!!!");
                connect4("5");
                break;
            case R.id.left:
                if(state == false){
                    Toast.makeText(StartActivity.this, "您尚未落座哦" , Toast.LENGTH_SHORT).show();
                }
                else{
                    endtime = new Date(System.currentTimeMillis());
                    long diff=endtime.getTime()-starttime.getTime();
                    long days = diff / (1000 * 60 * 60 * 24);
                    long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                    long minutes=(diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60)+hours*60;
                    time = String.valueOf(minutes);
                    DiyDialog6();
                }
                break;
            case R.id.navigation://导航系统
                dialog2();

                //Toast.makeText(this, "读取数据如下："+"\n"+"name：" + ngTable+ "\n" ,
                        //Toast.LENGTH_LONG).show();
                break;
            case R.id.recommand:
                break;
            case R.id.myPage:
                saveMes();//保存最终数据信息，不可向后添加数据
                Intent intent2 = new Intent( StartActivity.this,my_page.class);
               // intent2.putExtra("usename",strName);
                startActivity(intent2);
                break;
            case R.id.nearby:
                saveMes();//保存最终数据信息，不可向后添加数据
                Intent intent3 = new Intent( StartActivity.this,recommand.class);
                // intent2.putExtra("usename",strName);
                startActivity(intent3);
                break;
            case R.id.sit:
                //saveMes();//保存最终数据信息，不可向后添加数据
                //Intent intent3 = new Intent( StartActivity.this,recommand.class);
                // intent2.putExtra("usename",strName);
                //startActivity(intent3);

                break;

        }
    }

    private void dialog2() {//导航按钮
        final String items[]={"1号桌","2号桌","3号桌","4号桌","5号桌","古典文学","现代文学","外国文学"};
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("导航"); //设置标题
        //builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.drawable.aaaa);//设置图标，图片id即可
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ng_table = which;
                SharedPreferences mySharedPreferences= getSharedPreferences("test",
                        Activity.MODE_PRIVATE);
                //实例化SharedPreferences.Editor对象（第二步）
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                String ngTable = String.valueOf(ng_table);
                String take_state = String.valueOf(state);
                editor.putString("ng_table",ngTable);
                //editor.putString("take_state",take_state);
                editor.commit();
                saveMes();
                Toast.makeText(StartActivity.this, items[which], Toast.LENGTH_SHORT).show();
                //sendMsg2("oooo");
                Intent intent = new Intent(StartActivity.this, navigation.class);
                startActivity(intent);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置高度和宽度
        p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.65

        p.gravity = Gravity.CENTER;//设置位置

        p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }

    private void DiyDialog1(final String table_N,String cs){
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(StartActivity.this);
        alterDiaglog.setTitle("桌子编号:" + table_N);//文字
        alterDiaglog.setMessage(cs);
        alterDiaglog.setPositiveButton("预约", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connect3(strName,table_N);
                //Toast.makeText(StartActivity.this,"点击了预约",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = alterDiaglog.create();

        //显示
        dialog.show();
        //自定义的东西
        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置高度和宽度
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.65

        p.gravity = Gravity.CENTER;//设置位置

        p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }

    public void connect(int x1,int  x2,int  x3,int  x4, int  x5, int  x6){//落座
        if(state == true){
            Toast.makeText(StartActivity.this, "您已落座，避免重复落座" , Toast.LENGTH_SHORT).show();
        }
        else {
            String url = "http://m415i92312.qicp.vip/mydb/currentOrder";
            System.out.println("进入连接！！！！！！！");
            System.out.println("进入连接2！！！！！！！");
            final String startime = starttime.toString();
            String Sx1 = Integer.toString(x1);
            String Sx2 = Integer.toString(x2);
            String Sx3 = Integer.toString(x3);
            String Sx4 = Integer.toString(x4);
            String Sx5 = Integer.toString(x5);
            String Sx6 = Integer.toString(x6);
            System.out.println("进入连接3！！！！！！！");
            //请求传入的参数
            RequestBody requestBody = new FormBody.Builder()
                    .add("x1", Sx1)
                    .add("x2", Sx2)
                    .add("x3", Sx3)
                    .add("x4", Sx4)
                    .add("x5", Sx5)
                    .add("x6", Sx6)
                    .add("username", strName)
                    .add("starttime", startime)
                    .build();
            HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    System.out.println("连接失败！！！！！！！！！！");
                    Toast.makeText(StartActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();
                    System.out.println("连接成功！！！！！！！！！！");
                    responseDate = response.body().string();
                    judge(responseDate);
                    System.out.println(responseDate);
                   // Toast.makeText(StartActivity.this, "成功,用户名为：" + strName, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });

        }

    }

    public void connect2(String usename,String  studytime,String  tablenumber,String comment){//离开
        String url = "http://m415i92312.qicp.vip/mydb/leave";
        //请求传入的参数
        System.out.println(tablenumber);
        RequestBody requestBody = new FormBody.Builder()
                .add("username", usename)
                .add("studytime", studytime)
                .add("tablenumber", tablenumber)
                .add("comment", comment)
                .build();
        HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                System.out.println("连接失败！！！！！！！！！！");
                Toast.makeText(StartActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();

            }

            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                System.out.println("连接成功！！！！！！！！！！");
                responseDate = response.body().string();
                judge(responseDate);
                System.out.println(responseDate);
               // Toast.makeText(StartActivity.this, "成功,用户名为：" + strName, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });

    }

    public void connect3(String usename,String tablenumber){//预约
        if(state == true){
            Toast.makeText(StartActivity.this, "您已预约~" , Toast.LENGTH_SHORT).show();
        }
        else {
            String url = "http://m415i92312.qicp.vip/mydb/reserve";
            //请求传入的参数
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", usename)
                    .add("tablenumber", tablenumber)
                    .build();
            HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    System.out.println("连接失败！！！！！！！！！！");
                    Toast.makeText(StartActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }

                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();
                    System.out.println("连接成功！！！！！！！！！！");
                    responseDate = response.body().string();
                    judge(responseDate);
                    System.out.println(responseDate);
                    Looper.loop();
                }
            });
            state2 = true;
        }
    }
        public void judge(String str) {
            lines = str.split(" ");
            if (str.equals("full")) {//满员
                DiyDialog4("请换个座位");
            }
            else if(lines[0].equals("num")){//第一次使用更新textVeiw
                sendMsg(str);
            }
            else if(lines[0].equals("true")) {//离开
                Toast.makeText(StartActivity.this, "本次学习时长："+time+"分钟！辛苦了，好好休息一下吧！", Toast.LENGTH_SHORT).show();
                sendMsg("lilili");
                state = false;
            }
            else if(lines[0].equals("right")){//预约落座到了正确的桌子
                DiyDialog3(lines[1]);
                now_table = lines[1];
                state = true;

            }
                else if(str.equals("already")){//预约过
                    Toast.makeText(StartActivity.this, "哎呀，已经预约过了", Toast.LENGTH_SHORT).show();
                }

            else if(lines[0].equals("rture")){//预约成功
                Toast.makeText(StartActivity.this, "恭喜拿到宝座", Toast.LENGTH_SHORT).show();
                sendMsg(str);
            }
            else if(lines[0].equals("rfalse")){//预约失败
                Toast.makeText(StartActivity.this, "哎呀，桌子人满了，换个桌子吧", Toast.LENGTH_SHORT).show();
            }

            else if(str.equals("false")) {//未到达预约桌
                Toast.makeText(StartActivity.this, "未到达预约桌子哦", Toast.LENGTH_SHORT).show();
                //sendMsg("lilili");
                state = false;
            }else {//传递桌号和当前状态
                DiyDialog2(lines[0]);
                now_table = lines[0];
                System.out.println(now_table+"!!!!!!!!!!!!!");
                //changeNumber();
                sendMsg(str);
                state = true;
            }


        }

    public void connect4(final String tablenumber)
    {
        String url = "http://m415i92312.qicp.vip/mydb/getComment";
        RequestBody requestBody = new FormBody.Builder()
                .add("tableNumber", tablenumber)
                .build();
        HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                System.out.println("连接失败！！！！！！！！！！");
                Toast.makeText(StartActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                System.out.println("连接成功！！！！！！！！！！");
                String cs = response.body().string();
                DiyDialog1(tablenumber,cs);
                //judge(responseDate);
                //System.out.println(responseDate);
                Looper.loop();
            }
        });
    }

    public void sendMsg(String str) {
       // Log.e(TAG, "得到的信息：" + msg);
        Message message = new Message();
        message.obj = str;
        myHandler.sendMessage(message);
    }

    public void sendMsg2(String str) {
        // Log.e(TAG, "得到的信息：" + msg);

        Message message = new Message();
        message.obj = str;
        Ng_Handler.sendMessage(message);
    }

    private void DiyDialog3(String str){//预约落座到正确桌子
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(StartActivity.this);
        //alterDiaglog.setIcon(R.drawable.icon);//图标
        alterDiaglog.setTitle("你好啊，"+ strName);//文字
        alterDiaglog.setMessage("你已经成功落座到预约:"+ str + "号桌\r\n" + "今天也是充实学习的一天啊！");//提示消息
        //积极的选择
        AlertDialog dialog = alterDiaglog.create();

        //显示
        dialog.show();
        //自定义的东西
        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置高度和宽度
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.5); // 宽度设置为屏幕的0.65

        p.gravity = Gravity.CENTER;//设置位置

        p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }

    private void DiyDialog4(String str){//桌子人满了
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(StartActivity.this);
        //alterDiaglog.setIcon(R.drawable.icon);//图标
        alterDiaglog.setTitle("你好啊，"+ strName);//文字
        alterDiaglog.setMessage("你身边的桌子人满了呢" + "请换一张桌子吧");//提示消息
        //积极的选择
        AlertDialog dialog = alterDiaglog.create();

        //显示
        dialog.show();
        //自定义的东西
        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置高度和宽度
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.5); // 宽度设置为屏幕的0.65

        p.gravity = Gravity.CENTER;//设置位置

        p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }

    private void DiyDialog5(String str){
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(StartActivity.this);
        //alterDiaglog.setIcon(R.drawable.icon);//图标
        alterDiaglog.setTitle("你好啊，"+ strName);//文字
        alterDiaglog.setMessage("你已经成功落座:"+ str + "号桌\r\n" + "今天也是充实学习的一天啊！");//提示消息
        //积极的选择
        AlertDialog dialog = alterDiaglog.create();

        //显示
        dialog.show();
        //自定义的东西
        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置高度和宽度
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.5); // 宽度设置为屏幕的0.65

        p.gravity = Gravity.CENTER;//设置位置

        p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }

    private void DiyDialog2(String str){//正常成功落座
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(StartActivity.this);
        //alterDiaglog.setIcon(R.drawable.icon);//图标
        alterDiaglog.setTitle("你好啊，"+ strName);//文字
        alterDiaglog.setMessage("你已经成功落座:"+ str + "号桌\r\n" + "今天也是充实学习的一天啊！");//提示消息
        //积极的选择
        AlertDialog dialog = alterDiaglog.create();

        //显示
        dialog.show();
        //自定义的东西
        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置高度和宽度
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.5); // 宽度设置为屏幕的0.65

        p.gravity = Gravity.CENTER;//设置位置

        p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }

    public void changeNumber(){
        int arr[] = new int[6];
        for(int i = 1;i < 6;i++){
            arr[i] =Integer.parseInt(lines[i]);
        }
        String number = String.valueOf(arr[1]);
        String text = number + "/6";
        tv_t_1.setText(text);
        String number2 = String.valueOf(arr[2]);
        String text2 = number2 + "/6";
        tv_t_2.setText(text2);
        String number3 = String.valueOf(arr[3]);
        String text3 = number3 + "/6";
        tv_t_3.setText(text3);
        String number4 = String.valueOf(arr[4]);
        String text4 = number4 + "/6";
        tv_t_4.setText(text4);
        String number5 = String.valueOf(arr[5]);
        String text5 = number5 + "/6";
        tv_t_5.setText(text5);

    }

    private void DiyDialog6(){//离开弹窗，需要留下用户评论
        final AlertDialog.Builder alertDialog7 = new AlertDialog.Builder(this);
        View view1 = View.inflate(this, R.layout.dialog_setview, null);
        final EditText et = view1.findViewById(R.id.et_evaluate);
        Button bu = view1.findViewById(R.id.bt_affirm);
        alertDialog7
                .setView(view1)
                .create();
        final AlertDialog show = alertDialog7.show();
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(StartActivity.this, "电话" + et.getText().toString(), Toast.LENGTH_SHORT).show();
                comment = et.getText().toString();
                show.dismiss();
                connect2(strName,time,now_table,comment);
            }
        });
       // AlertDialog dialog = alterDiaglog.create();

        //显示
        show.show();
        //自定义的东西
        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = show.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置高度和宽度
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.65

        p.gravity = Gravity.CENTER;//设置位置

        p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }

    public void saveMes(){

        SharedPreferences mySharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        String startTime = simpleDateFormat.format(starttime);
        String order_state = String.valueOf(state2);
        String take_state = String.valueOf(state);
        editor.putString("take_state",take_state);//落座状态，false代表尚未落座
        editor.putString("order_state", order_state);//预约状态，false代表尚未预约
        editor.putString("now_table",now_table);//当前落座桌子，若无落座则为“0”
        editor.putString("starttime",startTime);//开始时间
        //提交当前数据
        editor.commit();
        //使用toast信息提示框提示成功写入数据
       // Toast.makeText(this, "数据成功写入SharedPreferences！" , Toast.LENGTH_LONG).show();

    }
}
