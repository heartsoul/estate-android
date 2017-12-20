
package com.glodon.bim.customview.datepicker;

/**
 * Description:日期滚动时的回调接口
 * 作者：zhourf on 2017/11/10
 * 邮箱：zhourf@glodon.com
 */
public interface OnDateScrollListener {
	void onScrollingStarted(DateView wheel);
	void onScrollingFinished(DateView wheel);
}
