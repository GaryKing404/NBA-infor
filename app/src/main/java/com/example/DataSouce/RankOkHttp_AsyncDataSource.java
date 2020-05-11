package com.example.DataSouce;

import com.example.Bean.RankTeam;
import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.mvc.http.okhttp.GetMethod;
import com.shizhefei.mvc.http.okhttp.ResponseParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class RankOkHttp_AsyncDataSource implements IAsyncDataSource<List<RankTeam>> {
    private int mPage;
    private int mMaxPage = 0;

    @Override
    public RequestHandle refresh(ResponseSender<List<RankTeam>> sender) throws Exception {
        return loadBooks(sender, 0);
    }

    @Override
    public RequestHandle loadMore(ResponseSender<List<RankTeam>> sender) throws Exception {
        return loadBooks(sender, mPage + 1);
    }

    @Override
    public boolean hasMore() {
        return mPage < mMaxPage;
    }

    private RequestHandle loadBooks(final ResponseSender<List<RankTeam>> sender, final int page) throws Exception {
        GetMethod method = new GetMethod("http://114.116.114.99:8080/api/ranks");
//        method.addParam("key", "e792a3c67684f368b09cba69c1f0e3fd");
        //method.addParam("size", "1");
        //method.addParam("page", page);
        method.executeAsync(sender, new ResponseParser<List<RankTeam>>() {
            @Override
            public List<RankTeam> parse(Response response) throws Exception {
                //Thread.sleep(2000);
                List<RankTeam> rankTeams = new ArrayList<>();
                //String result = Utils.fixJson(response.body().string());
                String result = response.body().string();
                //System.out.println(result);
                JSONObject object = new JSONObject(result);
                JSONArray jsonArray = object.getJSONArray("teamRanks");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    rankTeams.add(new RankTeam(jsonObject.getInt("teamRank"), jsonObject.getString("team"),
                            jsonObject.getInt("winNumber"), jsonObject.getInt("loseNumber"), jsonObject.getInt("rate")));
                }
                //raceOverviews.add(new RaceOverview(object.getString("time"), object.getString("team1"), object.getString("team2"), object.getInt("score1"), object.getInt("score2")));
                //rankTeams.add(new RankTeam(1, "火箭", 10, 4, 74));
                mPage = page;
                System.out.println("加载完成");
                return rankTeams;
            }
        });

        return method;
    }
}
