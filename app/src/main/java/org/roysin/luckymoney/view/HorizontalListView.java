package org.roysin.luckymoney.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * Created by Administrator on 2016/1/12.
 */
public class HorizontalListView extends AdapterView {

    private Adapter mAdapter;


    public HorizontalListView(Context context) {
        super(context);
    }

    public HorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int position) {

    }
}
