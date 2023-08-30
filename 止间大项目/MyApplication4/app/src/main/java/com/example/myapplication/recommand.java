package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class recommand extends AppCompatActivity implements View.OnClickListener{
    private TextView startPage;
    private TextView myPage;
    private ImageView table1;
    private ImageView table2;
    private ImageView table3;
    private ImageView table4;
    private ImageView table5;
    private Handler myHandler;
    private TextView chat;
    private String responseDate;
    private String password;
    private String strName;

    private  String lines[];

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand);
        startPage = findViewById(R.id.r_mainPage);
        myPage = findViewById(R.id.r_myPage);
        table1 = findViewById(R.id.table1);
        table2 = findViewById(R.id.table2);
        chat = findViewById(R.id.chat);
        table3 = findViewById(R.id.table3);
        table4 = findViewById(R.id.table4);
        table5 = findViewById(R.id.table5);
        table1.setOnClickListener(this);
        table2.setOnClickListener(this);
        table3.setOnClickListener(this);
        table4.setOnClickListener(this);
        table5.setOnClickListener(this);
        startPage.setClickable(true);
        chat.setClickable(true);
        myPage.setClickable(true);



        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = msg.obj.toString();
                lines = str.split(",");
                int n = lines.length;
                if(lines[0].equals("null")) {
                }
                else{
                    String temp[] = lines[0].split(" ");
                    //System.out.println(temp[0]);
                    int person = temp.length;
                    if(person == 1)
                        table1.setImageDrawable(getResources().getDrawable(R.drawable.oneperson));
                    else if(person == 2)
                        table1.setImageDrawable(getResources().getDrawable(R.drawable.twoperson));
                    else if(person == 3)
                        table1.setImageDrawable(getResources().getDrawable(R.drawable.threeperson));
                    else if(person == 4)
                        table1.setImageDrawable(getResources().getDrawable(R.drawable.fourperson));
                }
                if(lines[1].equals("null")) {
                }
                else{
                    String temp[] = lines[1].split(" ");
                    //System.out.println(temp[0]);
                    int person = temp.length;
                    if(person == 1)
                        table2.setImageDrawable(getResources().getDrawable(R.drawable.oneperson));
                    else if(person == 2)
                        table2.setImageDrawable(getResources().getDrawable(R.drawable.twoperson));
                    else if(person == 3)
                        table2.setImageDrawable(getResources().getDrawable(R.drawable.threeperson));
                    else if(person == 4)
                        table2.setImageDrawable(getResources().getDrawable(R.drawable.fourperson));
                }
                if(lines[2].equals("null")) {
                }
                else{
                    String temp[] = lines[2].split(" ");
                    //System.out.println(temp[0]);
                    int person = temp.length;
                    if(person == 1)
                        table3.setImageDrawable(getResources().getDrawable(R.drawable.oneperson));
                    else if(person == 2)
                        table3.setImageDrawable(getResources().getDrawable(R.drawable.twoperson));
                    else if(person == 3)
                        table3.setImageDrawable(getResources().getDrawable(R.drawable.threeperson));
                    else if(person == 4)
                        table3.setImageDrawable(getResources().getDrawable(R.drawable.fourperson));
                }
                if(lines[3].equals("null")) {
                }
                else{
                    String temp[] = lines[3].split(" ");
                    //System.out.println(temp[0]);
                    int person = temp.length;
                    if(person == 1)
                        table4.setImageDrawable(getResources().getDrawable(R.drawable.oneperson));
                    else if(person == 2)
                        table4.setImageDrawable(getResources().getDrawable(R.drawable.twoperson));
                    else if(person == 3)
                        table4.setImageDrawable(getResources().getDrawable(R.drawable.threeperson));
                    else if(person == 4)
                        table4.setImageDrawable(getResources().getDrawable(R.drawable.fourperson));
                }
                if(lines[4].equals("null")) {
                }
                else{
                    String temp[] = lines[4].split(" ");
                    //System.out.println(temp[0]);
                    int person = temp.length;
                    if(person == 1)
                        table5.setImageDrawable(getResources().getDrawable(R.drawable.oneperson));
                    else if(person == 2)
                        table5.setImageDrawable(getResources().getDrawable(R.drawable.twoperson));
                    else if(person == 3)
                        table5.setImageDrawable(getResources().getDrawable(R.drawable.threeperson));
                    else if(person == 4)
                        table5.setImageDrawable(getResources().getDrawable(R.drawable.fourperson));
                }



            }
        };

        SharedPreferences sharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        strName =sharedPreferences.getString("name", "");
        password =sharedPreferences.getString("password", "");


        startPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(recommand.this, StartActivity.class);
                intent.putExtra("page", "recommand");
                startActivity(intent);
            }
        });
        myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(recommand.this, my_page.class);
                intent.putExtra("page", "recommand");
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                signIn();
                Intent intent = new Intent(recommand.this, ECMainActivity.class);
                intent.putExtra("page", "recommand");
                startActivity(intent);
            }
        });

        String url = "http://m415i92312.qicp.vip/mydb/near";
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
                Toast.makeText(recommand.this, "post请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();

            }

            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                System.out.println("连接成功！！！！！！！！！！");
                responseDate = response.body().string();
                sendMsg(responseDate);
                System.out.println(responseDate);
                // Toast.makeText(StartActivity.this, "成功,用户名为：" + strName, Toast.LENGTH_SHORT).show();
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

    public String tableC(int person){
        if(person == 1)
            return "R.drawable.oneperson";
        else if(person == 2)
            return "R.drawable.twoperson";
        else if(person == 3)
            return "R.drawable.threeperson";
        else if(person == 4)
            return "R.drawable.fourperson";
        return " ";

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.table1:
                DiyDialog1(lines[0],"一号桌");
                break;
            case R.id.table2:
                DiyDialog1(lines[1], "二号桌");
                break;
            case R.id.table3:
                DiyDialog1(lines[2],"三号桌");
                break;
            case R.id.table4:
                DiyDialog1(lines[3],"四号桌");
                break;
            case R.id.table5:
                DiyDialog1(lines[4],"五号桌");
                break;

        }
    }


    private void DiyDialog1(String str,String str2){
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(recommand.this);
        alterDiaglog.setTitle(str2 + ": " );//文字
        if(str.equals("null"))
            alterDiaglog.setMessage("没有人哎？");
        else {
            String temp[] = str.split(" ");
            int n = temp.length;
            String mes = "";
            for(int i = 0; i < n;i++) {
                mes+= "求知若渴的：";
                mes += temp[i];
                mes += "\r\n";
            }
            alterDiaglog.setMessage(mes);
        }
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



    /**
     * 登录方法
     */
    private void signIn() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在登陆，请稍后...");
        mDialog.show();
        String username = strName;
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(recommand.this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        EMClient.getInstance().login(username, password, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mDialog.dismiss();
                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        // EMClient.getInstance().groupManager().loadAllGroups();
                        // 登录成功跳转界面
                        Intent intent = new Intent(recommand.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mDialog.dismiss();
                        Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(recommand.this,
                                        "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(recommand.this,
                                        "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(recommand.this,
                                        "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(recommand.this,
                                        "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(recommand.this,
                                        "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(recommand.this,
                                        "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(recommand.this,
                                        "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(recommand.this,
                                        "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(recommand.this,
                                        "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(recommand.this,
                                        "ml_sign_in_failed code: " + i + ", message:" + s,
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            @Override public void onProgress(int i, String s) {

            }
        });
    }



}
