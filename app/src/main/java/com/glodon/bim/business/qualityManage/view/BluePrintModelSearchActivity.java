package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.adapter.BluePrintModelSearchAdapter;
import com.glodon.bim.business.qualityManage.adapter.BluePrintModelSearchHistoryAdapter;
import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBeanItem;
import com.glodon.bim.business.qualityManage.contract.BluePrintModelSearchContract;
import com.glodon.bim.business.qualityManage.listener.OnSearchHistoryClickListener;
import com.glodon.bim.business.qualityManage.presenter.BluePrintModelSearchPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：模型图纸搜索
 * 作者：zhourf on 2017/12/21
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintModelSearchActivity extends BaseActivity implements View.OnClickListener, BluePrintModelSearchContract.View {

    private BluePrintModelSearchContract.Presenter mPresenter;
    private View mStatusView;
    private EditText mInputView;
    private TextView mCancelView;
    private RecyclerView mContentRecyclerView, mHistoryRecyclerView;

    private LinearLayout mDefaultBg;

    private BluePrintModelSearchHistoryAdapter mHistoryAdapter;
    private BluePrintModelSearchAdapter mResultAdapter;
    private int mSearchType = 0;//0图纸  1模型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_search_blueprint_model_activity);
        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.blueprint_model_search_statusview);
        mInputView = (EditText) findViewById(R.id.blueprint_model_search_input);
        mCancelView = (TextView) findViewById(R.id.blueprint_model_search_cancel);
        mContentRecyclerView = (RecyclerView) findViewById(R.id.blueprint_model_search_content);
        mHistoryRecyclerView = (RecyclerView) findViewById(R.id.blueprint_model_search_history);

        mDefaultBg = (LinearLayout) findViewById(R.id.blueprint_model_search_defaultbg);

        initContentRecyclerView();
        initHistoryRecyclerView();
    }

    private void initContentRecyclerView() {

        mContentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mContentRecyclerView.setVerticalScrollBarEnabled(true);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mContentRecyclerView.setLayoutManager(manager);

    }

    private void initHistoryRecyclerView() {
        mHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mHistoryRecyclerView.setVerticalScrollBarEnabled(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mHistoryRecyclerView.setLayoutManager(manager);

    }

    private void setListener() {
        mCancelView.setOnClickListener(this);
        mInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String key = mInputView.getText().toString().trim();
                if(TextUtils.isEmpty(key)){
                    mHistoryRecyclerView.setVisibility(View.GONE);
                }else{
                    showSearchHistory();
                }
            }
        });

        //监听软键盘的搜索按钮
        mInputView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    LogUtil.e("点击搜索");
                    String key = mInputView.getText().toString().trim();
                    saveKey(key);
                    if(!TextUtils.isEmpty(key)) {
                        search(key);
                    }
                }
                return true;
            }
        });

        mInputView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    showSearchHistory();
                }else{
                    hideHistory();
                }
            }
        });


    }

    //保存搜索历史
    private void saveKey(String key) {
        switch (mSearchType)
        {
            case 0:
                SharedPreferencesUtil.saveBluePrintSearchKey(key);
                break;
            case 1:
                SharedPreferencesUtil.saveModelSearchKey(key);
                break;
        }
    }



    private void showSearchHistory() {
        List<String> list = new ArrayList<>();

        switch (mSearchType)
        {
            case 0:
                list = SharedPreferencesUtil.getBluePrintSearchKey();
                break;
            case 1:
                list = SharedPreferencesUtil.getModelSearchKey();
                break;
        }
        if(list!=null && list.size()>0){
            mHistoryRecyclerView.setVisibility(View.VISIBLE);
            mHistoryAdapter.updateList(list);
        }else{
            mHistoryRecyclerView.setVisibility(View.GONE);
        }
    }


    private void initData() {
        mSearchType = getIntent().getIntExtra(CommonConfig.SEARCH_TYPE,0);
        LogUtil.e("type="+mSearchType);
        //搜索历史
        mHistoryAdapter = new BluePrintModelSearchHistoryAdapter(mActivity, new OnSearchHistoryClickListener() {
            @Override
            public void click(String key) {
                mInputView.setText(key);
                saveKey(key);
                search(key);

            }
        });
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);
        //搜搜索结果
        mResultAdapter = new BluePrintModelSearchAdapter(mActivity);
        mContentRecyclerView.setAdapter(mResultAdapter);

        mPresenter = new BluePrintModelSearchPresenter(this);
        mPresenter.initData(getIntent());

        mResultAdapter.setmListener(mPresenter.getmListener());
    }

    private void search(String key){

        if(!TextUtils.isEmpty(key)) {
            mPresenter.search(key);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.blueprint_model_search_cancel:
                String key = mInputView.getText().toString().trim();
                if(TextUtils.isEmpty(key)) {
                    mActivity.finish();
                }else{
                    mInputView.setText("");
                }
                break;
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
    public void hideHistory() {
        mHistoryRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showResult(List<BluePrintModelSearchBeanItem> mDataList) {
        mResultAdapter.updateList(mDataList);
        if(mDataList.size()>0) {
            mHistoryRecyclerView.setVisibility(View.GONE);
            mContentRecyclerView.setVisibility(View.VISIBLE);
            mDefaultBg.setVisibility(View.GONE);
        }else{
            mDefaultBg.setVisibility(View.VISIBLE);
            mHistoryRecyclerView.setVisibility(View.GONE);
            mContentRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mPresenter!=null){
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }
}
