package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class EditSignActivity extends Activity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_save;
    private EditText nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sign);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        nickName = (EditText) findViewById(R.id.et_nickName);
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("userId", AppApplication.gUser.getId());
                paramsMap.put("type", "13");
                paramsMap.put("content", nickName.getText().toString());
                paramsMap.put("timestamp", System.currentTimeMillis() + "");
                try {
                    paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NetRequestUtil.post(Constants.BASE_PATH + " editProfile.do", paramsMap, new RegisterCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Map<String, Object> response) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.EDIT_SIGN_BROADCAST");
                        intent.putExtra("signer", nickName.getText().toString());
                        EditSignActivity.this.sendBroadcast(intent);
                    }
                });
                finish();
                break;
            default:
                break;
        }
    }

}