package com.example.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ruangong.R;
import com.example.utils.Checkformat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeInforActivity extends AppCompatActivity {

    Toolbar toolbar;
    private EditText old_psw;
    private EditText new_psw1;
    private EditText new_psw2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinfor);
        toolbar = findViewById(R.id.toolbar_infor);
        toolbar.setTitle("修改密码");
        toolbar.setNavigationIcon(R.drawable.ic_web_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        old_psw = findViewById(R.id.password_old);
        new_psw1 = findViewById(R.id.password_new1);
        new_psw2 = findViewById(R.id.password_new2);

    }

    public void Change(View view) {
        String psw_old = old_psw.getText().toString();
        String psw_new = new_psw1.getText().toString();
        String psw_new2 = new_psw2.getText().toString();

        if (!psw_new.equals(psw_new2)) {
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Checkformat.checkActandpsw12(psw_old, psw_new)) {
            Toast.makeText(this, "输入信息格式有误", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject obj = new JSONObject();
        try {
            //obj.put("user_id", LoginActivity.user_id);
            obj.put("oldPassword", psw_old);
            obj.put("newPassword", psw_new);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //获取网络上的servlet路径
        String path = "http://114.116.114.99:8080/api/users/editPassword";
        System.out.println(path);
        //调用getTask,把获取到的用户名，密码与路径放入方法中
        new ChangeTask().execute(path, obj);
    }

    class ChangeTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String path = objects[0].toString();
            JSONObject obj = (JSONObject) objects[1];
            System.out.println(obj.toString());
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, obj.toString());
            Request request = new Request.Builder().url(path).addHeader("Authorization", LoginActivity.id_token).post(requestBody).build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            String result = null;
            try {
                Response response = call.execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Object o) {
            String result = (String) o;
            if (isRight(result)) {
                Toast.makeText(ChangeInforActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ChangeInforActivity.this, "原密码错误，请重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isRight(String string) {
        System.out.println(string);
        if (string.contains("success")) {
            return true;
        }
        return false;
    }
}
