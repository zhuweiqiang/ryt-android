package com.yxh.ryt.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.AttentionIndicatorAdapter;
import com.yxh.ryt.fragment.AttentionArtItemFragment;
import com.yxh.ryt.fragment.AttentionUserItemFragment;
import com.yxh.ryt.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/4/25.
 */
public class AttentionActivity extends BaseActivity {
    List<BaseFragment> indexChildFragments=new ArrayList<>();
    FragmentPagerAdapter indexChildAdapter;
    @Bind(R.id.attention_pager)
    ViewPager pager;
    @Bind(R.id.attention_indicator)
    TabPageIndicator indicator;
    private AttentionReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention);
        ButterKnife.bind(this);/*启用注解绑定*/
        indexChildFragments.add(new AttentionArtItemFragment());
        indexChildFragments.add(new AttentionUserItemFragment());
        indexChildAdapter = new AttentionIndicatorAdapter(getSupportFragmentManager(),indexChildFragments);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(indexChildAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        indicator.setViewPager(pager);
        receiver = new AttentionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MY_BROADCAST");
        registerReceiver(receiver, filter);
    }
    @OnClick(R.id.attention_ib_top)
    public void back(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public class AttentionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("=======================");
            Log.d("dddddddddd", indexChildAdapter.getPageTitle(0)+"")
            ;
            System.out.println("================5555=======");
        }
    }
}
