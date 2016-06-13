package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.Bill;
import com.yxh.ryt.vo.FollowUserUtil;
import com.yxh.ryt.vo.UserMoney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 *
 */
public class UserQianBaoActivity extends BaseActivity {
    @Bind(R.id.ib_top_lf)
    ImageView ibTopLf;
    @Bind(R.id.btn_tx)
    Button btnTx;
    @Bind(R.id.uqb_listView)
    ListView listView;
    private List<Bill> bills;
    private CommonAdapter<Bill> billCommonAdapter;
    private View footer;
    private View header;
    private TextView reward;
    private TextView invest;
    private TextView rest;
    private LinearLayout details;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserQianBaoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_qianbao);
        ButterKnife.bind(this);
        footer = LayoutInflater.from(this).inflate(R.layout.auction_footview, null);
        header = LayoutInflater.from(this).inflate(R.layout.auction_header, null);
        listView.addFooterView(footer);
        listView.addHeaderView(header);
        reward = ((TextView) header.findViewById(R.id.ah_rewardMoney));
        invest = ((TextView) header.findViewById(R.id.ah_investMoney));
        rest = ((TextView) header.findViewById(R.id.ah_restMoney));
        details = ((LinearLayout) footer.findViewById(R.id.af_allDetails));
        bills=new ArrayList<Bill>();
        billCommonAdapter=new CommonAdapter<Bill>(AppApplication.getSingleContext(),bills,R.layout.item_wallet) {
            @Override
            public void convert(ViewHolder helper, Bill item) {

            }
        };
        listView.setAdapter(billCommonAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId",AppApplication.gUser.getId());
        paramsMap.put("pageSize",Constants.pageSize+"");
        paramsMap.put("pageIndex", "1");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "transactionRecord.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                UserMoney userMoney = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("object")), UserMoney.class);
                if (userMoney!=null){
                    invest.setText("￥ "+userMoney.getInvestMoney());
                    rest.setText(userMoney.getRestMoney()+"元");
                    reward.setText(userMoney.getRewardMoney()+"元");
                    if (userMoney.getBillList().size()<=5){
                        bills.addAll(userMoney.getBillList());
                    }else {
                        for (int i=0;i<5;i++){
                            bills.add(userMoney.getBillList().get(i));
                        }
                    }
                    billCommonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick({ R.id.btn_tx})
    void btnClick(View v) {
        switch (v.getId()) {
            /*case R.id.btn_cz:
                UserChongZhiActivity.openActivity(this);
                break;*/
            case R.id.btn_tx:
                break;
        }
    }

    @OnClick(R.id.ib_top_lf)
    public void back1() {
        finish();
    }

    @OnClick(R.id.ib_top_rt)
    public void back2() {
        finish();
    }
}