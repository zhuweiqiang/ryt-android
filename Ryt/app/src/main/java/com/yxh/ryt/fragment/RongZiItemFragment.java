package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;
import com.yxh.ryt.vo.RongZi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class RongZiItemFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener {
	private AutoListView lstv;
	private CommonAdapter<RongZi> rongZiCommonAdapter;
	private List<RongZi> rongZiDatas;
	private int currentPage=1;
	private int pageSize=5;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rongZiDatas=new ArrayList<RongZi>();
	}


	@Override
	protected void lazyLoad() {
		LoadData(AutoListView.REFRESH,currentPage);
	}

	private void LoadData(final int state,int pageNum) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("pageSize",pageSize+"");
		paramsMap.put("pageNum", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "investorIndex.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if (state==AutoListView.REFRESH){
					lstv.onRefreshComplete();
					rongZiDatas.clear();
					List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<RongZi>>() {
					}.getType());
					if(null==objectList||objectList.size()==0){
						lstv.setResultSize(0);
					}
					if (null!=objectList&&objectList.size()>0){
						rongZiDatas.addAll(objectList);
						rongZiCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
				if (state==AutoListView.LOAD){
					lstv.onLoadComplete();
					List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<RongZi>>() {
					}.getType());
					if(null==objectList||objectList.size()==0){
						lstv.setResultSize(0);
					}
					if (null!=objectList&&objectList.size()>0) {
						rongZiDatas.addAll(objectList);
						rongZiCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_item, container, false);
		lstv = (AutoListView) contextView.findViewById(R.id.lstv);
		rongZiCommonAdapter=new CommonAdapter<RongZi>(AppApplication.getSingleContext(),rongZiDatas,R.layout.finance_list_item) {
			@Override
			public void convert(ViewHolder helper, RongZi item) {
				helper.setText(R.id.cl_01_tv_title,item.getTitle());
				helper.setText(R.id.cl_01_tv_brief,item.getBrief());
				helper.setImageByUrl(R.id.cl_01_tv_prc,item.getPicture_url());
			}
		};
		lstv.setAdapter(rongZiCommonAdapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		return contextView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onRefresh() {
		currentPage=1;
		LoadData(AutoListView.REFRESH,currentPage);
	}

	@Override
	public void onLoad() {
		System.out.println(currentPage+"=============");
		LoadData(AutoListView.LOAD,currentPage);
		currentPage++;
	}

}