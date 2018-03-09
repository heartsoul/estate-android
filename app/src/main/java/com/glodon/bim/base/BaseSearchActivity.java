package com.glodon.bim.base;

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
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.InputMethodutil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.main.adapter.QualityEquipmentSearchHistoryAdapter;
import com.glodon.bim.business.qualityManage.listener.OnSearchHistoryClickListener;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.List;

/**
 * Created by cwj on 2018/3/9.
 * Description:搜索页,子类又质检清单搜索页{@link com.glodon.bim.business.qualityManage.view.QualityCheckListSearchActivity}、<br/>
 * 材设清单搜索页{@link com.glodon.bim.business.equipment.view.EquipmentSearchActivity}
 */

public abstract class BaseSearchActivity extends BaseActivity implements View.OnClickListener {

    protected View mStatusView;
    protected TextView mCancelView;
    protected EditText mInputView;
    protected PullRefreshView mPullRefreshView;
    protected RecyclerView mContentRecyclerView, mHistoryRecyclerView;

    private QualityEquipmentSearchHistoryAdapter mHistoryAdapter;//历史
    private String searchKey;//每次搜索的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_search);
        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.base_search_statusview);
        mCancelView = (TextView) findViewById(R.id.base_search_cancel);
        mInputView = (EditText) findViewById(R.id.base_search_input);

        mHistoryRecyclerView = (RecyclerView) findViewById(R.id.base_search_history);
        mPullRefreshView = (PullRefreshView) findViewById(R.id.base_search_pull_refresh_view);

        initPullRefreshView();
        initRecyclerView(mHistoryRecyclerView);
        initRecyclerView(mContentRecyclerView);

        mInputView.clearFocus();
    }

    /**
     * 初始化PullRefreshView
     */
    private void initPullRefreshView() {
        mPullRefreshView.setPullDownEnable(true);
        mPullRefreshView.setPullUpEnable(true);
        mPullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDown() {
                pullDown();
                mPullRefreshView.onPullDownComplete();
            }

            @Override
            public void onPullUp() {
                pullUp();
                mPullRefreshView.onPullUpComplete();
            }
        });

        mContentRecyclerView = mPullRefreshView.getmRecyclerView();
    }

    /**
     * 设置RecyclerView
     *
     * @param recyclerView recyclerView
     */
    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVerticalScrollBarEnabled(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    /**
     * 取消事件的监听<br/>
     * 输入空的焦点变换监听<br/>
     * 键盘搜索键监听,出发搜索事件<br/>
     */
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
        //初始化页面绑定的presenter
        initPresenterAndContentAdapter();

        presenterSearch(searchKey);

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

        //初始显示隐藏search history list
        if (TextUtils.isEmpty(searchKey)) {
            showSearchHistory();
        } else {
            mInputView.setText(searchKey);
            InputMethodutil.HideKeyboard(mInputView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_search_cancel:
                cancelSearch();
                break;
        }
    }

    /**
     * 点击取消按钮时：返回原始搜索或退出页面。当搜索历史列表显示且有搜索历史时，返回原搜索列表，否则退出页面
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


    /**
     * 显示搜索历史列表
     */
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

    /**
     * 保存搜索记录
     *
     * @param key key
     */
    private void saveKey(String key) {
        SharedPreferencesUtil.saveQualityEquipmentSearchKey(key);
    }

    private void search(String key) {
        InputMethodutil.HideKeyboard(mInputView);
        if (!TextUtils.isEmpty(key)) {
            searchKey = key;
            presenterSearch(key);
        }
    }


    /**
     * 取消输入框的焦点，隐藏搜索历史
     */
    protected void clearInputViewFocus() {
        hideHistory();
        mInputView.clearFocus();
        InputMethodutil.HideKeyboard(mInputView);
    }

    /**
     * 初始化搜索绑定的presenter，及列表适配器
     */
    public abstract void initPresenterAndContentAdapter();

    /**
     * 下拉刷新
     */
    public abstract void pullDown();

    /**
     * 上拉加载
     */
    public abstract void pullUp();

    /**
     * 调用search方法
     *
     * @param key key
     */
    public abstract void presenterSearch(String key);


}
