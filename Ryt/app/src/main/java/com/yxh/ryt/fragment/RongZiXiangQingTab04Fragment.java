package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ArtworkInvest;
import com.yxh.ryt.vo.RongZi;
import com.yxh.ryt.vo.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;


/**
 * Created by sj on 15/11/25.
 */
public class RongZiXiangQingTab04Fragment extends StickHeaderBaseFragment {
    private ListView mListview;
    private CommonAdapter<ArtworkInvest> investorRecordCommonAdapter;
    private List<ArtworkInvest> investorDatas;
    private int currentPage=1;
    private View footer;
    private TextView loadFull;
    private TextView noData;
    private TextView more;
    private ProgressBar loading;
    private int lastItem;
    public RongZiXiangQingTab04Fragment(StickHeaderViewPagerManager manager, int position) {
        super(manager, position);
    }

    public RongZiXiangQingTab04Fragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        super(manager, position, isCanPulltoRefresh);
    }

    public static RongZiXiangQingTab04Fragment newInstance(StickHeaderViewPagerManager manager, int position) {
        RongZiXiangQingTab04Fragment listFragment = new RongZiXiangQingTab04Fragment(manager, position);
        return listFragment;
    }

    public static RongZiXiangQingTab04Fragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        RongZiXiangQingTab04Fragment listFragment = new RongZiXiangQingTab04Fragment(manager, position, isCanPulltoRefresh);
        return listFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        investorDatas=new ArrayList<>();
    }

    @Override
    public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        mListview = (ListView)view.findViewById(R.id.v_scroll);
        footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
        setAdapter();
        return view;
    }

    private void setAdapter() {
        investorRecordCommonAdapter=new CommonAdapter<ArtworkInvest>(getActivity(),investorDatas,R.layout.investorrecord_item) {
            @Override
            public void convert(ViewHolder helper, ArtworkInvest item) {
                helper.getView(R.id.iri_ve_line).setVisibility(View.GONE);
                if (helper.getPosition()==2){
                    helper.getView(R.id.iri_ve_line).setVisibility(View.VISIBLE);
                }
                helper.setText(R.id.iri_tv_nickname,item.getCreator().getName()+"--"+helper.getPosition());
                helper.setText(R.id.iri_tv_content,"投资了"+item.getPrice()+"元");
                helper.setImageByUrl(R.id.iri_iv_icon,item.getCreator().getPictureUrl());
            }
        };
        mListview.setAdapter(investorRecordCommonAdapter);
        mListview.addFooterView(footer);
        loadFull = (TextView) footer.findViewById(R.id.loadFull);
        noData = (TextView) footer.findViewById(R.id.noData);
        more = (TextView) footer.findViewById(R.id.more);
        loading = (ProgressBar) footer.findViewById(R.id.loading);
        more.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        loadFull.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
                // TODO Auto-generated method stub
            }

            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                if (lastItem == investorRecordCommonAdapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    more.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loadFull.setVisibility(View.GONE);
                    noData.setVisibility(View.GONE);
                    LoadData(false,currentPage+1);
                }
            }
        });
        LoadData(true,currentPage);
    }


    private void LoadData(final boolean flag,int pageNum) {
        more.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loadFull.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId","qydeyugqqiugd2");
        paramsMap.put("pageSize", 2+"");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkInvest.do", paramsMap, new RZCommentCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Log.d("onResponse(Map<String, Object",response.toString());
                if ("0".equals(response.get("resultCode"))){
                    Map<String,Object> object= (Map<String, Object>) response.get("object");
                    if (flag){
                        List<ArtworkInvest> topList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestTopList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        if (topList==null){
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }else {
                            investorDatas.addAll(topList);
                            investorRecordCommonAdapter.notifyDataSetChanged();
                        }
                    }
                    List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                    }.getType());
                    if (investList ==null|| investList.size()<2 ){
                        more.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        loadFull.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                    }else {
                        more.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        loadFull.setVisibility(View.GONE);
                        noData.setVisibility(View.GONE);
                    }
                    investorDatas.addAll(investList);
                    investorRecordCommonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {
    }
}
