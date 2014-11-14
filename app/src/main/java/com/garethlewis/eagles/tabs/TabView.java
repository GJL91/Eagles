package com.garethlewis.eagles.tabs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.garethlewis.eagles.R;

public class TabView extends TextView {
    private int index;

    private int mMaxTabWidth;

    public TabView(Context context) {
        super(context, null, R.attr.tabPageIndicatorStyle);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setmMaxTabWidth(int mMaxTabWidth) {
        this.mMaxTabWidth = mMaxTabWidth;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Re-measure if we went beyond our maximum size.
        if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
                    heightMeasureSpec);
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
