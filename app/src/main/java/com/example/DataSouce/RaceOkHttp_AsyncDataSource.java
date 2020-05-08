package com.example.DataSouce;

import com.example.Bean.RaceOverview;
import com.example.utils.Utils;
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

/**
 * 这是封装OKHttp请求的演示代码
 */
public class RaceOkHttp_AsyncDataSource implements IAsyncDataSource<List<RaceOverview>> {
    private int mPage;
    private int mMaxPage = 100;

    @Override
    public RequestHandle refresh(ResponseSender<List<RaceOverview>> sender) throws Exception {
        return loadBooks(sender, 0);
    }

    @Override
    public RequestHandle loadMore(ResponseSender<List<RaceOverview>> sender) throws Exception {
        return loadBooks(sender, mPage + 1);
    }

    @Override
    public boolean hasMore() {
        return mPage < mMaxPage;
    }

    private RequestHandle loadBooks(final ResponseSender<List<RaceOverview>> sender, final int page) throws Exception {
        GetMethod method = new GetMethod("http://114.116.114.99:8080/api/summaries");
        //GetMethod method = new GetMethod("http://192.168.2.218:8080/login");
//        method.addParam("key", "e792a3c67684f368b09cba69c1f0e3fd");
        method.addParam("size", "5");
        method.addParam("page", page);
        method.executeAsync(sender, new ResponseParser<List<RaceOverview>>() {
            @Override
            public List<RaceOverview> parse(Response response) throws Exception {
                //Thread.sleep(2000);
                //String result = Utils.fixJson(response.body().string());
                String result = response.body().string();
                System.out.println(result);
                JSONObject object = new JSONObject(result);
                JSONArray jsonArray = object.getJSONArray("gameSummaries");
                List<RaceOverview> raceOverviews = new ArrayList<RaceOverview>();
                if (jsonArray.toString().equals("[]")) {
                    mPage = mMaxPage;
                    return raceOverviews;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (!jsonObject.getBoolean("finish")) {
                        raceOverviews.add(new RaceOverview(jsonObject.getInt("match_id"), Utils.getDate(jsonObject.getString("date")),
                                jsonObject.getString("team1"), jsonObject.getString("team2"),
                                jsonObject.getInt("score1"), jsonObject.getInt("score2")));
                    } else {
                        raceOverviews.add(new RaceOverview(jsonObject.getInt("match_id"), Utils.getDate(jsonObject.getString("date")),
                                jsonObject.getString("team1"), jsonObject.getString("team2")));
                    }
                }
                //raceOverviews.add(new RaceOverview(object.getString("time"), object.getString("team1"), object.getString("team2"), object.getInt("score1"), object.getInt("score2")));
                mPage = page;
                System.out.println("加载完成");
                return raceOverviews;
            }
        });

        return method;
    }
}
