

package com.example.utils;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.ColorInt;

import com.example.ruangong.R;

import java.text.ParseException;
import java.util.Date;


public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 是否是深色的颜色
     *
     * @param color
     * @return
     */
    public static boolean isColorDark(@ColorInt int color) {
        double darkness =
                1
                        - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color))
                        / 255;
        return darkness >= 0.382;
    }

    public static String getDate() {
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        }
        Date d = new Date();
        String str = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            str = sdf.format(d);
        }
        return str;
    }

    public static String getDate(String old_date) {
        SimpleDateFormat sdf = null;
        Date date = new Date();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ");
                date = sdf.parse(old_date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf2.format(date);
        }
        return null;
    }

    public static String change2md(String old_date) {
        String string = old_date.split(" ")[0];
        String month = string.split("-")[1];
        String day = string.split("-")[2];
        return month + "月" + day + "日";
    }

    public static void setSymbol(ImageView imageView, String team) {
        switch (team) {
            case "76人":
                imageView.setImageResource(R.mipmap.sevensixren);
                break;
            case "步行者":
                imageView.setImageResource(R.mipmap.buxingzhe);
                break;
            case "独行侠":
                imageView.setImageResource(R.mipmap.duxingxia);
                break;
            case "公牛":
                imageView.setImageResource(R.mipmap.gongniu);
                break;
            case "国王":
                imageView.setImageResource(R.mipmap.guowang);
                break;
            case "黄蜂":
                imageView.setImageResource(R.mipmap.huangfeng);
                break;
            case "灰熊":
                imageView.setImageResource(R.mipmap.huixiong);
                break;
            case "火箭":
                imageView.setImageResource(R.mipmap.huojian);
                break;
            case "活塞":
                imageView.setImageResource(R.mipmap.huosai);
                break;
            case "湖人":
                imageView.setImageResource(R.mipmap.huren);
                break;
            case "掘进":
                imageView.setImageResource(R.mipmap.juejin);
                break;
            case "爵士":
                imageView.setImageResource(R.mipmap.jueshi);
                break;
            case "凯尔特人":
                imageView.setImageResource(R.mipmap.kaierteren);
                break;
            case "开拓者":
                imageView.setImageResource(R.mipmap.kaituozhe);
                break;
            case "快船":
                imageView.setImageResource(R.mipmap.kuaichuan);
                break;
            case "篮网":
                imageView.setImageResource(R.mipmap.lanwang);
                break;
            case "老鹰":
                imageView.setImageResource(R.mipmap.laoying);
                break;
            case "雷霆":
                imageView.setImageResource(R.mipmap.leiting);
                break;
            case "马刺":
                imageView.setImageResource(R.mipmap.maci);
                break;
            case "猛龙":
                imageView.setImageResource(R.mipmap.menglong);
                break;
            case "魔术":
                imageView.setImageResource(R.mipmap.moshu);
                break;
            case "尼克斯":
                imageView.setImageResource(R.mipmap.nikesi);
                break;
            case "奇才":
                imageView.setImageResource(R.mipmap.qicai);
                break;
            case "骑士":
                imageView.setImageResource(R.mipmap.qishi);
                break;
            case "热火":
                imageView.setImageResource(R.mipmap.rehuo);
                break;
            case "森林狼":
                imageView.setImageResource(R.mipmap.senlinlang);
                break;
            case "太阳":
                imageView.setImageResource(R.mipmap.taiyang);
                break;
            case "鹈鹕":
                imageView.setImageResource(R.mipmap.tihu);
                break;
            case "雄鹿":
                imageView.setImageResource(R.mipmap.xionglu);
                break;
            case "勇士":
                imageView.setImageResource(R.mipmap.yongshi);
                break;
            default:
                imageView.setImageResource(R.mipmap.yongshi);
                break;
        }
    }

    public static String getRealName(String team_Name) {
        switch (team_Name) {
            case "多伦多猛龙":
                return "猛龙";
            case "洛杉矶快船":
                return "快船";
            case "萨克拉门托国王":
                return "国王";
            case "明尼苏达森林狼":
                return "森林狼";
            case "孟菲斯灰熊":
                return "灰熊";
            case "费城76人":
                return "76人";
            case "犹他爵士":
                return "爵士";
            case "俄克拉荷马城雷霆":
                return "雷霆";
            case "圣安东尼奥马刺":
                return "马刺";
            case "波特兰开拓者":
                return "开拓者";
            case "克利夫兰骑士":
                return "骑士";
            case "华盛顿奇才":
                return "奇才";
            case "亚特兰大老鹰":
                return "老鹰";
            case "新奥尔良鹈鹕":
                return "鹈鹕";
            case "丹佛掘金":
                return "掘金";
            case "布鲁克林篮网":
                return "篮网";
            case "夏洛特黄蜂":
                return "黄蜂";
            case "金州勇士":
                return "勇士";
            case "印第安纳步行者":
                return "步行者";
            case "奥兰多魔术":
                return "魔术";
            case "密尔沃基雄鹿":
                return "雄鹿";
            case "菲尼克斯太阳":
                return "太阳";
            case "达拉斯独行侠":
                return "独行侠";
            case "波士顿凯尔特人":
                return "凯尔特人";
            case "休斯顿火箭":
                return "火箭";
            case "芝加哥公牛":
                return "公牛";
            case "纽约尼克斯":
                return "尼克斯";
            case "洛杉矶湖人":
                return "湖人";
            case "底特律活塞":
                return "活塞";
            case "迈阿密热火":
                return "热火";
            default:
                return "梦之队?";
        }

    }

    public static String fixJson(String injson) {
        String outjson;
        outjson = injson.substring(1, injson.length() - 1).trim();
        return outjson;

    }


}
