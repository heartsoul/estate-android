
package com.glodon.bim.customview.datepicker.adapter;


import android.content.Context;

import com.glodon.bim.customview.datepicker.TNBDateAdapter;


/**
 * Adapter class for old wheel adapter (deprecated WheelAdapter class).
 * 
 * deprecated Will be removed soon
 */
public class TNBAdapterDate extends TNBAbstractDateTextAdapter {

    // Source adapter
    private TNBDateAdapter adapter;
    
    /**
     * Constructor
     * @param context the current context
     * @param adapter the source adapter
     */
    public TNBAdapterDate(Context context, TNBDateAdapter adapter) {
        super(context);
        
        this.adapter = adapter;
    }

    /**
     * Gets original adapter
     * @return the original adapter
     */
    public TNBDateAdapter getAdapter() {
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
