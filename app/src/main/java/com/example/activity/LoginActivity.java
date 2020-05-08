package com.example.activity;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    private EditText uname;
    private EditText upass;
    public static SharedPreferences sharedPreferences;//应单独提出来设为全局变量
    public static String id_token = "";//应单独提出来设为全局变量
    public static String user_name = "游客";//应单独提出来设为全局变量
    public static int user_id = 0;//游客id为0
    public static String user_team = "火箭";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }


        //获取用户名的id
        uname = (EditText) findViewById(R.id.uname);
        //获取密码的id
        upass = (EditText) findViewById(R.id.upass);
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        /*清除登录状态*/
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.commit();

        String user_name1 = sharedPreferences.getString("user_name", null);
        String token = sharedPreferences.getString("user_token", null);
        String picture = sharedPreferences.getString("headPicture", null);
        int id = sharedPreferences.getInt("user_id", 0);

        if (user_name1 != null) {
//            byte[] bytes = Base64.decode(picture, Base64.DEFAULT);
//            ChangePicActivity.user_pic = (BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            ChangePicActivity.img_url = picture;
            id_token = token;
            user_name = user_name1;
            user_id = id;
            Intent intent = new Intent(this, MainActivity.class);
            //intent.putExtra("account", user_account);
            startActivity(intent);
            finish();
        }
    }

    public void LoginGet(View view) {
        //获取用户名的值
        String name = uname.getText().toString();
        //获取密码的值
        String pass = upass.getText().toString();

//        if (!Checkformat.checkActandpsw(name, pass)) {
//            Toast.makeText(this, "输入信息格式错误", Toast.LENGTH_SHORT).show();
//            return;
//        }

        //获取网络上的servlet路径
        String path = "http://114.116.114.99:8080/api/authenticate";
        //调用getTask,把获取到的用户名，密码与路径放入方法中
        new MainTask().execute(name, pass, path);
    }

    public void Register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void Youke(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    class MainTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            //依次获取用户名，密码与路径
            String name = params[0].toString();
            String pass = params[1].toString();
            String path = params[2].toString();

            try {
                //获取网络上get方式提交的整个路径
                JSONObject obj = new JSONObject();
                obj.put("username", name);
                obj.put("password", pass);
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(mediaType, obj.toString());
                final Request request = new Request.Builder().url(path).post(requestBody).build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Call call = okHttpClient.newCall(request);
                String result = null;
                try {
                    Response response = call.execute();
                    result = response.body().string();
                    System.out.println(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
//                okHttpClient.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        String result = response.body().string();
//                        System.out.println(result);
//                    }
//                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            String result = (String) o;
            String headPicture = null;
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.get("id_token") != null) {
                    JSONObject obj1 = (JSONObject) obj.get("user");
                    id_token = "Bearer " + (String) obj.get("id_token");
                    //user_id = obj1.getInt("user_id");
                    user_id = obj1.getInt("id");
                    user_name = obj1.getString("user_name");
                    result = "permit";

                    //todo:接受用户头像字符串并解码为bitmap，设置为user_pic
                    ChangePicActivity.img_url = obj1.getString("img_url");
                    //ChangePicActivity.img_url = headPicture;
                    System.out.println(ChangePicActivity.img_url);
//                    if (!headPicture.equals("")) {
//                        byte[] bytes = Base64.decode(headPicture, Base64.DEFAULT);
//                        ChangePicActivity.user_pic = (BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();//将Bitmap转成Byte[]
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);//压缩
//                        headPicture = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);//加密转换成String
//                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (result.equals("permit")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_name", user_name);
                editor.putInt("user_id", user_id);
                editor.putString("user_token", id_token);
                editor.putString("headPicture", ChangePicActivity.img_url);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "密码错误，请重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

//    public void sendChatMsg(View view) {
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = new NotificationCompat.Builder(this, "chat")
//                .setContentTitle("收到一条聊天消息")
//                .setContentText("今天中午吃什么？")
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.basketballsym)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.basketballsym))
//                .setAutoCancel(true)
//                .build();
//        manager.notify(1, notification);
//    }

}