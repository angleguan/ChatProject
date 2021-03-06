package com.yzx.chat.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzx.chat.R;
import com.yzx.chat.base.BaseRecyclerViewAdapter;
import com.yzx.chat.bean.ContactBean;
import com.yzx.chat.util.GlideUtil;

import java.util.List;


/**
 * Created by YZX on 2017年06月29日.
 * 生命太短暂,不要去做一些根本没有人想要的东西
 */


public class ContactAdapter extends BaseRecyclerViewAdapter<ContactAdapter.ItemView> {

    private List<ContactBean> mContactList;
    private SparseArray<String> mIdentitySparseArray;

    public ContactAdapter(List<ContactBean> contactList) {
        mContactList = contactList;
        mIdentitySparseArray = new SparseArray<>(32);
        registerAdapterDataObserver(mDataObserver);
    }

    @Override
    public ItemView getViewHolder(ViewGroup parent, int viewType) {
        return new ItemContactView(LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false));

    }

    @Override
    public void bindDataToViewHolder(ItemView holder, int position) {
        ItemContactView friendHolder = (ItemContactView) holder;
        String identity = mIdentitySparseArray.get(position);
        if (identity != null) {
            holder.itemView.setTag(identity);
        } else {
            holder.itemView.setTag(null);
        }
        ContactBean contactBean = mContactList.get(position);
        friendHolder.mTvName.setText(contactBean.getName());
        GlideUtil.loadFromUrl(mContext, friendHolder.mIvHeadImage, R.drawable.temp_head_image);
    }

    @Override
    public int getViewHolderCount() {
        return mContactList == null ? 0 : mContactList.size();
    }

    public int findPositionByLetter(String letter) {
        int keyIndex = mIdentitySparseArray.indexOfValue(letter);
        if (keyIndex != -1) {
            return mIdentitySparseArray.keyAt(keyIndex);
        }
        return -1;
    }

    private void resetLetter() {
        if (mContactList != null && mContactList.size() != 0) {
            String identity;
            String abbreviation;
            String currentIdentity = null;
            for (int i = 0, length = mContactList.size(); i < length; i++) {
                abbreviation = mContactList.get(i).getAbbreviation();
                if (abbreviation != null) {
                    identity = abbreviation.substring(0, 1);
                    if (!identity.equals(currentIdentity)) {
                        mIdentitySparseArray.append(i, identity.toUpperCase().intern());
                        currentIdentity = identity;
                    }
                }
            }
        }
    }

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            resetLetter();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            resetLetter();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            resetLetter();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            resetLetter();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            resetLetter();
        }

    };

    static abstract class ItemView extends BaseRecyclerViewAdapter.BaseViewHolder {

        ItemView(View itemView) {
            super(itemView);
        }
    }

    private static class ItemContactView extends ItemView {

        TextView mTvName;
        ImageView mIvHeadImage;

        ItemContactView(View itemView) {
            super(itemView);
            initView();

        }

        private void initView() {
            mTvName = (TextView) itemView.findViewById(R.id.ContactAdapter_mTvName);
            mIvHeadImage = (ImageView) itemView.findViewById(R.id.ContactAdapter_mIvHeadImage);
        }
    }

}
