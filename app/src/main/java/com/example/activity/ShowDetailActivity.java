package com.example.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bin.david.form.core.SmartTable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.Bean.CommentBean;
import com.example.Bean.CommentDetailBean;
import com.example.Bean.MatchDetail;
import com.example.Bean.ReplyDetailBean;
import com.example.CommentExpandableListView;
import com.example.adapter.CommentExpandAdapter;
import com.example.ruangong.R;
import com.example.utils.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * by moos on 2018/04/20
 */
public class ShowDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ShowDetailActivity";
    private Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private SmartTable smartTable;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;
    private String testJson = "";
    private MatchDetail matchDetail;
    private String matchJson = "";
    private int match_id;
    private TextView match_title;
    private TextView match_content;
    private ImageView match_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhanbao);
        match_id = getIntent().getIntExtra("match_id", 0);
        String path = " http://114.116.114.99:8080/api/report/" + match_id;
        new ShowMatchTask().execute(path);
        path = " http://114.116.114.99:8080/api/comments/" + match_id;
        new ShowCommentTask().execute(path);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        //smartTable = findViewById(R.id.table);
        bt_comment.setOnClickListener(this);
        setSupportActionBar(toolbar);
        //toolbar.setTitle("详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbar.setTitle("详情");
    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList) {
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        //System.out.println("??????????????:" + commentList.size());
        for (int i = 0; i < commentList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                //点击打开、关闭回复列表
//                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
//                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition, -1);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                //Toast.makeText(ShowDetailActivity.this, "点击了回复", Toast.LENGTH_SHORT).show();
                showReplyDialog(groupPosition, childPosition);
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }

    /**
     * by moos on 2018/04/20
     * func:生成测试数据
     *
     * @return 评论数据
     */
    private List<CommentDetailBean> generateTestData() {
        Gson gson = new Gson();
        System.out.println(testJson);
        commentBean = gson.fromJson(testJson, CommentBean.class);
        //System.out.println(commentBean.getList().get(1).getUser_name());
        return commentBean.getList();
    }

    private void generateMatchData() {
        Gson gson = new Gson();
        System.out.println(matchJson);
        matchDetail = gson.fromJson(matchJson, MatchDetail.class);
        //System.out.println(commentBean.getList().get(1).getUser_name());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.detail_page_do_comment) {

            showCommentDialog();
        }
    }

    /**
     * by moos on 2018/04/20
     * func:弹出评论框
     */
    private void showCommentDialog() {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0, 0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(commentContent)) {

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    String date = Utils.getDate();
                    CommentDetailBean detailBean = new CommentDetailBean(LoginActivity.user_name, commentContent, date, ChangePicActivity.img_url);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_id", LoginActivity.user_id);
                        jsonObject.put("match_id", match_id);
                        jsonObject.put("hf_id", 0);
                        jsonObject.put("date", date);
                        jsonObject.put("content", commentContent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String path = "http://114.116.114.99:8080/api/comments";
                    new CommitTask().execute(path, jsonObject);

                    adapter.addTheCommentData(detailBean);
                    //     Toast.makeText(ShowDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ShowDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() >= 1) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * by moos on 2018/04/20
     * func:弹出回复框
     */
    private void showReplyDialog(final int position, final int childPosition) {
        String tishi;
        int hf_id;
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        if (childPosition != -1) {
            tishi = "回复 " + commentsList.get(position).getList().get(childPosition).getUser_name() + " :";
            hf_id = commentsList.get(position).getLy_id();
        } else {
            tishi = "回复 " + commentsList.get(position).getUser_name() + " :";
            hf_id = commentsList.get(position).getLy_id();
        }
        commentText.setHint(tishi);
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(replyContent)) {
                    String date = Utils.getDate();
                    if (childPosition != -1) {
                        replyContent = tishi + replyContent;
                    }
                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean(LoginActivity.user_name, replyContent);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_id", LoginActivity.user_id);
                        jsonObject.put("match_id", match_id);
                        jsonObject.put("hf_id", hf_id);
                        //System.out.println("我是hf_id:" + hf_id);
                        jsonObject.put("date", date);
                        jsonObject.put("content", replyContent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String path = "http://114.116.114.99:8080/api/comments";
                    new CommitTask().execute(path, jsonObject);

                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
//                    Toast.makeText(ShowDetailActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShowDetailActivity.this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() >= 1) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }


    class ShowCommentTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String path = objects[0].toString();
            Request request = new Request.Builder().url(path).get().build();
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
            testJson = result;
            commentsList = generateTestData();
            initExpandableListView(commentsList);
        }
    }

    class ShowMatchTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String path = objects[0].toString();
            Request request = new Request.Builder().url(path).get().build();
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
            System.out.println(result);
            matchJson = result;
            generateMatchData();
            match_pic = findViewById(R.id.match_pic);
            match_title = findViewById(R.id.match_title);
            match_content = findViewById(R.id.match_content);
            match_title.setText(matchDetail.getTitle());
            match_content.setText(matchDetail.getContent());
            System.out.println(matchDetail.getTitle());
            Glide.with(ShowDetailActivity.this).load(matchDetail.getPicture())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(match_pic);
        }
    }

    class CommitTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String path = objects[0].toString();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, objects[1].toString());
            Request request = new Request.Builder().url(path).addHeader("Authorization", LoginActivity.id_token).post(requestBody).build();
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
            System.out.println(result);
            if (result.contains("success")) {
                Toast.makeText(ShowDetailActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ShowDetailActivity.this, "评论失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
