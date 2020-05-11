package com.example.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.adapter.FragmentAdapter;
import com.example.fragment.AIFragment;
import com.example.fragment.MainFragment;
import com.example.fragment.MineFragment;
import com.example.fragment.RankFragment;
import com.example.ruangong.R;
import com.example.test.SearchDemo;
import com.example.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xutil.common.CollectionUtils;
import com.xuexiang.xutil.display.Colors;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, View.OnClickListener {


    //退出时的时间
    private long mExitTime;


    BottomNavigationView bnView;
    ViewPager viewPager;
    NavigationView navView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private String[] mTitles;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //System.out.println(LoginActivity.id_token);

        bnView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);
        navView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        mTitles = ResUtils.getStringArray(R.array.home_titles);
        //toolbar.setOnMenuItemClickListener();
        toolbar.setTitle(mTitles[0]);
        viewPager.setOffscreenPageLimit(mTitles.length - 1);
        setSupportActionBar(toolbar);
        //navView.setItemIconTintList(null);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_right_grey);
        intent = getIntent();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new RankFragment());
        fragments.add(new AIFragment());
        fragments.add(new MineFragment());
        initListeners();
        initHeader();

        MenuItem side = navView.getMenu().findItem(R.id.nav_liebiao);
        side.setChecked(true);

        FragmentAdapter adapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        String path = "http://114.116.114.99:8080/api/summaries/latestSupport";
        new NoticeTask().execute(path);

        //BottomNavigationView 点击事件监听
//        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                int menuId = menuItem.getItemId();
//                // 跳转指定页面：Fragment
//                switch (menuId) {
//                    case R.id.nav_news:
//                        viewPager.setCurrentItem(0);
//                        break;
//                    case R.id.nav_trending:
//                        viewPager.setCurrentItem(1);
//                        break;
//                    case R.id.nav_paihang:
//                        viewPager.setCurrentItem(2);
//                        break;
//                    case R.id.nav_profile:
//                        viewPager.setCurrentItem(3);
//                        break;
//                }
//                return false;
//            }
//        });
//
//        // ViewPager 滑动事件监听
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                //将滑动到的页面对应的 menu 设置为选中状态
//                bnView.getMenu().getItem(i).setChecked(true);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
    }

    protected void initListeners() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.isCheckable()) {
                drawerLayout.closeDrawers();
                return handleNavigationItemSelected(menuItem);
            } else {
                if (menuItem.getTitle().equals("搜索")) {
                    //Toast.makeText(this, "点击了:" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, SearchDemo.class);
                    startActivity(intent);
                }
            }
            return true;
        });

        viewPager.addOnPageChangeListener(this);
        bnView.setOnNavigationItemSelectedListener(this);
    }

    private boolean handleNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
            toolbar.setTitle(menuItem.getTitle());
            viewPager.setCurrentItem(index, false);
            return true;
        }
        return false;
    }


    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MenuItem item = bnView.getMenu().getItem(position);
        toolbar.setTitle(item.getTitle());
        item.setChecked(true);
        updateSideNavStatus(item);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
            toolbar.setTitle(menuItem.getTitle());
            viewPager.setCurrentItem(index, false);
            updateSideNavStatus(menuItem);
            return true;
        }
        return false;
    }

    private void updateSideNavStatus(MenuItem menuItem) {
        MenuItem side = navView.getMenu().findItem(menuItem.getItemId());
        if (side != null) {
            side.setChecked(true);
        }
    }

    private void initHeader() {
        navView.setItemIconTintList(null);
        View headerView = navView.getHeaderView(0);
        LinearLayout navHeader = headerView.findViewById(R.id.nav_header);
        RadiusImageView ivAvatar = headerView.findViewById(R.id.iv_avatar);
        TextView tvAvatar = headerView.findViewById(R.id.tv_avatar);
        TextView tvSign = headerView.findViewById(R.id.tv_sign);

        if (Utils.isColorDark(ThemeUtils.resolveColor(this, R.attr.colorAccent))) {
            tvAvatar.setTextColor(Colors.WHITE);
            tvSign.setTextColor(Colors.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_white));
            }
        } else {
            tvAvatar.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_title_text));
            tvSign.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_explain_text));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_gray_3));
            }
        }

        ivAvatar.setImageResource(R.drawable.ic_default_head);
        if (ChangePicActivity.img_url != null) {
            //ivAvatar.setImageBitmap(ChangePicActivity.user_pic);
            Glide.with(this).load(ChangePicActivity.img_url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(ivAvatar);
        }
        tvAvatar.setText(LoginActivity.user_name);
        tvSign.setText("这个家伙很懒，什么也没有留下～～");
        navHeader.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header:
                Toast.makeText(this, "点击头部", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 3000) {
            Toast.makeText(MainActivity.this, "再按一次退出当前应用程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    public void sendSubscribeMsg(String team1, String team2, String date) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "subscribe")
                .setContentTitle("赛事提醒")
                .setContentText(team1 + ":对阵" + team2 + "的比赛将于" + date + "进行！")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.basketballsym)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.basketballsym))
                .setAutoCancel(true)
                .build();
        manager.notify(2, notification);
    }

    class NoticeTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String path = objects[0].toString();
            //JSONObject obj = (JSONObject) objects[1];
            //System.out.println(obj.toString());
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, "");
            Request request = new Request.Builder().url(path).addHeader("Authorization", LoginActivity.id_token).get().build();
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
            //System.out.println(result);
            if (result.contains("team")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    sendSubscribeMsg(jsonObject.getString("team1"),
                            jsonObject.getString("team2"),
                            Utils.change2md(Utils.getDate(jsonObject.getString("date"))));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}