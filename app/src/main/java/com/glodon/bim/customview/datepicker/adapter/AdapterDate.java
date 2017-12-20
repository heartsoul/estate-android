
package com.glodon.bim.customview.datepicker.adapter;


import android.content.Context;

import com.glodon.bim.customview.datepicker.DateAdapter;


/**
 * 作者：zhourf on 2017/11/10
 * 邮箱：zhourf@glodon.com
 */
public class AdapterDate extends AbstractDateTextAdapter {

    // Source adapter
    private DateAdapter adapter;
    
    /**
     * Constructor
     * @param context the current context
     * @param adapter the source adapter
     */
    public AdapterDate(Context context, DateAdapter adapter) {
        super(context);
        
        this.adapter = adapter;
    }

    /**
     * Gets original adapter
     * @return the original adapter
     */
    public DateAdapter getAdapter() {
        return adapter;
    }
    
    @Override
    public int getItemsCount() {
        return adapter.getItemsCount();
    }

    @Override
    protected CharSequence getItemText(int index) {
        return adapter.getItem(index);
    }

}
