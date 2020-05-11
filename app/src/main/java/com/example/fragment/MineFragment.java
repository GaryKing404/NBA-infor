package com.example.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.activity.ChangeInforActivity;
import com.example.activity.ChangePicActivity;
import com.example.activity.LoginActivity;
import com.example.ruangong.R;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

public class MineFragment extends Fragment implements SuperTextView.OnSuperTextViewClickListener {

    private RadiusImageView rivHeadPic;
    private SuperTextView menuAbout;
    private SuperTextView memuAccount;
    private SuperTextView memuLogout;
    private SuperTextView memuPic;
    private SuperTextView memuTeam;
    private TextView memuName;
    //SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mineFragment = inflater.inflate(R.layout.mine_fragment, container, false);
        rivHeadPic = mineFragment.findViewById(R.id.riv_head_pic);
        if (ChangePicActivity.img_url != null) {
            //rivHeadPic.setImageBitmap(ChangePicActivity.user_pic);
            Glide.with(this).load(ChangePicActivity.img_url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(rivHeadPic);
        }
        menuAbout = mineFragment.findViewById(R.id.menu_about);
        memuAccount = mineFragment.findViewById(R.id.my_account);
        memuLogout = mineFragment.findViewById(R.id.menu_logout);
        memuPic = mineFragment.findViewById(R.id.my_pic);
        memuName = mineFragment.findViewById(R.id.my_name1);
        memuTeam = mineFragment.findViewById(R.id.my_team);
        memuName.setText(LoginActivity.user_name);
        menuAbout.setOnSuperTextViewClickListener(this);
        memuAccount.setOnSuperTextViewClickListener(this);
        memuLogout.setOnSuperTextViewClickListener(this);
        memuPic.setOnSuperTextViewClickListener(this);
        memuTeam.setOnSuperTextViewClickListener(this);
        return mineFragment;
    }

    @Override
    public void onClick(SuperTextView view) {
        switch (view.getId()) {
            case R.id.my_pic:
                Intent intent = new Intent(getActivity(), ChangePicActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_about:
                //openNewPage(AboutFragment.class);
                Toast.makeText(getActivity(), "点击了关于", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_logout:
                LoginActivity.id_token = "";
                LoginActivity.user_name = "游客";
                LoginActivity.user_id = 0;
                SharedPreferences.Editor editor = LoginActivity.sharedPreferences.edit();
                editor.clear();
                editor.commit();
                ChangePicActivity.img_url = null;
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.my_account:
                //Toast.makeText(getActivity(), "点击了账号", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), ChangeInforActivity.class);
                startActivity(intent);
                break;
            case R.id.my_team:
                break;
            default:
                break;
        }
    }
}
