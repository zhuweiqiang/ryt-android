package com.yxh.ryt.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.AuctionOrder;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class AuctionOrderConfirmActivity extends BaseActivity{
    @Bind(R.id.aass_tv_number)
    TextView number;
    @Bind(R.id.aass_tv_addressName)
    TextView addressName;
    @Bind(R.id.aass_tv_addressPhone)
    TextView addressPhone;
    @Bind(R.id.aass_tv_addressDetail)
    TextView addressDetail;
    @Bind(R.id.foa_iv_projectPicture)
    ImageView projectPicture;
    @Bind(R.id.foa_iv_projectTitle)
    TextView projectTitle;
    @Bind(R.id.foa_iv_projectState)
    TextView projectState;
    @Bind(R.id.foa_iv_projectBrief)
    TextView projectBrief;
    @Bind(R.id.foa_tv_projectPrice)
    TextView projectPrice;
    @Bind(R.id.aass_tv_marginMoney)
    TextView marginMoney;
    @Bind(R.id.aass_tv_auctionMoney)
    TextView auctionMoney;
    @Bind(R.id.aass_tv_realMoney)
    TextView realMoney;
    private AuctionOrder auctionOrder;
    @Bind(R.id.aass_tv_edit)
    TextView edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctionordersummaryconfirm);
        ButterKnife.bind(this);
        auctionOrder=null;
        if (getIntent()!=null){
            auctionOrder= (AuctionOrder) getIntent().getSerializableExtra("data");
        }
        initData();
    }

    private void initData() {
        number.setText(auctionOrder.getId());
        addressName.setText(auctionOrder.getUser().getName());
        addressPhone.setText(auctionOrder.getConsumerAddress().getPhone()+"");
        addressDetail.setText(auctionOrder.getConsumerAddress().getDetails());
        AppApplication.displayImage(auctionOrder.getArtwork().getPicture_url(),projectPicture);
        projectTitle.setText(auctionOrder.getArtwork().getTitle());
        projectState.setText("待收货");
        projectState.setBackgroundColor(Color.rgb(33,133,208));
        if (auctionOrder.getArtwork().getBrief()==null || "".equals(auctionOrder.getArtwork().getBrief())){
            projectBrief.setText("暂无简介");
        }else {
            projectBrief.setText(auctionOrder.getArtwork().getBrief());
        }
        edit.setText("确认收货");
        marginMoney.setText("￥"+auctionOrder.getAmount().longValue()*0.1+"");
        projectPrice.setText("￥"+auctionOrder.getAmount()+"");
        auctionMoney.setText("￥"+auctionOrder.getAmount()+"");
        realMoney.setText("￥"+auctionOrder.getFinalPayment()+"");
    }
    private void confirmReceiveGoods(final String id) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("auctionOrderId", id);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "confirmReceipt.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    ToastUtil.showShort(AuctionOrderConfirmActivity.this,"确认收货！");
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                confirmReceiveGoods(id);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    //计算加价幅度
    private int getAuctionPrice(long price) {
        //计算加价幅度
        if (price <= 499) {
            return 10;
        } else if (price >= 500 && price <= 999) {
            return 50;
        } else if (price >= 1000 && price <= 4999) {
            return 100;
        } else if (price >= 5000 && price <= 9999) {
            return 200;
        } else if (price >= 10000 && price <= 29999) {
            return 500;
        } else if (price >= 30000 && price <= 99999) {
            return 1000;
        } else if (price >= 100000) {
            return 2000;
        }
        return 0;
    }
    @OnClick({R.id.aass_tv_edit,R.id.aaos_ib_back})
    public void back(View view){
        switch (view.getId()){
            case R.id.aaos_ib_back:
                finish();
                break;
            case R.id.aass_tv_edit:
                confirmReceiveGoods(auctionOrder.getId());
                break;
        }
    }


}
