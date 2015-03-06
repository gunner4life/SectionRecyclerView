package com.gunner.sectionrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * SectionRecyclerView
 * Created by gunner4life on 15/3/5.
 */
public class SectionRecyclerView extends RecyclerView {

    public enum LayoutType {
        Grid("grid"), List("list");

        private String layoutType;

        LayoutType(String layoutType) {
            this.layoutType = layoutType;
        }

        public String getLayoutType() {
            return layoutType;
        }
    }

    private LayoutType mLayoutType;

    private static final int SpanCount = 2;

    private int spanCount = SpanCount;

    private Context mContext;

    public SectionRecyclerView(Context context) {
        this(context, null);
    }

    public SectionRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mContext = context;
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SectionRecyclerView, defStyle, 0);
        String layoutType = a.getString(R.styleable.SectionRecyclerView_layout_type);
        if (LayoutType.List.getLayoutType().equalsIgnoreCase(layoutType)) {
            mLayoutType = LayoutType.List;
        } else {
            mLayoutType = LayoutType.Grid;
        }
        spanCount = a.getInteger(R.styleable.SectionRecyclerView_span_count, SpanCount);
        a.recycle();
    }

    private void renderView(final BaseSectionRecyclerViewAdapter mAdapter) {
        LayoutManager mLayoutManager;
        if (mLayoutType == LayoutType.List) {
            mLayoutManager = new LinearLayoutManager(mContext);
        } else {
            mLayoutManager = new GridLayoutManager(mContext, spanCount);
            GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = mAdapter.getItemViewType(position);
                    switch (viewType) {
                        case BaseSectionRecyclerViewAdapter.TYPE_HEADER:
                        case BaseSectionRecyclerViewAdapter.TYPE_FOOTER:
                        case BaseSectionRecyclerViewAdapter.TYPE_ITEM_GROUP:
                            return spanCount;
                        case BaseSectionRecyclerViewAdapter.TYPE_ITEM_CHILD:
                            return 1;
                        default:
                            return 0;

                    }
                }
            };
            ((GridLayoutManager) mLayoutManager).setSpanSizeLookup(spanSizeLookup);
        }
        setLayoutManager(mLayoutManager);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof BaseSectionRecyclerViewAdapter) {
            renderView((BaseSectionRecyclerViewAdapter) adapter);
            super.setAdapter(adapter);
        } else {
            throw new RuntimeException("SectionRecyclerView's adapter must be the instance of BaseSectionRecyclerViewAdapter!");
        }
    }

    /**
     * Set the type of layout, the value should be one of 'grid' or 'list'
     *
     * @param layoutType type of layout
     */
    public void setLayoutType(LayoutType layoutType) {
        this.mLayoutType = layoutType;
        Adapter mAdapter = getAdapter();
        if (mAdapter != null && mAdapter instanceof BaseSectionRecyclerViewAdapter) {
            renderView((BaseSectionRecyclerViewAdapter) mAdapter);
        } else {
            throw new RuntimeException("setLayoutType(String layoutType) must be called after setAdpter(Adapter adapter) and" +
                    " SectionRecyclerView's adapter must be the instance of BaseSectionRecyclerViewAdapter!");
        }
    }
}
