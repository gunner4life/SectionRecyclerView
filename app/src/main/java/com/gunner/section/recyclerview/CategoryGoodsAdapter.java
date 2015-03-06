package com.gunner.section.recyclerview;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gunner.sectionrecyclerview.BaseSectionRecyclerViewAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter
 * Created by gunner4life on 15/3/5.
 */
public class CategoryGoodsAdapter extends BaseSectionRecyclerViewAdapter {

    private List<Category> mCategoryList = new ArrayList<>();

    public void refreshViewByData(List<Category> categoryList) {
        this.mCategoryList = categoryList;
        super.refreshView();
    }

    public static class GoodsViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public final TextView mTitleView;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.goods_item_image);
            mTitleView = (TextView) itemView.findViewById(R.id.goods_item_name);
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitleView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            mTitleView = (TextView) itemView.findViewById(R.id.category_name);
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footer, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    protected int getGroupCount() {
        return mCategoryList.size();
    }

    @Override
    protected int getChildrenCount(int groupPosition) {
        return mCategoryList.get(groupPosition).goodsList.size();
    }


    @Override
    protected RecyclerView.ViewHolder onCreateGroupViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);
        return new CategoryViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateChildrenViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goods_item, viewGroup, false);
        return new GoodsViewHolder(view);
    }

    @Override
    protected void onBindGroupViewHolder(RecyclerView.ViewHolder groupViewHolder, int groupPosition) {
        if (groupViewHolder instanceof CategoryViewHolder) {
            Category category = mCategoryList.get(groupPosition);
            ((CategoryViewHolder) groupViewHolder).mTitleView.setText(category.categoryName);
        }
    }

    @Override
    protected void onBindChildViewHolder(final RecyclerView.ViewHolder childViewHolder, int groupPosition, int childPosition) {
        if (childViewHolder instanceof GoodsViewHolder) {
            Goods goods = mCategoryList.get(groupPosition).goodsList.get(childPosition);
            ((GoodsViewHolder) childViewHolder).mTitleView.setText(goods.goodsName);
            Picasso.with(((GoodsViewHolder) childViewHolder).mImageView.getContext())
                    .load(goods.goodsImg)
                    .transform(PaletteTransformation.getInstance())
                    .into(((GoodsViewHolder) childViewHolder).mImageView, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) ((GoodsViewHolder) childViewHolder).mImageView.getDrawable()).getBitmap();
                            Palette palette = PaletteTransformation.getPalette(bitmap);
                            if (palette != null) {
                                ((GoodsViewHolder) childViewHolder).mTitleView.setTextColor(palette.getMutedColor(Color.parseColor("#333333")));
                            }
                        }
                    });
        }
    }
}
