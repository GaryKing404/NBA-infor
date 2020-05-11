package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.Bean.RaceOverview;
import com.example.DataSouce.RaceOkHttp_AsyncDataSource;
import com.example.adapter.RaceAdapter;
import com.example.ruangong.R;
import com.shizhefei.mvc.MVCCoolHelper;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;

import java.util.List;

public class MainFragment extends Fragment {

    private MVCHelper<List<RaceOverview>> mvcHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View mineFragment = inflater.inflate(R.layout.pulltofrefreshlistview, container, false);
//        CoolRefreshView coolRefreshView = (CoolRefreshView) mineFragment.findViewById(R.id.listview_funnyRefreshView);
//
//        mvcHelper = new MVCCoolHelper<List<Book>>(coolRefreshView);
//
//        // 设置数据源
//        mvcHelper.setDataSource(new BooksOkHttp_AsyncDataSource());
//        // 设置适配器
//        mvcHelper.setAdapter(new BooksAdapter(coolRefreshView.getContext()));
//
//        // 加载数据
//        mvcHelper.refresh();
//
//        return mineFragment;

        View mineFragment = inflater.inflate(R.layout.raceoverview_listview, container, false);
        CoolRefreshView coolRefreshView = (CoolRefreshView) mineFragment.findViewById(R.id.listview_RaceRefreshView);

//        ListView listView = mineFragment.findViewById(R.id.listview_RacelistView);
//
//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "点击了哦", Toast.LENGTH_SHORT).show();
//            }
//        });

        mvcHelper = new MVCCoolHelper<List<RaceOverview>>(coolRefreshView);

        // 设置数据源
        mvcHelper.setDataSource(new RaceOkHttp_AsyncDataSource());
        // 设置适配器
        mvcHelper.setAdapter(new RaceAdapter(coolRefreshView.getContext(), getActivity()));

        // 加载数据
        mvcHelper.refresh();

        return mineFragment;
    }
}
