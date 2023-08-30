package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class loginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private TextView register;
    private TextView repassword;
    private URL url;
    private String temp;
    private PrintWriter out;
    private Button btn_login;
    private Button btn_cancel;
    String strName;
    String strPassword;
    private String responseDate;
    private  String str_password;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("进入连接！！！！！！！！！！");
        setContentView(R.layout.activity_login);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        register = (TextView) findViewById(R.id.link_register);
        repassword = (TextView) findViewById(R.id.link_password);
        btn_cancel = (Button) findViewById(R.id.bt_bos);
        register.setClickable(true);
        repassword.setClickable(true);
        repassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, forgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, register.class);
                startActivity(intent);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });
        initView();
    }

    public void doRegister(View view) {
        String strName = username.getText().toString();
        String strPassword = password.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("name", strName);
        bundle.putString("password", strPassword);
    }

        private void initView() {
            //绑定控件
            username = (EditText) findViewById((R.id.et_username));
            password = (EditText) findViewById(R.id.et_password);
            btn_login = findViewById(R.id.bt_log);

            //为登录按钮设置点击事件
            btn_login.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String url = "http://m415i92312.qicp.vip/mydb/login";
                    strName = username.getText().toString();
                    strPassword = password.getText().toString();

                    //请求传入的参数
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", strName)
                            .add("password", strPassword)
                            .build();
                    HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                        public void onFailure(Call call, IOException e) {
                            Looper.prepare();
                            System.out.println("连接失败！！！！！！！！！！");
                            Toast.makeText(loginActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        public void onResponse(Call call, Response response) throws IOException {
                                Looper.prepare();
                                System.out.println("连接成功！！！！！！！！！！");
                                responseDate = response.body().string();
                                judge(responseDate);
                                System.out.println(responseDate);
                                //Toast.makeText(loginActivity.this, "成功,用户名为：" + strName, Toast.LENGTH_SHORT).show();
                                Looper.loop();
                           // signIn();
                        }
                    });
                }
            });
        }

        public void judge(String str){
            if(str.equals("true")){
                str_password = password.getText().toString();
                saveMes();
                Intent intent = new Intent( loginActivity.this,StartActivity.class);
                intent.putExtra("page","login");
                startActivity(intent);
            }
            else if(str.equals("password")){
                Toast.makeText(getBaseContext(), "密码错误", Toast.LENGTH_SHORT).show();
            }
            else if(str.equals("exist")){
                Toast.makeText(getBaseContext(), "用户名不存在", Toast.LENGTH_SHORT).show();
            }
        }

        public void saveMes(){
            SharedPreferences mySharedPreferences= getSharedPreferences("test",
                    Activity.MODE_PRIVATE);
            //实例化SharedPreferences.Editor对象（第二步）
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            //用putString的方法保存数据
            editor.putString("name", strName);
            editor.putString("password",str_password);
            //提交当前数据
            editor.commit();
            //使用toast信息提示框提示成功写入数据
           // Toast.makeText(this, "数据成功写入SharedPreferences！" , Toast.LENGTH_LONG).show();

        }




    }
