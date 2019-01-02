package com.lmq.tool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newbrainapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/12/25 0025.
 */

public class LmqTool {
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz)

    {
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
            ArrayList<T> arrayList = new ArrayList<T>();
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
            return arrayList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasNetWork(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static boolean isIdCard(String idcard){
        Pattern p=Pattern.compile("^(\\d{15}|\\d{17}[\\dx])$");
        Matcher m=p.matcher(idcard);
        if(!m.matches()){

            return false;
        }
        int w[] = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
        int sum = 0;//保存级数和
        for(int i = 0; i <idcard.length() -1 ; i++){
            sum += new Integer(idcard.substring(i,i+1)) * w[i];
        }
        String sums[] = {"1","0","x","9","8","7","6","5","4","3","2"};
        if (sums[(sum%11)].equals(idcard.substring(idcard.length()-1,idcard.length()))) {//与身份证最后一位比较
        }else {

            return false;

        }
        return true;
    }
    /**
     * 检测邮箱地址是否合法
     * @param email
     * @return true合法 false不合法
     */  //
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
//	    Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
    public static boolean isPhone(String phone){
        // Pattern pattern = Pattern.compile("^13/d{9}||15[8,9]/d{8}$");"^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        //  Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern pattern = Pattern.compile("^(13|14|15|16|17|18|19)\\d{9}$");
        Matcher matcher = pattern.matcher(phone);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    /**
     * SD卡是否存在
     */
    public static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    public static Dialog createCustomDialog(Context context,
                                            List<DialogItem> items, int style) {
        LinearLayout dialogView = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.custom_dialog_layout, null);
        final Dialog customDialog = new Dialog(context,style);
        LinearLayout itemView;
        //ImageView sp;
        TextView textView;

        for (int i=0;i< items.size();i++) {

            itemView = (LinearLayout) LayoutInflater.from(context).inflate(
                    items.get(i).getViewId(), null);
            textView = (TextView) itemView.findViewById(R.id.popup_text);
            if(items.get(i).getmText().length()>13)
                textView.setTextSize(12);
            else
                textView.setTextSize(18);
            textView.setText(items.get(i).getmText());
            textView.setOnClickListener(new OnItemClick(items.get(i), customDialog));
            dialogView.addView(itemView);
            if(i<items.size()-1){
                //添加分隔线
                ImageView sp=new ImageView(context);
                sp.setBackgroundResource(R.drawable.custom_dialog_sp);
                dialogView.addView(sp);
            }
        }

        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        localLayoutParams.x = 0;
        localLayoutParams.y = -1000;
        localLayoutParams.gravity = 80;
        dialogView.setMinimumWidth(10000);

        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        customDialog.setContentView(dialogView);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                customDialog.show();
            }
        }

        return customDialog;
    }
    //多次点击
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
