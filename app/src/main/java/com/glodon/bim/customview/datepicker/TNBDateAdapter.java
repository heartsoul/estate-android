
package com.glodon.bim.customview.datepicker;

/**
 * Description:填充数据的adapter
 * Created by 周瑞峰
 * Job number:136597
 * Phone:15001340978
 * Email:zhouruifeng@syswin.com
 * Person in charge:周瑞峰
 * Leader:周瑞峰
 */
public interface TNBDateAdapter {
	/**
	 * Gets items count
	 * @return the count of wheel items
	 */
	int getItemsCount();
	
	/**
	 * Gets a wheel item by index.
	 * 
	 * @param index the item index
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
