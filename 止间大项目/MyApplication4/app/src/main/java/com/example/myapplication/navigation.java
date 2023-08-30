package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class navigation extends AppCompatActivity {
    private Button navigation;
    private String strName;
    private String responseDate;
    private String ngTable;
    private String state;


    private Handler myHandler;

    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;

    private int begining;
    private int ending;

    public Handler mHandler1 = new Handler() {
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
                        System.out.println(strength[i] + "!***********************************");
                    }
                    connect1(strength[0],strength[1],strength[2],strength[3],strength[4],strength[5]);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public Handler mHandler2 = new Handler() {
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

                    int[] strength = new int[7];
                    for(int i = 0;i<7;i++)
                    {
                        strength[i] = 91;
                    }
                    List<ScanResult> results = wifi.getScanResults();
                    for(ScanResult result:results) {
                        if(result.BSSID.equals("16:69:6c:d6:a3:03")){
                            strength[0] = Math.abs(result.level);
                        } else if(result.BSSID.equals("16:69:6c:d4:47:f6")){
                            strength[1] = Math.abs(result.level);
                        } else if(result.BSSID.equals("16:69:6c:d6:8f:17")){
                            strength[2] = Math.abs(result.level);
                        } else if(result.BSSID.equals("16:69:6c:bc:65:91")){
                            strength[3] = Math.abs(result.level);
                        } else if(result.BSSID.equals("16:69:6c:d6:8f:18")){
                            strength[4] = Math.abs(result.level);
                        } else if(result.BSSID.equals("16:69:6c:d3:40:8e")){
                            strength[5] = Math.abs(result.level);
                        } else if(result.BSSID.equals("16:69:6c:d6:9d:83")){
                            strength[6] = Math.abs(result.level);
                        }
                    }
                    for(int i = 0; i < 7;i++){
                        System.out.print(strength[i] + "*****");
                    }
                    connect2(strength[0],strength[1],strength[2],strength[3],strength[4],strength[5],strength[6]);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public Handler mHandler3 = new Handler() {
        public void handleMessage(Message msg){
            //int number = Integer.getInteger(msg);
            String str = msg.obj.toString();
            int number = Integer.parseInt(str);
            MyView myView=new MyView(navigation.this,number);
            setContentView(myView);


         super.handleMessage(msg);}
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Intent intent = getIntent();

        // 使用getString方法获得value，注意第2个参数是value的默认值
        SharedPreferences sharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        state =sharedPreferences.getString("take_state", "");
        ngTable = sharedPreferences.getString("ng_table", "");
        Toast.makeText(this, "读取数据如下："+"\n"+"take_state：" + state + "\n" + "table:" + ngTable,
                Toast.LENGTH_LONG).show();

        navigation = findViewById(R.id.bt_navigation);
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(navigation.this, StartActivity.class);
                intent.putExtra("usename", strName);
                startActivity(intent);
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

        if(state.equals("true"))
        {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    mHandler1.sendMessage(message);
                }
            });
            thread.start();
            //连接服务器

        }
        else
        {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    mHandler2.sendMessage(message);
                }
            });
            thread.start();
            //连接服务器
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

    public void connect1(int x1,int  x2,int  x3,int  x4, int  x5, int  x6){//落座
            String url = "http://m415i92312.qicp.vip/mydb/gpsTable";
            String Sx1 = String.valueOf(x1);
            String Sx2 = String.valueOf(x2);
            String Sx3 = String.valueOf(x3);
            String Sx4 = String.valueOf(x4);
            String Sx5 = String.valueOf(x5);
            String Sx6 = String.valueOf(x6);
            //请求传入的参数
            RequestBody requestBody = new FormBody.Builder()
                    .add("x1", Sx1)
                    .add("x2", Sx2)
                    .add("x3", Sx3)
                    .add("x4", Sx4)
                    .add("x5", Sx5)
                    .add("x6", Sx6)
                    .build();
            HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    System.out.println("连接失败！！！！！！！！！！");
                    Toast.makeText(navigation.this, "post请求失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }

                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();
                    System.out.println("!!连接成功！！！！！！！！！！");
                    String sbegining = response.body().string();
                    begining = Integer.parseInt(sbegining);
                    begining -= 1;
                    System.out.println("daohang:"+sbegining + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    //begining = Integer.parseInt(sbegining);
                    String str = String.valueOf(begining);
                    sendMsg(str);
                    Looper.loop();
                }
            });
    }

    public void connect2(int x1,int  x2,int  x3,int  x4, int  x5, int  x6,int  x7){//落座
            String url = "http://m415i92312.qicp.vip/mydb/gpsRoad";
            String Sx1 = String.valueOf(x1);
            String Sx2 = String.valueOf(x2);
            String Sx3 = String.valueOf(x3);
            String Sx4 = String.valueOf(x4);
            String Sx5 = String.valueOf(x5);
            String Sx6 = String.valueOf(x6);
            String Sx7 = String.valueOf(x7);
            //请求传入的参数
            RequestBody requestBody = new FormBody.Builder()
                    .add("x1", Sx1)
                    .add("x2", Sx2)
                    .add("x3", Sx3)
                    .add("x4", Sx4)
                    .add("x5", Sx5)
                    .add("x6", Sx6)
                    .add("x7", Sx7)
                    .build();
            HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    System.out.println("连接失败！！！！！！！！！！");
                    Toast.makeText(navigation.this, "post请求失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();
                    System.out.println("~连接成功！！！！！！！！！！");
                    String sbegining = response.body().string();
                    System.out.println("daohang:"+sbegining + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    begining = Integer.parseInt(sbegining);
                    String str = String.valueOf(begining);
                    sendMsg(str);
                    Looper.loop();
                }
            });
    }

    private class MyView extends SurfaceView {
        private int number;
        public MyView(Context context,int number){
            super(context) ;
            setBackgroundResource(R.drawable.ng_b2);
            this.number = number;
            System.out.println(number + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        /*重写onDraw()*/
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            /*设置背景为白色*/
            setZOrderOnTop(true);
            getHolder().setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度
            canvas.drawColor(Color.TRANSPARENT);
            Paint paint=new Paint();

            /*去锯齿*/

            paint.setAntiAlias(true);

            /*设置paint的颜色*/

            paint.setColor(Color.RED);

            /*设置paint的 style 为STROKE：空心*/

            paint.setStyle(Paint.Style.STROKE);

            /*设置paint的外框宽度*/

            paint.setStrokeWidth(10);

            System.out.println("ng"+ngTable);
            ending=Integer.parseInt(ngTable);
            System.out.println(ending + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            int Xray[]=new int[22];
            int Yray[]=new int[22];

            Xray[0]= 225;  Yray[0]= 600;
            Xray[1]= 600;  Yray[1]= 600;
            Xray[2]= 950;  Yray[2]= 600;

            Xray[3]= 400;  Yray[3]= 1425;
            Xray[4]= 775;  Yray[4]= 1425;


            Xray[5]= 225;  Yray[5]= 1020;
            Xray[6]= 600;  Yray[6]= 1020;
            Xray[7]= 950;  Yray[7]= 1020;


            Xray[8]= 225;  Yray[8]= 800;
            Xray[9]= 600;  Yray[9]= 800;
            Xray[10]= 950;  Yray[10]= 800;

            Xray[11]= 225;  Yray[11]= 1225;
            Xray[12]= 600;  Yray[12]= 1225;
            Xray[13]= 950;  Yray[13]= 1225;


            Xray[14]= 100;  Yray[14]= 800;
            Xray[15]= 400;  Yray[15]= 800;
            Xray[16]= 775;  Yray[16]= 800;
            Xray[17]= 700;  Yray[17]= 800;

            Xray[18]= 100;  Yray[18]= 1225;
            Xray[19]= 400;  Yray[19]= 1225;
            Xray[20]= 775;  Yray[20]= 1225;
            Xray[21]= 700;  Yray[21]= 1225;

            int Matrix[][] =new int[22][22];
            for(int i=0;i<22;i++)
            {
                for(int j=0;j<22;j++)
                {
                    Matrix[i][j]=10000;
                }
            }

            Matrix[0][8]=1;//阅览室
            Matrix[1][9]=1;
            Matrix[2][10]=1;
            Matrix[3][19]=1;
            Matrix[4][20]=1;

            Matrix[5][8]=1;//书架
            Matrix[5][11]=1;
            Matrix[6][9]=1;
            Matrix[6][12]=1;
            Matrix[7][10]=1;
            Matrix[7][13]=1;


            Matrix[14][8]=1;//走廊1
            Matrix[8][15]=1;
            Matrix[15][9]=1;
            Matrix[9][16]=1;
            Matrix[16][10]=1;
            Matrix[10][17]=1;

            Matrix[18][11]=1;//走廊2
            Matrix[11][19]=1;
            Matrix[19][12]=1;
            Matrix[12][20]=1;
            Matrix[20][13]=1;
            Matrix[13][21]=1;


            Matrix[14][18]=1;//连通
            Matrix[15][19]=1;
            Matrix[16][20]=1;
            Matrix[17][21]=1;

            Matrix[8][0]=1;//阅览室
            Matrix[9][1]=1;
            Matrix[10][2]=1;
            Matrix[19][3]=1;
            Matrix[20][4]=1;

            Matrix[8][5]=1;//书架
            Matrix[11][5]=1;
            Matrix[9][6]=1;
            Matrix[12][6]=1;
            Matrix[10][7]=1;
            Matrix[13][7]=1;


            Matrix[8][14]=1;//走廊1
            Matrix[15][8]=1;
            Matrix[9][15]=1;
            Matrix[16][9]=1;
            Matrix[10][16]=1;
            Matrix[17][10]=1;

            Matrix[11][18]=1;//走廊2
            Matrix[19][11]=1;
            Matrix[12][19]=1;
            Matrix[20][12]=1;
            Matrix[13][20]=1;
            Matrix[21][13]=1;


            Matrix[18][14]=1;//连通
            Matrix[19][15]=1;
            Matrix[20][16]=1;
            Matrix[21][17]=1;


            int s[]=new int[22];
            for(int n=0;n<22;n++)
            {
                s[n]=0;
            }
            int d[]=new int[22];
            for(int n=0;n<22;n++)
            {
                d[n]=0;
            }
            int i, j, u = 0, k, mindis;
            int way[][] = new int[22][22];

            for(int m=0;m<22;m++)
            {
                for(int n=0;n<22;n++)
                {
                    way[m][n]=-1;
                }
            }

            int infinity = 10000;

            for (i = 0; i < 22; i++)
            {
                d[i] = Matrix[number][i];   //存储临时路劲长度
                s[i] = 0;                     //记录顶点i是否找到最短路径
                way[i][0] = number;   //将路径起点设为i
            }
            System.out.println("haha*********************:"+number);
            s[number] = 1;
            for (i = 1; i < 22; i++)
            {
                mindis = infinity;
                for (j = 1; j < 22; j++)    //找d[j]中最小值
                {
                    if (s[j] == 0 && d[j] < mindis)
                    {
                        u = j;
                        mindis = d[j];
                    }
                }
                s[u] = 1;  //顶点u已找到最短路径
                for (k = 1; k < 22; k++)   //将u设为路径终点
                {
                    if (way[u][k] == -1)
                    {
                        way[u][k] = u;
                        break;
                    }
                }
                for (j = 0; j < 22; j++)
                {
                    if (s[j] == 0 && d[j] > d[u] + Matrix[u][j])
                    {
                        d[j] = d[u] + Matrix[u][j];  //修改j的最短路径
                        for (k = 0; k < 22; k++)
                        {
                            if (way[u][k] != -1)
                                way[j][k] = way[u][k];  //a到c，c到d，更短，可以将a到c的路径先记入a到d的路径中
                            else
                                break;

                        }

                    }

                }
            }

            Path path=new Path();
            path.moveTo(Xray[number],Yray[number]);

            for (k = 1; k < 22; k++)   //画出路径
            {
                if (way[ending][k] != -1)
                    path.lineTo(Xray[way[ending][k]], Yray[way[ending][k]]);
                else
                    break;
            }
            canvas.drawPath(path, paint);



        }
    }

    public void sendMsg(String str) {
        // Log.e(TAG, "得到的信息：" + msg);
        Message message = new Message();
        message.obj = str;
        mHandler3.sendMessage(message);
    }

}
