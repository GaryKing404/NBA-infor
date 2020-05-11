package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Bean.SearchOverview;
import com.example.activity.ShowDetailActivity;
import com.example.ruangong.R;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter implements IDataAdapter<List<SearchOverview>> {
    private List<SearchOverview> searchOverviews = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public SearchAdapter(Context context, Context activity) {
        super();
        this.context = activity;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return searchOverviews.size();
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
            convertView = inflater.inflate(R.layout.item_search, parent, false);
        }
        TextView title = convertView.findViewById(R.id.search_title);
        TextView content = convertView.findViewById(R.id.search_content);
        ImageView search_in = convertView.findViewById(R.id.search_in);
        title.setText(searchOverviews.get(position).getTitle());
        content.setText(searchOverviews.get(position).getContent());
        search_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowDetailActivity.class);
                intent.putExtra("match_id", searchOverviews.get(position).getMatch_id());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void notifyDataChanged(List<SearchOverview> data, boolean isRefresh) {
        if (isRefresh) {
            searchOverviews.clear();
        }
        searchOverviews.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<SearchOverview> getData() {
        return searchOverviews;
    }
}
