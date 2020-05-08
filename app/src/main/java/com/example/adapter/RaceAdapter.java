/*
Copyright 2015 shizhefei（LuckyJayce）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Bean.RaceOverview;
import com.example.activity.MainActivity;
import com.example.activity.ShowDetailActivity;
import com.example.ruangong.R;
import com.example.utils.Utils;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class RaceAdapter extends BaseAdapter implements IDataAdapter<List<RaceOverview>> {
    private List<RaceOverview> raceOverviews = new ArrayList<RaceOverview>();
    private LayoutInflater inflater;
    private Context context;

    public RaceAdapter(Context context, Context activity) {
        super();
        this.context = activity;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return raceOverviews.size();
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
            convertView = inflater.inflate(R.layout.item_race, parent, false);
        }
        TextView time = convertView.findViewById(R.id.race_time);
        TextView team1 = convertView.findViewById(R.id.race_team1);
        TextView team2 = convertView.findViewById(R.id.race_team2);
        ImageView imageView1 = convertView.findViewById(R.id.race_image1);
        ImageView imageView2 = convertView.findViewById(R.id.race_image2);
        time.setText(raceOverviews.get(position).getTime());
        team1.setText(raceOverviews.get(position).getTeam1());
        team2.setText(raceOverviews.get(position).getTeam2());
        TextView complete = convertView.findViewById(R.id.race_complete);
        if (raceOverviews.get(position).getIsCompleted()) {
            TextView score1 = convertView.findViewById(R.id.race_score1);
            TextView score2 = convertView.findViewById(R.id.race_score2);
            score1.setText(raceOverviews.get(position).getScore1());
            score2.setText(raceOverviews.get(position).getScore2());
            complete.setText("查看详情");
            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowDetailActivity.class);
                    intent.putExtra("match_id", raceOverviews.get(position).getMatch_id());
                    context.startActivity(intent);
                }
            });
        } else {
            complete.setText("尚未进行");
        }
        Utils.setSymbol(imageView1, team1.getText().toString());
        Utils.setSymbol(imageView2, team2.getText().toString());
        return convertView;
    }

    @Override
    public void notifyDataChanged(List<RaceOverview> data, boolean isRefresh) {
        if (isRefresh) {
            raceOverviews.clear();
        }
        raceOverviews.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<RaceOverview> getData() {
        return raceOverviews;
    }

}
