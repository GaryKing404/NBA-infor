package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.Bean.RankTeam;
import com.example.DataSouce.RankOkHttp_AsyncDataSource;
import com.example.adapter.RankAdapter;
import com.example.ruangong.R;
import com.shizhefei.mvc.MVCCoolHelper;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;

import java.util.List;

public class RankFragment extends Fragment {
    private MVCHelper<List<RankTeam>> mvcHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mineFragment = inflater.inflate(R.layout.rank_listview, container, false);
        CoolRefreshView coolRefreshView = (CoolRefreshView) mineFragment.findViewById(R.id.listview_TeamRefreshView);

        mvcHelper = new MVCCoolHelper<List<RankTeam>>(coolRefreshView);

        // 设置数据源
        mvcHelper.setDataSource(new RankOkHttp_AsyncDataSource());
        // 设置适配器
        mvcHelper.setAdapter(new RankAdapter(coolRefreshView.getContext()));

        // 加载数据
        mvcHelper.refresh();

        return mineFragment;
    }
}
