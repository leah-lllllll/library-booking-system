package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class register extends AppCompatActivity {
    private TextView verification;
    private EditText username2;
    private EditText phone_number;
    private EditText password2;
    private EditText repassword;
    private EditText en_verification;
    private Button btn_register;
    private Spinner school;
    private ArrayAdapter<String> adapter;
    private int s_position;
    private String strName;
    private String strPassword;
    private String reStrPassword;
    private String responseDate;
    private String  strPhone;
    private String strVerify;
    private  String str_password;
    private Handler myHandler;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        verification = (TextView) findViewById(R.id.tv_verification);
        school = (Spinner) findViewById(R.id.sp_xueyuan);
        verification.setClickable(true);
        verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(loginActivity.this,register.class);
//                startActivity(intent);
                verification.setText("已发送");
                strPhone = phone_number.getText().toString();
                connect2();
            }
        });
        final String[] arr = {"软件", "计算机", "语传", "建艺", "电信", "交运", "马克思", "理学院", "土建", "经管", "电气"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        school.setAdapter(adapter);
        school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {//直接数组源//String name = getResources().getStringArray(R.array.selector)[position];//Toast.makeText(getBaseContext(), name, Toast.LENGTH_SHORT).show();//适配器的时候
                s_position = position;
                String name = arr[position];//实质上如果是MainActivity.this也可以，但是如果改成this,会报错。因为这个用了匿名内部类
                System.out.println(s_position);
                Toast.makeText(getBaseContext(), name, Toast.LENGTH_SHORT).show();

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = msg.obj.toString();
                username2.setText("");
                password2.setText("");
                repassword.setText("");
                phone_number.setText("");
                en_verification.setText("");

            }
        };
        initView();
    }

    public void sendMsg(String str) {

        Message message = new Message();
        message.obj = str;
        myHandler.sendMessage(message);
    }


    private void initView() {
        //绑定控件
        username2 = (EditText) findViewById((R.id.et_username2));
        password2 = (EditText) findViewById(R.id.et_repassword);
        repassword = (EditText) findViewById(R.id.et_repassword2);
        btn_register= findViewById(R.id.bt_register);
        en_verification = (EditText)findViewById(R.id.et_verification) ;
        phone_number = (EditText)findViewById(R.id.et_phone);

        //为注册按钮设置点击事件
        btn_register.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    strName = username2.getText().toString();
                    strPassword = password2.getText().toString();
                    reStrPassword = repassword.getText().toString();
                    if (!strPassword.equals(reStrPassword)) {
                        Toast.makeText(getBaseContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                        username2.setText("");
                        password2.setText("");
                        repassword.setText("");
                        phone_number.setText("");
                        en_verification.setText("");
                    } else {
                        strVerify = en_verification.getText().toString();
                        //strPhone = phone_number.getText().toString();
                        System.out.println("进入连接！！！！！！！！！！！！！！！！！！！");
                        String url = "http:/m415i92312.qicp.vip/mydb/register";
                        String schoolN = Integer.toString(s_position);
                        System.out.println("进入连接2！！！！！！！！！！！！！！！！！！！");
                        //请求传入的参数
                        RequestBody requestBody = new FormBody.Builder()
                                .add("telephone", strPhone)
                                .add("username", strName)
                                .add("password", strPassword)
                                .add("school", schoolN)
                                .add("validcode", strVerify)
                                .build();
                        HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                System.out.println("连接失败！！！！！！！！！！");
                                Toast.makeText(register.this, "post请求失败", Toast.LENGTH_SHORT).show();
                                Looper.loop();

                            }
                            public void onResponse(Call call, Response response) throws IOException {
                                Looper.prepare();
                                System.out.println("连接成功！！！！！！！！！！");
                                responseDate = response.body().string();
                                judge(responseDate);
                                System.out.println(responseDate);
                                Looper.loop();

                                //signUp();
                            }
                        });
                    }
                }
            });


    }

    public void connect2( ){//发送验证码
        String url = "http://m415i92312.qicp.vip/mydb/sendMsg";
        //请求传入的参数

        RequestBody requestBody = new FormBody.Builder()
                .add("phone", strPhone)
                .build();
        HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                System.out.println("连接失败！！！！！！！！！！");
                Toast.makeText(register.this, "post请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();

            }

            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                System.out.println("连接成功！！！！！！！！！！");
                Toast.makeText(register.this, "成功发送验证码", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });

    }

    public void judge(String str){
        if(str.equals("true")){
            //Intent intent = new Intent( register.this,StartActivity.class);
            str_password = password2.getText().toString();
            saveMes();
            Intent intent = new Intent( register.this,StartActivity.class);
            intent.putExtra("page","register");
            startActivity(intent);
            Toast.makeText(register.this, "成功,用户名为：" + strName, Toast.LENGTH_SHORT).show();
            //startActivity(intent);
        }
        else if(str.equals("repeat")){
            Toast.makeText(getBaseContext(), "用户名重复", Toast.LENGTH_SHORT).show();
            sendMsg("aaa");
        }
        else{
            String temp[] = str.split(" ");
            if(temp[0].equals("valid"))
            {
                Toast.makeText(getBaseContext(), "验证码错误", Toast.LENGTH_SHORT).show();
            }

            /*username2.setText("");
            password2.setText("");
            repassword.setText("");
            phone_number.setText("");
            verification.setText("");*/
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
        //Toast.makeText(this, "数据成功写入SharedPreferences！" , Toast.LENGTH_LONG).show();

    }


    /*
    private void signUp() {
        // 注册是耗时过程，所以要显示一个dialog来提示下用户
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("注册中，请稍后...");
        mDialog.show();

        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    String username = strName;
                    String password = strPassword;
                    EMClient.getInstance().createAccount(username, password);
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            if (!register.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            Toast.makeText(register.this, "注册成功", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            if (!register.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            /**
                             * 关于错误码可以参考官方api详细说明
                             * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                             */

    /*
                            int errorCode = e.getErrorCode();
                            String message = e.getMessage();
                            Log.d("lzan13",
                                    String.format("sign up - errorCode:%d, errorMsg:%s", errorCode,
                                            e.getMessage()));
                            switch (errorCode) {
                                // 网络错误
                                case EMError.NETWORK_ERROR:
                                    Toast.makeText(register.this,
                                            "网络错误 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                // 用户已存在
                                case EMError.USER_ALREADY_EXIST:
                                    Toast.makeText(register.this,
                                            "用户已存在 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                case EMError.USER_ILLEGAL_ARGUMENT:
                                    Toast.makeText(register.this,
                                            "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: "
                                                    + errorCode
                                                    + ", message:"
                                                    + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 服务器未知错误
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    Toast.makeText(register.this,
                                            "服务器未知错误 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                case EMError.USER_REG_FAILED:
                                    Toast.makeText(register.this,
                                            "账户注册失败 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(register.this,
                                            "ml_sign_up_failed code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

*/

}
