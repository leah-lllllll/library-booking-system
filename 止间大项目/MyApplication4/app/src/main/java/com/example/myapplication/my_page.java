package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class my_page extends Activity {
    private ListView lv;
    private String strName;
    private TextView startPage;
    private TextView nearby;
    private TextView my_name;
    private String responseDate;
    private ImageView touxiang;
    private String school;
    private String studyTime;
    private String credit;
    private String phone;
    private String school_name;
    private Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        touxiang = findViewById(R.id.touxiang);
        nearby = findViewById(R.id.m_nearby);
        nearby.setClickable(true);
        SharedPreferences sharedPreferences = getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        strName = sharedPreferences.getString("name", "");
        //Toast.makeText(this, "读取数据如下：" + "\n" + "name：" + strName + "\n",
                //Toast.LENGTH_LONG).show();
        my_name = findViewById(R.id.name);
        my_name.setText(strName);
        lv = (ListView) findViewById(R.id.lv);
        startPage = findViewById(R.id.mainPage);
        startPage.setClickable(true);


        String url = "http://m415i92312.qicp.vip/mydb/getData";
        System.out.println("进入连接！！！！！！！");
        System.out.println("进入连接2！！！！！！！");
        //final String startime =   starttime.toString();
        System.out.println("进入连接3！！！！！！！");
        //请求传入的参数
        //Toast.makeText(my_page
               // .this, "成功,用户名为：" + strName, Toast.LENGTH_SHORT).show();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", strName)
                .build();
        HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                System.out.println("连接失败！！！！！！！！！！");
                Toast.makeText(my_page.this, "post请求失败", Toast.LENGTH_SHORT).show();
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

        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = msg.obj.toString();
                if (school.equals("0")) {
                    touxiang.setImageResource(R.drawable.software2);
                    school_name = "软件";
                } else if (school.equals("1")) {
                    touxiang.setImageResource(R.drawable.computer);
                    school_name = "计算机科学";
                } else if (school.equals("2")) {
                    touxiang.setImageResource(R.drawable.word);
                    school_name = "语言与传播";
                } else if (school.equals("3")) {
                    touxiang.setImageResource(R.drawable.art2);
                    school_name = "建筑与艺术";
                } else if (school.equals("5")) {
                    touxiang.setImageResource(R.drawable.transport);
                    school_name = "交通与运输";
                }
            }
        };


        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        /*在数组中存放数据*/


        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.rf1);//加入图片
        map.put("ItemTitle", "第1行");
        map.put("ItemText", "个人信息");
        listItem.add(map);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("ItemImage", R.drawable.rf2);//加入图片
        map2.put("ItemTitle", "第2行");
        map2.put("ItemText", "排行榜");
        listItem.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("ItemImage", R.drawable.rf3);//加入图片
        map3.put("ItemTitle", "第3行");
        map3.put("ItemText", "好友列表");
        listItem.add(map3);

        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("ItemImage", R.drawable.rf4);//加入图片
        map4.put("ItemTitle", "第4行");
        map4.put("ItemText", "联系我们");
        listItem.add(map4);

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, listItem,//需要绑定的数据
                R.layout.item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{"ItemImage", "ItemText"},
                new int[]{R.id.ItemImage, R.id.ItemTitle}
        );
        lv.setAdapter(mSimpleAdapter);//为ListView绑定适配器
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //setTitle("你点击了第" + arg2 + "行");//设置标题栏显示点击的行
                if(arg2 == 0){
                    DiyDialog();
                }
                else  if(arg2 == 1){
                    connect2();
                }
            }
        });
        startPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(my_page.this, StartActivity.class);
                intent.putExtra("page", "my_page");
                startActivity(intent);
            }
        });
        nearby.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(my_page.this, recommand.class);
                intent.putExtra("page", "my_page");
                startActivity(intent);
            }
        });


    }

    public void judge(String str) {
        String lines[] = str.split(" ");
        school = lines[0];
        studyTime = lines[1];
        credit = lines[2];
        phone = lines[3];
        sendMsg(lines[0]);
    }


    public void sendMsg(String str) {
        // Log.e(TAG, "得到的信息：" + msg);

        Message message = new Message();
        message.obj = str;
        myHandler.sendMessage(message);
    }


    private void DiyDialog(){
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(my_page.this);
        //alterDiaglog.setIcon(R.drawable.icon);//图标
        alterDiaglog.setTitle("个人信息：");//文字
        alterDiaglog.setMessage("--------------------" +"\n" +"我的姓名：" + strName + "\r\n" + "学院：" + school_name + "\r\n" + "手机号码："+ phone + "\r\n" + "信用度：" + credit
        +"\r\n" + "学习时长：" + studyTime + "\r\n");//提示消息
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


    public void connect2(){//离开
            String url = "http://m415i92312.qicp.vip/mydb/rank";
            System.out.println("进入连接！！！！！！！");
            System.out.println("进入连接2！！！！！！！");
            // final String startime = starttime.toString();
            System.out.println("进入连接3！！！！！！！");
            //请求传入的参数
            //System.out.println(tablenumber);
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", "1111111")
                    .build();
            HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    System.out.println("连接失败！！！！！！！！！！");
                    Toast.makeText(my_page.this, "post请求失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }

                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();
                    System.out.println("连接成功！！！！！！！！！！");
                    responseDate = response.body().string();
                    judge2(responseDate);
                    System.out.println(responseDate);
                    Toast.makeText(my_page.this, "成功,用户名为：" + strName, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });

        }

    public void judge2(String str){
        String rank[] = str.split(",");
        DiyDialog2(rank);

    }

    private void DiyDialog2(String lines[]){
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(my_page.this);
        //alterDiaglog.setIcon(R.drawable.icon);//图标
        alterDiaglog.setTitle("学习时长排行榜：");//文字
        int n = lines.length;
        String ranking = "";
        for(int i = 0; i < n; i++){
            String temp[] = lines[i].split(" ");
            //System.out.println(temp[0]);
            String mes =  temp[0] + "     " +temp[1];
            ranking += mes;
            ranking += "\r\n";
        }
        alterDiaglog.setMessage( ranking);//提示消息
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
        p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.65

        p.gravity = Gravity.CENTER;//设置位置

        p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }
}