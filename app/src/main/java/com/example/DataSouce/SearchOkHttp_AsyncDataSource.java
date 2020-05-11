package com.example.DataSouce;

import com.example.Bean.SearchOverview;
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


public class SearchOkHttp_AsyncDataSource implements IAsyncDataSource<List<SearchOverview>> {
    private int mPage;
    private int mMaxPage = 1;
    private String infor = "";

    public SearchOkHttp_AsyncDataSource(String infor) {
        super();
        this.infor = infor;
    }

    @Override
    public RequestHandle refresh(ResponseSender<List<SearchOverview>> sender) throws Exception {
        return loadBooks(sender, 0);
    }

    @Override
    public RequestHandle loadMore(ResponseSender<List<SearchOverview>> sender) throws Exception {
        return loadBooks(sender, mPage + 1);
    }

    @Override
    public boolean hasMore() {
        return mPage < mMaxPage;
    }

    private RequestHandle loadBooks(final ResponseSender<List<SearchOverview>> sender, final int page) throws Exception {

        GetMethod method = new GetMethod("http://114.116.114.99:8080/api/report/search");
        //GetMethod method = new GetMethod("http://192.168.2.218:8080/login");
//        method.addParam("key", "e792a3c67684f368b09cba69c1f0e3fd");
        //method.addParam("size", "5");
        //method.addParam("page", page);
        method.addParam("search", infor);
        method.executeAsync(sender, new ResponseParser<List<SearchOverview>>() {
            @Override
            public List<SearchOverview> parse(Response response) throws Exception {
                //Thread.sleep(2000);
                //String result = Utils.fixJson(response.body().string());
                String result = response.body().string();
                //result = result.substring(1, result.length() - 1);
                System.out.println(result);

                JSONObject object = new JSONObject(result);
                JSONArray jsonArray = object.getJSONArray("matchedReports");
                List<SearchOverview> searchOverviews = new ArrayList<SearchOverview>();

                // todo 整个修改
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    searchOverviews.add(new SearchOverview(jsonObject.getString("title"), jsonObject.getString("content"), jsonObject.getInt("id")));
                }
                mPage = mMaxPage;
                System.out.println("搜索加载完成");
                return searchOverviews;
            }
        });

        return method;
    }
}
