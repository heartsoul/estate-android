package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.InputMethodutil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.main.adapter.QualityEquipmentSearchHistoryAdapter;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListAdapter;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;
import com.glodon.bim.business.qualityManage.listener.OnSearchHistoryClickListener;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.List;

/**
 * 质检清单搜索
 */
public class QualityCheckListSearchActivity extends BaseActivity implements View.OnClickListener ,QualityCheckListContract.View{

    private View mStatusView;
    private TextView mCancelView;
    private EditText mInputView;
    private PullRefreshView mPullRefreshView;
    private RecyclerView mContentRecyclerView, mHistoryRecyclerView;

    private QualityCheckListAdapter mQualityResultAdapter;//质检清单结果
    private QualityEquipmentSearchHistoryAdapter mHistoryAdapter;//历史
    private QualityCheckListContract.Presenter mPresenter;

    private String searchKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_search);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.quality_search_statusview);
        mCancelView = (TextView) findViewById(R.id.quality_search_cancel);
        mInputView = (EditText) findViewById(R.id.quality_search_input);

        mHistoryRecyclerView = (RecyclerView) findViewById(R.id.quality_search_history);
        mPullRefreshView = (PullRefreshView) findViewById(R.id.quality_search_pull_refresh_view);

        initPullRefreshView();
        initRecyclerView(mHistoryRecyclerView);
        initRecyclerView(mContentRecyclerView);

        mInputView.clearFocus();
    }

    private void initPullRefreshView() {
        mPullRefreshView.setPullDownEnable(true);
        mPullRefreshView.setPullUpEnable(true);
        mPullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDown() {
                mPresenter.pullDown();
                mPullRefreshView.onPullDownComplete();
            }

            @Override
            public void onPullUp() {
                mPresenter.pullUp();
                mPullRefreshView.onPullUpComplete();
            }
        });

        mContentRecyclerView = mPullRefreshView.getmRecyclerView();
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVerticalScrollBarEnabled(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    private void setListener() {
        mCancelView.setOnClickListener(this);
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
        searchKey = this.getIntent().getStringExtra(CommonConfig.SEARCH_KEY);
        mPresenter = new QualityCheckListPresenter(this);
        mQualityResultAdapter = new QualityCheckListAdapter(this, mPresenter.getListener());
        mContentRecyclerView.setAdapter(mQualityResultAdapter);

        mPresenter.search(searchKey);

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

        showSearchHistory();

    }

    private void showSearchHistory() {
        mInputView.requestFocus();
        List<String> list = SharedPreferencesUtil.getQualityEquipmentSearchKey();

        if (list != null && list.size() > 0) {
            mHistoryRecyclerView.setVisibility(View.VISIBLE);
            mHistoryAdapter.updateList(list);
        } else {
            mHistoryRecyclerView.setVisibility(View.GONE);
        }
    }

    private void hideHistory() {
        mHistoryRecyclerView.setVisibility(View.GONE);
    }

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
            case R.id.quality_search_cancel:
                cancelSearch();
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
    public void updateData(List<QualityCheckListBeanItem> mDataList) {
        mQualityResultAdapter.updateList(mDataList);
        mInputView.clearFocus();
        InputMethodutil.HideKeyboard(mInputView);

    }

    @Override
    public void create() {

    }

    @Override
    public void updateClassifyCount(List<ClassifyNum> list) {

    }
}
