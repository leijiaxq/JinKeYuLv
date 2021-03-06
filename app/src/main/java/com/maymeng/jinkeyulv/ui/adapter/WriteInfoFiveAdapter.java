package com.maymeng.jinkeyulv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.utils.ImageUtil;
import com.maymeng.jinkeyulv.view.mask.PorterShapeImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by  leijiaxq
 * Date       2017/3/15 11:27
 * Describe
 */
public class WriteInfoFiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> mDatas0; //名称
    private List<List<String>> mDatas;
    //    private List<String> mDatas1;
    //    private List<String> mDatas2;
//    private List<String> mDatas3;
//    private List<String> mDatas4;
//    private List<String> mDatas5;
//    private List<String> mDatas6;
//    private List<String> mDatas7;
//    private List<String> mDatas8;
//    private List<String> mDatas9;
//    private List<String> mDatas10;
//    private List<String> mDatas11;
    public boolean isAllLoad = false;


    public WriteInfoFiveAdapter(Context context, List<String> datas0, List<List<String>> datas/*, List<String> datas1, List<String> datas2, List<String> datas3, List<String> datas4, List<String> datas5, List<String> datas6, List<String> datas7, List<String> datas8, List<String> datas9, List<String> datas10, List<String> datas11*/) {
        mContext = context;
        mDatas0 = datas0;
        mDatas = datas;
//        mDatas1 = datas1;
//        mDatas2 = datas2;
//        mDatas3 = datas3;
//        mDatas4 = datas4;
//        mDatas5 = datas5;
//        mDatas6 = datas6;
//        mDatas7 = datas7;
//        mDatas8 = datas8;
//        mDatas9 = datas9;
//        mDatas10 = datas10;
//        mDatas11 = datas11;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == Constants.TYPE_FOOT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_writeinfo_five_bottom, parent, false);
            return new ViewHolderFoot(view);
        } else if (viewType == Constants.TYPE_ONE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_writeinfo_five, parent, false);
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
        String name = mDatas0.get(position);
        holder.mItemNameTv.setText(name);

        holder.mItemTopV.setVisibility(position == 0 ? View.VISIBLE : View.GONE);

        List<String> list = mDatas.get(position);

        if (list.size() == 0) {
            holder.mLayout11.setVisibility(View.VISIBLE);
            holder.mLayout12.setVisibility(View.GONE);
            holder.mLayout13.setVisibility(View.GONE);
            holder.mDelete11Iv.setVisibility(View.GONE);
//            holder.mIamge11Iv.setImageResource(R.drawable.add_img);
            ImageUtil.getInstance().displayImage(mContext, R.drawable.add_img, holder.mIamge11Iv);
        } else if (list.size() == 1) {
            holder.mLayout11.setVisibility(View.VISIBLE);
            holder.mDelete11Iv.setVisibility(View.VISIBLE);
            holder.mLayout12.setVisibility(View.VISIBLE);
            holder.mDelete12Iv.setVisibility(View.GONE);
            holder.mLayout13.setVisibility(View.GONE);

            String path = "";
            String str = list.get(0);
            if (str.startsWith("/Image")||str.startsWith("/image")) {
                path = Constants.BASE_URL + str;
            } else {
                path = str;
            }
//            ImageUtil.getInstance().displayRoundImage(mContext, path, holder.mIamge11Iv, 50);
            ImageUtil.getInstance().displayImage(mContext, path, holder.mIamge11Iv);
//            holder.mIamge12Iv.setImageResource(R.drawable.add_img);
            ImageUtil.getInstance().displayImage(mContext, R.drawable.add_img, holder.mIamge12Iv);
        } else if (list.size() == 2) {
            holder.mLayout11.setVisibility(View.VISIBLE);
            holder.mDelete11Iv.setVisibility(View.VISIBLE);
            holder.mLayout12.setVisibility(View.VISIBLE);
            holder.mDelete12Iv.setVisibility(View.VISIBLE);
            holder.mLayout13.setVisibility(View.VISIBLE);
            holder.mDelete13Iv.setVisibility(View.GONE);
            String path = "";
            String str = list.get(0);
            if (str.startsWith("/Image")||str.startsWith("/image")) {
                path = Constants.BASE_URL + str;
            } else {
                path = str;
            }

            ImageUtil.getInstance().displayImage(mContext, path, holder.mIamge11Iv);
//            ImageUtil.getInstance().displayRoundImage(mContext, path, holder.mIamge11Iv, 50);
            String path2 = "";
            String str2 = list.get(1);
            if (str2.startsWith("/Image")||str2.startsWith("/image")) {
                path2 = Constants.BASE_URL + str2;
            } else {
                path2 = str2;
            }

            ImageUtil.getInstance().displayImage(mContext, path2, holder.mIamge12Iv);
//            ImageUtil.getInstance().displayRoundImage(mContext, path2, holder.mIamge12Iv, 50);
//            holder.mIamge13Iv.setImageResource(R.drawable.add_img);
            ImageUtil.getInstance().displayImage(mContext, R.drawable.add_img, holder.mIamge13Iv);
        } else if (list.size() >= 3) {
            holder.mLayout11.setVisibility(View.VISIBLE);
            holder.mDelete11Iv.setVisibility(View.VISIBLE);
            holder.mLayout12.setVisibility(View.VISIBLE);
            holder.mDelete12Iv.setVisibility(View.VISIBLE);
            holder.mLayout13.setVisibility(View.VISIBLE);
            holder.mDelete13Iv.setVisibility(View.VISIBLE);

            String path = "";
            String str = list.get(0);
            if (str.startsWith("/Image")||str.startsWith("/image")) {
                path = Constants.BASE_URL + str;
            } else {
                path = str;
            }
//            ImageUtil.getInstance().displayRoundImage(mContext, path, holder.mIamge11Iv, 50);
            ImageUtil.getInstance().displayImage(mContext, path, holder.mIamge11Iv);
            String path2 = "";
            String str2 = list.get(1);
            if (str2.startsWith("/Image")||str2.startsWith("/image")) {
                path2 = Constants.BASE_URL + str2;
            } else {
                path2 = str2;
            }

            ImageUtil.getInstance().displayImage(mContext, path2, holder.mIamge12Iv);
//            ImageUtil.getInstance().displayRoundImage(mContext, path2, holder.mIamge12Iv, 50);
            String path3 = "";
            String str3 = list.get(2);
            if (str3.startsWith("/Image")||str3.startsWith("/image")) {
                path3 = Constants.BASE_URL + str3;
            } else {
                path3 = str3;
            }

            ImageUtil.getInstance().displayImage(mContext, path3, holder.mIamge13Iv);
//            ImageUtil.getInstance().displayRoundImage(mContext, path3, holder.mIamge13Iv, 50);
        }

        holder.mIamge11Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null) {
                    mListener.onItemImageClick(v,position,0);
                }
            }
        });
        holder.mIamge12Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null) {
                    mListener.onItemImageClick(v,position,1);
                }
            }
        });
        holder.mIamge13Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null) {
                    mListener.onItemImageClick(v,position,2);
                }
            }
        });

        holder.mDelete11Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null) {
                    mListener.onItemdeleteClick(position,0);
                }
            }
        });
        holder.mDelete12Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null) {
                    mListener.onItemdeleteClick(position,1);
                }
            }
        });
        holder.mDelete13Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null) {
                    mListener.onItemdeleteClick(position,2);
                }
            }
        });

    }


    //设置底部foot
    private void setDataFoot(ViewHolderFoot holder) {
        holder.mNextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null) {
                    mListener.onItemBottonClick();
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return 12;
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

        void onItemImageClick(View view,int position, int position2);  //position是item条目位置，position2是item中iamge的位置

        void onItemdeleteClick(int position, int position2);//position是item条目位置，position2是item中delete图标的位置

        void onItemBottonClick();

    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }


    static class ViewHolderFoot extends RecyclerView.ViewHolder {
        @BindView(R.id.next_tv)
        TextView mNextTv;

        ViewHolderFoot(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderType1 extends RecyclerView.ViewHolder {
        @BindView(R.id.item_top_v)
        View mItemTopV;
        @BindView(R.id.iamge11_iv)
        PorterShapeImageView mIamge11Iv;
        @BindView(R.id.delete11_iv)
        ImageView mDelete11Iv;
        @BindView(R.id.layout11)
        FrameLayout mLayout11;
        @BindView(R.id.iamge12_iv)
        PorterShapeImageView mIamge12Iv;
        @BindView(R.id.delete12_iv)
        ImageView mDelete12Iv;
        @BindView(R.id.layout12)
        FrameLayout mLayout12;
        @BindView(R.id.iamge13_iv)
        PorterShapeImageView mIamge13Iv;
        @BindView(R.id.delete13_iv)
        ImageView mDelete13Iv;
        @BindView(R.id.layout13)
        FrameLayout mLayout13;
        @BindView(R.id.item_name_tv)
        TextView mItemNameTv;

        ViewHolderType1(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
