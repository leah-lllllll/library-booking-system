package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class forgetPasswordActivity extends AppCompatActivity {

    private TextView phoneText;
    private TextView verifyText;
    private TextView verification;
    private Button findButton;
    private String password;
    private String repassword;
    private String telephone;
    private String verify;
    private String responseDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        phoneText = (TextView)findViewById(R.id.et_phone);
        verifyText = (TextView)findViewById(R.id.et_verification);
        findButton = (Button)findViewById(R.id.bt_find_password);
        verification = (TextView)findViewById(R.id.tv_verification) ;

        verification.setClickable(true);
        verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verification.setText("已发送");
                telephone = phoneText.getText().toString();
                connect(telephone);
            }
        });

        findButton.setClickable(true);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify = verifyText.getText().toString();
                if(responseDate.equals(verify))
                {
                    //验证码正确，输两次新密码，给服务器传手机号和新密码
                    DiyDialog();
                }else{
                    Toast.makeText(forgetPasswordActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    verifyText.setText("");
                    verification.setText("获取验证码");
                }
            }
        });

    }

    public void connect(String phoneNumber){//忘记密码
        String url = "http://m415i92312.qicp.vip/mydb/sendMsg";
        //请求传入的参数
        final RequestBody requestBody = new FormBody.Builder()
                .add("phone", phoneNumber)
                .build();
        HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                System.out.println("连接失败！！！！！！！！！！");
                Looper.loop();
            }

            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                System.out.println("连接成功！！！！！！！！！！");
                responseDate = response.body().string();
                System.out.println(responseDate);
                Toast.makeText(forgetPasswordActivity.this, "成功发送验证码", Toast.LENGTH_SHORT).show();
                Looper.loop();
                if(responseDate.equals("true")) {
                    Intent intent = new Intent(forgetPasswordActivity.this, StartActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void DiyDialog(){//新密码
        final AlertDialog.Builder alertDialog7 = new AlertDialog.Builder(this);
        View view1 = View.inflate(this, R.layout.dialoh_two_et, null);
        final EditText et = view1.findViewById(R.id.et_fgpassword);
        final EditText et2 = view1.findViewById(R.id.et_refgpassword);
        Button bu = view1.findViewById(R.id.bt_affirm2);
        alertDialog7
                .setView(view1)
                .create();
        final AlertDialog show = alertDialog7.show();
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = et.getText().toString();
                repassword = et2.getText().toString();
                show.dismiss();
                if (password.equals(repassword))
                    connect2(password);
                else
                    Toast.makeText(forgetPasswordActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            }
        });

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

    public void connect2(String password)
    {
        String url = "http://m415i92312.qicp.vip/mydb/forgetPassword";
        RequestBody requestBody = new FormBody.Builder()
                .add("phone",telephone)
                .add("newPassword", password)
                .build();
        HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                System.out.println("连接失败！！！！！！！！！！");
                Toast.makeText(forgetPasswordActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                System.out.println("连接成功！！！！！！！！！！");
                String cs = response.body().string();
                Looper.loop();
            }
        });
    }

}