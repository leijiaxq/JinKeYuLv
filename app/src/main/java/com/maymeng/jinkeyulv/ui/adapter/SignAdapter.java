package com.maymeng.jinkeyulv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.bean.SignBean;
import com.maymeng.jinkeyulv.view.ProgressWheel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by  leijiaxq
 * Date       2017/3/15 11:27
 * Describe
 */
public class SignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<SignBean.ResponseDataBean> mDatas;
    public boolean isAllLoad = false;


    public SignAdapter(Context context, List<SignBean.ResponseDataBean> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == Constants.TYPE_FOOT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot_layout, parent, false);
            return new ViewHolderFoot(view);
        } else if (viewType == Constants.TYPE_ONE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_dispatch, parent, false);
            return new ViewHolderType1(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderType1) {
            setDataType1((ViewHolderType1) holder, position);
        } else if (holder instanceof ViewHolderFoot) {
            setDataFoot((ViewHolderFoot) holder);
        }
    }


    private void setDataType1(ViewHolderType1 holder, final int position) {
        SignBean.ResponseDataBean bean = mDatas.get(position);
        holder.mItemNameTv.setText(TextUtils.isEmpty(bean.CustomerName) ? "" : bean.CustomerName);

        String phone = "";
        if (!TextUtils.isEmpty(bean.Phone)) {
            if (bean.Phone.length() == 11) {

                StringBuilder sb = new StringBuilder();
                sb.append(bean.Phone.substring(0, 3)).append("-");
                sb.append(bean.Phone.substring(3, 7)).append("-");
                sb.append(bean.Phone.substring(7, 11));
                phone = sb.toString();

            } else {
                phone = bean.Phone;
            }
        }
        holder.mItemPhoneTv.setText("联系电话：" + phone);

        holder.mItemStatusTv.setText("未签约");

/*        if (bean.IsStatus == 100) {
//            holder.mItemStatusTv.setSelected(false);
            holder.mItemStatusTv.setText("已完成");
        } else if (bean.IsStatus == 0) {
//            holder.mItemStatusTv.setSelected(true);
            holder.mItemStatusTv.setText("未开始");
        } else {
            holder.mItemStatusTv.setText("进行中");

        }*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });

    }


    //设置底部foot
    private void setDataFoot(ViewHolderFoot holder) {
        if (isAllLoad) {
            holder.mItemFootPb.setVisibility(View.GONE);
            holder.mItemFootTv.setVisibility(View.GONE);
//            holder.mItemFootTv.setText("所有数据已经加载完");
        } else {
            holder.mItemFootPb.setVisibility(View.VISIBLE);
            holder.mItemFootTv.setVisibility(View.VISIBLE);
            holder.mItemFootTv.setText("正在加载...");
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null || mDatas.size() == 0 ? 0 : mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position + 1 == getItemCount()) {
            return Constants.TYPE_FOOT;
        }
        return Constants.TYPE_ONE;

    }

    public OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }


    static class ViewHolderFoot extends RecyclerView.ViewHolder {
        @BindView(R.id.item_foot_pb)
        ProgressWheel mItemFootPb;
        @BindView(R.id.item_foot_tv)
        TextView mItemFootTv;

        ViewHolderFoot(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderType1 extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name_tv)
        TextView mItemNameTv;
        @BindView(R.id.item_phone_tv)
        TextView mItemPhoneTv;
        @BindView(R.id.item_status_tv)
        TextView mItemStatusTv;
        @BindView(R.id.item_line_v)
        View mItemLineV;

        ViewHolderType1(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
