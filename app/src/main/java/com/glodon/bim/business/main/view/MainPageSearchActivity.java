package com.glodon.bim.business.main.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.InputMethodutil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.equipment.adapter.EquipmentListAdapter;
import com.glodon.bim.business.equipment.bean.EquipmentListBeanItem;
import com.glodon.bim.business.main.adapter.QualityEquipmentSearchHistoryAdapter;
import com.glodon.bim.business.main.contract.QualityEquipmentSearchContract;
import com.glodon.bim.business.main.presenter.QualityEquipmentSearchPresenter;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListAdapter;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnSearchHistoryClickListener;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;

import java.util.List;

/**
 * Created by cwj on 2018/3/6.
 * Description:项目主页搜索，质检清单、材设的混合搜索
 */
public class MainPageSearchActivity extends BaseActivity implements QualityEquipmentSearchContract.View, View.OnClickListener {

    private QualityEquipmentSearchContract.Presenter mPresenter;

    private View mStatusView;
    private TextView mCancelView;
    private EditText mInputView;
    private ScrollView mScrollView;
    private RecyclerView mQualityContentRecyclerView, mEquipmentContentRecyclerView, mHistoryRecyclerView;
    private TextView mQualityTagTv;
    private TextView mEquipmentTagTv;
    private RelativeLayout mQualityMoreRl;
    private RelativeLayout mEquipmentMoreRl;

    private QualityEquipmentSearchHistoryAdapter mHistoryAdapter;//历史
    private QualityCheckListAdapter mQualityResultAdapter;//质检清单结果
    private EquipmentListAdapter mEquipmentResultAdapter;//材设清单结果

    private PhotoAlbumDialog mPhotoAlbumDialog;//拍照相册弹出框
    private String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_search);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.main_page_search_statusview);
        mCancelView = (TextView) findViewById(R.id.main_page_search_cancel);
        mInputView = (EditText) findViewById(R.id.main_page_search_input);
        mScrollView = (ScrollView) findViewById(R.id.main_page_search_sv);

        mQualityTagTv = (TextView) findViewById(R.id.main_page_search_quality_tag_tv);
        mEquipmentTagTv = (TextView) findViewById(R.id.main_page_search_equipment_tag_tv);
        mQualityMoreRl = findViewById(R.id.main_page_search_quality_more_rl);
        mEquipmentMoreRl = findViewById(R.id.main_page_search_equipment_more_rl);

        mQualityContentRecyclerView = (RecyclerView) findViewById(R.id.main_page_search_quality_content);
        mEquipmentContentRecyclerView = (RecyclerView) findViewById(R.id.main_page_search_equipment_content);
        mHistoryRecyclerView = (RecyclerView) findViewById(R.id.main_page_search_history);

        initRecyclerView(mQualityContentRecyclerView, false);
        initRecyclerView(mEquipmentContentRecyclerView, false);
        initRecyclerView(mHistoryRecyclerView, true);

        mInputView.clearFocus();

    }

    private void initRecyclerView(RecyclerView recyclerView, final boolean canScrollVertically) {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVerticalScrollBarEnabled(true);
        final LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                if (canScrollVertically) {
                    return super.canScrollVertically();
                }
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
    }

    private void setListener() {
        mCancelView.setOnClickListener(this);
        mQualityMoreRl.setOnClickListener(this);
        mEquipmentMoreRl.setOnClickListener(this);

        mInputView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showSearchHistory();
                } else {
                    hideHistory();
                }
            }
        });

        mInputView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    LogUtil.e("点击搜索");
                    String key = mInputView.getText().toString().trim();
                    if (!TextUtils.isEmpty(key)) {
                        saveKey(key);
                        search(key);
                    }
                }
                return true;
            }
        });
    }


    private void initData() {
        mPresenter = new QualityEquipmentSearchPresenter(this);
        mPresenter.initData(getIntent());

        //材设清单列表
        mEquipmentResultAdapter = new EquipmentListAdapter(this, mPresenter.getOperateEquipmentSheetListener());
        mEquipmentContentRecyclerView.setAdapter(mEquipmentResultAdapter);

        //质检清单列表
        mQualityResultAdapter = new QualityCheckListAdapter(this, mPresenter.getOnOperateSheetListener());
        mQualityContentRecyclerView.setAdapter(mQualityResultAdapter);

        //搜索历史
        mHistoryAdapter = new QualityEquipmentSearchHistoryAdapter(mActivity, new OnSearchHistoryClickListener() {
            @Override
            public void click(String key) {
                if (!TextUtils.isEmpty(key)) {
                    mInputView.setText(key);
                    mInputView.setSelection(key.length());
                    saveKey(key);
                    search(key);
                }
            }
        });
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);

        mInputView.requestFocus();

        showSearchHistory();
    }

    @Override
    public void showLoadingDialog() {
        showLoadDialog(true);
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoadDialog();
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public void hideHistory() {
        mHistoryRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showResult(List<QualityCheckListBeanItem> mQualityData, List<EquipmentListBeanItem> mEquipmentData) {
        mQualityResultAdapter.updateList(mQualityData);
        mEquipmentResultAdapter.updateList(mEquipmentData);
        mScrollView.scrollTo(0, 0);
        if (mQualityData.size() == 0) {
            setQualityViewVisibility(View.GONE);
        } else if (mQualityData.size() < 3) {
            mQualityMoreRl.setVisibility(View.GONE);
        } else {
            setQualityViewVisibility(View.VISIBLE);
        }
        if (mEquipmentData.size() == 0) {
            setEquipmentViewVisibility(View.GONE);
        } else if (mEquipmentData.size() < 3) {
            mEquipmentMoreRl.setVisibility(View.GONE);
        } else {
            setEquipmentViewVisibility(View.VISIBLE);
        }
        mInputView.clearFocus();
        InputMethodutil.HideKeyboard(mInputView);
    }

    /**
     * 设置质检清单View的显示隐藏状态
     *
     * @param visibility
     */
    private void setQualityViewVisibility(int visibility) {
        mQualityTagTv.setVisibility(visibility);
        mQualityMoreRl.setVisibility(visibility);
        mQualityContentRecyclerView.setVisibility(visibility);
    }

    /**
     * 设置材设清单View的显示隐藏状态
     *
     * @param visibility
     */
    private void setEquipmentViewVisibility(int visibility) {
        mEquipmentTagTv.setVisibility(visibility);
        mEquipmentMoreRl.setVisibility(visibility);
        mEquipmentContentRecyclerView.setVisibility(visibility);
    }

    @Override
    public void create() {
        if (mPhotoAlbumDialog == null) {
            mPhotoAlbumDialog = new PhotoAlbumDialog(getActivity()).builder(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.openPhoto();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.openAlbum();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.toCreate();
                }
            });
        }
        mPhotoAlbumDialog.show();
    }

    /**
     * 显示搜索历史列表
     */
    private void showSearchHistory() {
        List<String> list = SharedPreferencesUtil.getQualityEquipmentSearchKey();

        if (list != null && list.size() > 0) {
            mHistoryRecyclerView.setVisibility(View.VISIBLE);
            mHistoryAdapter.updateList(list);
        } else {
            mHistoryRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * 保存搜索历史记录
     *
     * @param key
     */
    private void saveKey(String key) {
        SharedPreferencesUtil.saveQualityEquipmentSearchKey(key);

    }

    private void search(String key) {
        InputMethodutil.HideKeyboard(mInputView);
        if (!TextUtils.isEmpty(key)) {
            searchKey = key;
            mPresenter.search(key);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_page_search_cancel:
                cancelSearch();
                break;
            case R.id.main_page_search_quality_more_rl:
                if (mPresenter != null) {
                    mPresenter.searchMoreQualityCheckList();
                }
                break;
            case R.id.main_page_search_equipment_more_rl:
                if (mPresenter != null) {
                    mPresenter.searchMoreEquipmentList();
                }
                break;
        }
    }

    /**
     * 点击取消按钮时：返回原始搜索或退出页面
     */
    private void cancelSearch() {
        if (mHistoryRecyclerView.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(searchKey)) {
            hideHistory();
            mInputView.setText(searchKey);
            mInputView.clearFocus();
            InputMethodutil.HideKeyboard(mInputView);
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
