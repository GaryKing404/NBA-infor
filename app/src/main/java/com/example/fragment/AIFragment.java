package com.example.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.activity.AiDetailActivity;
import com.example.ruangong.R;
import com.xuexiang.xui.widget.button.ButtonView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AIFragment extends Fragment {

    private Spinner spinner_zhu;
    private Spinner spinner_ke;
    private ButtonView buttonView;
    private String team1;
    private String team2;
    private String path = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View aiFragment = inflater.inflate(R.layout.ai_fragment, container, false);
        spinner_zhu = aiFragment.findViewById(R.id.spinner_zhu);
        spinner_ke = aiFragment.findViewById(R.id.spinner_ke);
        buttonView = aiFragment.findViewById(R.id.btn_aiyc);
        setOnlistener();
        return aiFragment;
    }

    private void setOnlistener() {
        spinner_zhu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] languages = getResources().getStringArray(R.array.spinner_values);
                team1 = languages[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        spinner_ke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] languages = getResources().getStringArray(R.array.spinner_values);
                team2 = languages[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team1.equals(team2)) {
                    Toast.makeText(getActivity(), "自己不能跟自己对战哦！", Toast.LENGTH_SHORT).show();
                } else {
                    new aiyuceTask().execute();
                }
            }
        });
    }

    class aiyuceTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            String result = null;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("team1", team1);
                jsonObject.put("team2", team2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());
            final Request request = new Request.Builder().url(path).post(requestBody).build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                result = response.body().string();
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


        protected void onPostExecute(Object o) {
            Intent intent = new Intent(getActivity(), AiDetailActivity.class);
            //todo 给intent添加数据
            startActivity(intent);
        }
    }
}
