package com.example.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ruangong.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePicActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;
    Bitmap bitmap;
    String headPicture;//图像转化成字符串
    //public static Bitmap user_pic;//应单独提出来设为全局变量
    public static String img_url = null;//图像url

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepic);
        toolbar = findViewById(R.id.toolbar_pic);
        imageView = findViewById(R.id.user_image);
        if (img_url != null) {
            //imageView.setImageBitmap(user_pic);
            Glide.with(this).load(img_url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(imageView);
        }
        toolbar.setTitle("修改头像");
        toolbar.setNavigationIcon(R.drawable.ic_web_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //bitmap = user_pic;
    }

    private void selectPic() {
        //intent可以应用于广播和发起意图，其中属性有：ComponentName,action,data等
        Intent intent = new Intent();
        intent.setType("image/*");
        //action表示intent的类型，可以是查看、删除、发布或其他情况；我们选择ACTION_GET_CONTENT，系统可以根据Type类型来调用系统程序选择Type
        //类型的内容给你选择
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //如果第二个参数大于或等于0，那么当用户操作完成后会返回到本程序的onActivityResult方法
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //用户操作完成，结果码返回是-1，即RESULT_OK
        if (resultCode == RESULT_OK) {
            //获取选中文件的定位符
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            //使用content的接口
            ContentResolver cr = this.getContentResolver();
            try {
                //获取图片
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //imageView.setImageBitmap(bitmap);
                Glide.with(this).load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .error(R.mipmap.ic_launcher)
                        .centerCrop()
                        .into(imageView);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        } else {
            //操作错误或没有选择图片
            Log.i("ChangePicActivity", "operation error");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Choose(View view) {
        selectPic();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Submit(View view) throws JSONException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//将Bitmap转成Byte[]
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);//压缩
        headPicture = Base64.getEncoder().encodeToString(baos.toByteArray());//加密转换成String
        //System.out.println(headPicture.length());
        //int n = headPicture.length();
        JSONObject object = new JSONObject();
        //object.put("user_id", LoginActivity.user_id);
        object.put("newImage", headPicture);

        String path = "http://114.116.114.99:8080/api/users/editImage";
        //调用getTask,把获取到的用户名，密码与路径放入方法中
        new PicTask().execute(path, object);
    }

    class PicTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String path = objects[0].toString();
            //JSONObject obj = (JSONObject) objects[1];
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, objects[1].toString());
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
            System.out.println(result);
            if (isRight(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (!jsonObject.getString("url").equals(""))
                        img_url = jsonObject.getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //user_pic = bitmap;
                SharedPreferences.Editor editor = LoginActivity.sharedPreferences.edit();
                editor.putString("headPicture", img_url);
                editor.commit();
                Toast.makeText(ChangePicActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(ChangePicActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(ChangePicActivity.this, "头像修改失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isRight(String string) {
        if (string.contains("url")) {
            return true;
        }
        return false;
    }
}
