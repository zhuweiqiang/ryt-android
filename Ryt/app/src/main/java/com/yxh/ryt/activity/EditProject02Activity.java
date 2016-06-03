package com.yxh.ryt.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.custemview.CustomGridView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.GetImageTask;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.util.phote.util.Bimp;
import com.yxh.ryt.util.phote.util.ImageItem;
import com.yxh.ryt.util.phote.util.PublicWay;
import com.yxh.ryt.util.phote.util.Res;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.Image;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/11.
 */
public class EditProject02Activity extends  BaseActivity {
    public final int REQUEST_IMAGE=0;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    Map<String,File> fileMap=new HashMap<>();
    public static Bitmap bimap ;
    String artworkId="";
    @Bind(R.id.tv_done)
    TextView tvDone;
    @Bind(R.id.ev_shuoming)
    EditText evShuoming;
    @Bind(R.id.ev_zhizuo_shuoming)
    EditText evZhizuo;
    @Bind(R.id.ev_jiehuo)
    EditText evJieHuo;
    List<String> ImageList;
    //艺术家发布项目第一步接口一网络请求
    private void twoStepRequst() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artworkId",artworkId);
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramsMap.put("description",evShuoming.getText().toString());
        paramsMap.put("make_instru",evZhizuo.getText().toString());
        paramsMap.put("financing_aq",evJieHuo.getText().toString());
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        System.out.println(paramsMap.toString());
        NetRequestUtil.postFile(Constants.BASE_PATH + "initNewArtWork2.do", "file", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println("成功了");
                Log.d("XXXXXXXXXXXXXXXXXXXXX", "YYYYYYYYYYY");
                Log.d("tagonResponse", response.toString());
            }
        });
    }
    @OnClick(R.id.tv_done)
    public void doneClick(View v){
        twoStepRequst();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Res.init(this);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.icon_addpic_unfocused);
        PublicWay.activityList.add(this);
        setContentView(R.layout.edit_project_02);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        /*artworkId = getIntent().getCharSequenceExtra("artworkId").toString();*/
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Intent intent = new Intent(AppApplication.getSingleContext(), MultiImageSelectorActivity.class);
                    // 是否显示调用相机拍照
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                    if (Bimp.tempSelectBitmap.size() == 0) {
                        // 最大图片选择数量
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                    } else {
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9 - Bimp.tempSelectBitmap.size());
                    }
                    // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                    startActivityForResult(intent, REQUEST_IMAGE);
                } else {
                    Intent intent = new Intent(EditProject02Activity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        /*loadData();*/
    }

    private void loadData() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId", "imyt7yax314lpzzj");
        paramsMap.put("currentUserId", "imhfp1yr4636pj49");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg=EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkView.do", paramsMap, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                ImageList = new ArrayList<String>();
                if ("0".equals(response.get("resultCode"))) {
                    Map<String, Object> object = (Map<String, Object>) response.get("object");
                    List<Image> list = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkAttachmentList")), new TypeToken<List<Image>>() {
                    }.getType());
                    if (list.size() > 9) {
                        for (int i = 0; i < 9; i++) {
                            ImageList.add(list.get(i).getFileName());
                        }
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            ImageList.add(list.get(i).getFileName());
                        }
                    }
                    Bimp.tempSelectBitmap = new ArrayList<ImageItem>();
                    for (int i = 0; i < ImageList.size(); i++) {
                        /*System.out.print(Bimp.tempSelectBitmap.size() + "XXXXXXXXXXXXXXXXXX");*/
                        GetImageTask imageTask = new GetImageTask(new GetImageTask.ImageCallBack() {
                            @Override
                            public void getBitmapAndLc(Bitmap result, int location) {
                                if (result != null) {
                                    File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator + "image" + File.separator);
                                    if (!sampleDir.exists()) {
                                        sampleDir.mkdirs();
                                    }
                                    if (!sampleDir.isDirectory()) {
                                        sampleDir.delete();
                                        sampleDir.mkdirs();
                                    }
                                    File mRecordFile = null;
                                    try {
                                        Log.d("hhhhhhhhhhhhhhhhhhhhh",ImageList.get(location));
                                        mRecordFile = File.createTempFile("" + System.currentTimeMillis(), Utils.getImageFormat(ImageList.get(location)), sampleDir); //mp4格式
                                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mRecordFile));
                                        result.compress(Utils.getImageFormatBig(ImageList.get(location)), 100, bos);
                                        bos.flush();
                                        bos.close();
                                        ImageItem imageItem = new ImageItem();
                                        imageItem.setImagePath(mRecordFile.getAbsolutePath());
                                        Bimp.tempSelectBitmap.add(imageItem);
                                        adapter.notifyDataSetChanged();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },i);
                        imageTask.execute(ImageList.get(i));
                    }
                }
            }
        });
    }

    public class GridAdapter extends BaseAdapter {
            private LayoutInflater inflater;
            private int selectedPosition = -1;
            private boolean shape;

            public boolean isShape() {
                return shape;
            }

            public void setShape(boolean shape) {
                this.shape = shape;
            }

            public GridAdapter(Context context) {
                inflater = LayoutInflater.from(context);
            }

            public void update() {
                loading();
            }

            public int getCount() {
                if(Bimp.tempSelectBitmap.size() == 9){
                    return 9;
                }
                return (Bimp.tempSelectBitmap.size() + 1);
            }

            public Object getItem(int arg0) {
                return null;
            }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Utils.setListViewHeightBasedOnChildren(noScrollgridview);
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
        }

        protected void onRestart() {
            adapter.update();
            super.onRestart();
        }
        private static final int TAKE_PICTURE = 0x000001;


        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK&&Bimp.tempSelectBitmap.size()<9){
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                for (String s:path){
                    File file = new File(s);
                    System.out.println(file.getName()+"=================");
                    fileMap.put(file.getName(), file);
                    String fileName = String.valueOf(System.currentTimeMillis());
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(null);
                    takePhoto.setImagePath(s);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
            }
        }
        }

    }
