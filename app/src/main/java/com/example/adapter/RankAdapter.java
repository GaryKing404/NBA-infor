package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Bean.RankTeam;
import com.example.ruangong.R;
import com.example.utils.Utils;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class RankAdapter extends BaseAdapter implements IDataAdapter<List<RankTeam>> {
    private List<RankTeam> rankTeams = new ArrayList<>();
    private LayoutInflater inflater;

    public RankAdapter(Context context) {
        super();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return rankTeams.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_team, parent, false);
        }
        TextView team_rank = convertView.findViewById(R.id.team_paiming);
        TextView team_name = convertView.findViewById(R.id.team_name);
        TextView team_win = convertView.findViewById(R.id.team_win);
        TextView team_lose = convertView.findViewById(R.id.team_lose);
        TextView team_rate = convertView.findViewById(R.id.team_rate);
        ImageView team_pic = convertView.findViewById(R.id.team_pic);
        team_rank.setText(rankTeams.get(position).getRank_num());
        team_name.setText(rankTeams.get(position).getTeam());
        team_win.setText(rankTeams.get(position).getWin_num());
        team_lose.setText(rankTeams.get(position).getLose_num());
        team_rate.setText(rankTeams.get(position).getWin_rate() + "%");
        Utils.setSymbol(team_pic, team_name.getText().toString());
        return convertView;
    }

    @Override
    public void notifyDataChanged(List<RankTeam> data, boolean isRefresh) {
        if (isRefresh) {
            rankTeams.clear();
        }
        rankTeams.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<RankTeam> getData() {
        return rankTeams;
    }
}
