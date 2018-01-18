
package com.glodon.bim.customview.datepicker;

/**
 * Description:填充数据的adapter
 * 作者：zhourf on 2017/11/10
 * 邮箱：zhourf@glodon.com
 */
public interface DateAdapter {
	/**
	 * Gets items count
	 * @return the count of wheel items
	 */
	int getItemsCount();
	
	/**
	 * Gets a wheel item by batchCode.
	 * 
	 * @param index the item batchCode
	 * @return the wheel item text or null
	 */
	String getItem(int index);
	
	/**
	 * Gets maximum item length. It is used to determine the wheel width. 
	 * If -1 is returned there will be used the default wheel width.
	 * 
	 * @return the maximum item length or -1
	 */
	int getMaximumLength();
}
