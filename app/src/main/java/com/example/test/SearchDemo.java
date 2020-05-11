package com.example.test;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.Bean.SearchOverview;
import com.example.DataSouce.SearchOkHttp_AsyncDataSource;
import com.example.adapter.SearchAdapter;
import com.example.ruangong.R;
import com.shizhefei.mvc.MVCCoolHelper;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;

import java.util.List;


public class SearchDemo extends AppCompatActivity {

    // 1. 初始化搜索框变量
    private SearchView searchView;
    private MVCHelper<List<SearchOverview>> mvcHelper;
    private CoolRefreshView coolRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSubmitButtonEnabled(true);
        //设置为取则隐藏为只显示一个图标
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getApplicationContext(), "你查询的是："+query, Toast.LENGTH_SHORT).show();
                hintKbTwo();
                mvcHelper.setDataSource(new SearchOkHttp_AsyncDataSource(query));
                mvcHelper.refresh();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        //提示hint
        searchView.setQueryHint("搜点啥");

        coolRefreshView = (CoolRefreshView) findViewById(R.id.listview_SearchRefreshView);
        mvcHelper = new MVCCoolHelper<List<SearchOverview>>(coolRefreshView);

        mvcHelper.setAdapter(new SearchAdapter(coolRefreshView.getContext(), this));

    }

    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}