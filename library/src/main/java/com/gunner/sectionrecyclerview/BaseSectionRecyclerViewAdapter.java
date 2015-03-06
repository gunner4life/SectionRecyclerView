package com.gunner.sectionrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Base adapter for {@link SectionRecyclerView}
 * Created by gunner4life on 15/3/5.
 */
public abstract class BaseSectionRecyclerViewAdapter extends RecyclerView.Adapter {

    /**
     * The type of the item is Header
     */
    public static final int TYPE_HEADER = 1;

    /**
     * The type of the item is Footer
     */
    public static final int TYPE_FOOTER = 2;

    /**
     * The type of the item is Group
     */
    public static final int TYPE_ITEM_GROUP = 3;

    /**
     * The type of the item is Child
     */
    public static final int TYPE_ITEM_CHILD = 4;

    /**
     * Whether the RecyclerView has Header, default is false
     */
    private boolean hasHeader = false;

    /**
     * Whether the RecyclerView has Footer, default is false
     */
    private boolean hasFooter = false;

    /**
     * A list to store the info of each position
     */
    private List<PositionMeta> positionMetaList;

    /**
     * Instance of item click listener
     */
    private OnItemClickListener onItemClickListener;

    /**
     * Define the item click listener of the {@link SectionRecyclerView}
     */
    public interface OnItemClickListener {
        void onGroupItemClickListener(View view, int groupPosition);

        void onChildItemClickListener(View view, int groupPosition, int childPosition);
    }

    /**
     * Calculate the number of the group
     *
     * @return the group number
     */
    protected abstract int getGroupCount();

    /**
     * Calculate the child number of the Group
     *
     * @param groupPosition the position of the Group
     * @return the child number of the group at given position
     */
    protected abstract int getChildrenCount(int groupPosition);

    /**
     * Get the {@link android.support.v7.widget.RecyclerView.ViewHolder} instance of Group
     *
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position
     * @return the {@link android.support.v7.widget.RecyclerView.ViewHolder} instance of Group
     */
    protected abstract RecyclerView.ViewHolder onCreateGroupViewHolder(ViewGroup viewGroup);

    /**
     * Get the {@link android.support.v7.widget.RecyclerView.ViewHolder} instance of Child
     *
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position
     * @return the {@link android.support.v7.widget.RecyclerView.ViewHolder} instance of Child
     */
    protected abstract RecyclerView.ViewHolder onCreateChildrenViewHolder(ViewGroup viewGroup);

    /**
     * Display the group data at the specified position
     *
     * @param groupViewHolder the {@link android.support.v7.widget.RecyclerView.ViewHolder} which should be updated to
     *                        represent the contents of the Group item at the given position in the data set
     * @param groupPosition   the position of the Group
     */
    protected abstract void onBindGroupViewHolder(RecyclerView.ViewHolder groupViewHolder, int groupPosition);

    /**
     * Display the child data at the specified position
     *
     * @param childViewHolder the {@link android.support.v7.widget.RecyclerView.ViewHolder} which should be updated to
     *                        represent the contents of the Child item at the given position in the data set
     * @param groupPosition   the position of the Group
     * @param childPosition   the position of the Child
     */
    protected abstract void onBindChildViewHolder(RecyclerView.ViewHolder childViewHolder, int groupPosition, int childPosition);

    /**
     * Get the {@link android.support.v7.widget.RecyclerView.ViewHolder} instance of Header
     *
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position
     * @return the {@link android.support.v7.widget.RecyclerView.ViewHolder} instance of Header
     */
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        throw new IllegalAccessError("Please Override this Method if you want to add a Header to RecyclerView, or " +
                "you should not call setHasHeader(true)");
    }

    /**
     * Get the {@link android.support.v7.widget.RecyclerView.ViewHolder} instance of Footer
     *
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position
     * @return the {@link android.support.v7.widget.RecyclerView.ViewHolder} instance of Footer
     */
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup viewGroup) {
        throw new IllegalAccessError("Please Override this Method if you want to add a Footer to RecyclerView, or " +
                "you should not call setHasFooter(true)");
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && position == 0) {
            return TYPE_HEADER;
        } else if (hasFooter && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return positionMetaList.get(position - getHeaderCount()).type == PositionMeta.GROUP ? TYPE_ITEM_GROUP : TYPE_ITEM_CHILD;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return onCreateHeaderViewHolder(viewGroup);
            case TYPE_FOOTER:
                return onCreateFooterViewHolder(viewGroup);
            case TYPE_ITEM_GROUP:
                return onCreateGroupViewHolder(viewGroup);
            case TYPE_ITEM_CHILD:
                return onCreateChildrenViewHolder(viewGroup);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_ITEM_CHILD:
                final PositionMeta positionMeta = positionMetaList.get(position - getHeaderCount());
                onBindChildViewHolder(viewHolder, positionMeta.groupPos, positionMeta.childPos);
                if (onItemClickListener != null) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onChildItemClickListener(viewHolder.itemView, positionMeta.groupPos, positionMeta.childPos);
                        }
                    });
                }
                break;
            case TYPE_ITEM_GROUP:
                final PositionMeta groupPositionMeta = positionMetaList.get(position - getHeaderCount());
                onBindGroupViewHolder(viewHolder, groupPositionMeta.groupPos);
                if (onItemClickListener != null) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onGroupItemClickListener(viewHolder.itemView, groupPositionMeta.groupPos);
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        int groupCount = getGroupCount();
        int totalChildCount = 0;
        for (int i = 0; i < groupCount; i++) {
            totalChildCount += getChildrenCount(i);
        }
        return groupCount + totalChildCount + getHeaderCount() + getFooterCount();
    }

    /**
     * Define whether the {@link SectionRecyclerView} has Header
     *
     * @param hasHeader whether the {@link SectionRecyclerView} has Header
     */
    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    /**
     * Define whether the {@link SectionRecyclerView} has Footer
     *
     * @param hasFooter whether the {@link SectionRecyclerView} has Footer
     */
    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    /**
     * Set the item click listener of the {@link SectionRecyclerView}
     *
     * @param clickListener
     */
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }


    /**
     * Get the number of Header
     *
     * @return the number of Header
     */
    public int getHeaderCount() {
        return hasHeader ? 1 : 0;
    }

    /**
     * Get the number of Footer
     *
     * @return the number of Footer
     */
    public int getFooterCount() {
        return hasFooter ? 1 : 0;
    }

    /**
     * Generate the {@link PositionMeta} info of the {@link SectionRecyclerView}, should be called after each data set changed
     */
    private void generatePositionMetaInfo() {
        List<PositionMeta> positionMetaList = new ArrayList<>();
        int groupCount = getGroupCount();
        for (int groupPos = 0; groupPos < groupCount; groupPos++) {
            // Group PositionMeta
            PositionMeta groupPositionMeta = new PositionMeta();
            groupPositionMeta.type = PositionMeta.GROUP;
            groupPositionMeta.groupPos = groupPos;
            groupPositionMeta.childPos = 0;
            positionMetaList.add(groupPositionMeta);

            int childCountInGroup = getChildrenCount(groupPos);
            for (int childPos = 0; childPos < childCountInGroup; childPos++) {
                // Child PositionMeta
                PositionMeta childPositionMeta = new PositionMeta();
                childPositionMeta.type = PositionMeta.CHILD;
                childPositionMeta.groupPos = groupPos;
                childPositionMeta.childPos = childPos;
                positionMetaList.add(childPositionMeta);
            }
        }
        this.positionMetaList = positionMetaList;
    }

    /**
     * Refresh the whole {@link SectionRecyclerView}, you should call this method after each data set changed
     */
    protected void refreshView() {
        generatePositionMetaInfo();
        notifyDataSetChanged();
    }

    /**
     * Store the basic info of each {@link SectionRecyclerView} item
     */
    public class PositionMeta {

        public final static int CHILD = 1;

        public final static int GROUP = 2;

        public int groupPos;

        public int childPos;

        public int type;
    }
}
