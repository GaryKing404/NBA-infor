package com.example.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.RegexUtils;
import com.example.ruangong.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText account;
    private EditText pass1;
    private EditText pass2;
    private EditText uname;
    private EditText uemail;
    private Spinner spinner;
    private String myteam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        account = findViewById(R.id.account);
        pass1 = findViewById(R.id.upass1);
        pass2 = findViewById(R.id.upass2);
        uname = findViewById(R.id.uname);
        uemail = findViewById(R.id.user_email);
        spinner = findViewById(R.id.spinner_simple);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.spinner_values);
                //Toast.makeText(MainActivity.this, "你点击的是:"+languages[pos], 2000).show();
                myteam = languages[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

//    final Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 0x1) {
//                startActivity(new Intent(Register.this,MainActivity.class));
//            }
//        }
//    };


    public void userRegister(View view) {
        if (!pass1.getText().toString().equals(pass2.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            if (checkInfor()) {
                String path = "http://114.116.114.99:8080/api/user/create";
                new RegisterTask().execute(path);
            }
        }
    }

    class RegisterTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String account_ = account.getText().toString();
            String pass_ = pass1.getText().toString();
            String uname_ = uname.getText().toString();
            String email_ = uemail.getText().toString();
            JSONObject obj = new JSONObject();
            try {
                obj.put("user_account", account_);
                obj.put("user_pwd", pass_);
                obj.put("user_name", uname_);
                obj.put("user_email", email_);
                obj.put("user_team", myteam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println(obj.toString());
            String path = objects[0].toString();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, obj.toString());
            Request request = new Request.Builder().url(path).post(requestBody).build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            String result = null;
            try {
                Response response = call.execute();
                result = response.body().string();
                //System.out.println(result);
//                Message msg = new Message();
//                msg.what = 1;
                //handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Object o) {
            String result = (String) o;
            try {
                if (isRight(result)) {
                    //ToastUtils.show(Register.this,"注册成功");
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    //ToastUtils.show(Register.this,"注册失败");
                    Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkInfor() {
        if (!RegexUtils.isMobileExact(account.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass1.getText().toString().length() < 6) {
            Toast.makeText(RegisterActivity.this, "密码需6-12位", Toast.LENGTH_SHORT).show();
            return false;
        } else if (uname.getText().toString().trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!RegexUtils.isEmail(uemail.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "请输入正确邮箱", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isRight(String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.getString("user_account") != null) {
            return true;
        }
        return false;
    }
}
