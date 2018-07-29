package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.kanade.loadmoredemotwo.R;

import java.util.List;

import bean.Fruit;
import callback.RecyclerOnScrollerListener;

public class FruitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Fruit> mFruitList;
    private Context mContext;
    private RecyclerOnScrollerListener mOnScrollListener;
//    private RecyclerView.AdapterDataObserver mAdapterDataObserver;

    private static final int VIEW_TYPE_CONTENT = 0;
    private static final int VIEW_TYPE_FOOTER = 1;
    private boolean isCanLoadMore = true;

    private Animation rotateAnimation;

    private static final int PER_PAGE = 10;

    public FruitAdapter(List<Fruit> fruitList) {
        this.mFruitList = fruitList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (rotateAnimation == null) {
            rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading);
            rotateAnimation.setInterpolator(new LinearInterpolator());
        }
        if (viewType == VIEW_TYPE_CONTENT) {
            return new ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_fruit, parent, false));
        } else {

            return new FooterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_footer, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_CONTENT) {
            Fruit fruit = mFruitList.get(position);
            //这里必须强制转换
            //如果外层的判断条件改为if(holder instance ContentViewHolder)，这里输入holder后会自动转换
            ((ContentViewHolder) holder).tvName.setText(fruit.getName());
            ((ContentViewHolder) holder).ivImage.setImageResource(fruit.getImageId());
        } else {
            Log.d("mytest", "isCanLoadMore: " + isCanLoadMore);
            if (isCanLoadMore) {
                ((FooterViewHolder) holder).showLoading();
            } else {
                ((FooterViewHolder) holder).showTextOnly("无更多数据");
            }
        }

    }

    /*
    * 本例中所有数据加载完毕后还是保留footerView并显示已加载完全，所以footerView一直存在。
    * */
    @Override
    public int getItemCount() {
//        return isCanLoadMore ? mFruitList.size() + 1 : mFruitList.size();
        return mFruitList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_CONTENT;
    }

    //ContentView，水果们
    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName = itemView.findViewById(R.id.tv_name);
        ImageView ivImage = itemView.findViewById(R.id.iv_image);

        public ContentViewHolder(View itemView) {
            super(itemView);
        }
    }

    //底部的FooterView
    class FooterViewHolder extends RecyclerView.ViewHolder {

        ImageView ivLoading = itemView.findViewById(R.id.iv_loading);
        TextView tvLoading = itemView.findViewById(R.id.tv_loading);

        public FooterViewHolder(View itemView) {
            super(itemView);
        }

        void showTextOnly(String s) {
            Log.d("mytest", "showTextOnly: " + s);
            ivLoading.setVisibility(View.INVISIBLE);
            tvLoading.setText(s);
        }

        void showLoading() {
            Log.i("mytest", "show loading");
            ivLoading.setImageResource(R.mipmap.ic_launcher);
            tvLoading.setText("正在加载...");
            if (ivLoading != null) {
                ivLoading.startAnimation(rotateAnimation);
            }
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mOnScrollListener = new RecyclerOnScrollerListener(recyclerView) {
            @Override
            public void onLoadMore(int currentPage) {
                Log.i("loadingtest", "currentPage: " + currentPage);
                mOnLoadMoreListener.onLoadMore(currentPage);
            }
        };
        recyclerView.addOnScrollListener(mOnScrollListener);
//        mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//            }
//        };
        //初始化的时候如果未填满一页，那么肯定就没有更多数据了
        if (mFruitList.size() < PER_PAGE) {
            setCanLoadMore(false);
        } else {
            setCanLoadMore(true);
        }
    }


    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mOnScrollListener != null) {
            recyclerView.removeOnScrollListener(mOnScrollListener);
        }
//        if (mAdapterDataObserver != null) {
//            unregisterAdapterDataObserver(mAdapterDataObserver);
//        }
        mOnScrollListener = null;
    }

    public void setData(List<Fruit> list) {
        mFruitList = list;
        notifyDataSetChanged();
    }

    /*
    * 数据加载完毕时执行setCanLoadMore()，此时isLoading都置为false
    * */
    public void setCanLoadMore(boolean isCanLoadMore) {
        this.isCanLoadMore = isCanLoadMore;
        mOnScrollListener.setCanLoadMore(isCanLoadMore);
        mOnScrollListener.setLoading(false);
    }


    public interface OnLoadMoreListener {
        void onLoadMore(int currentPage);
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
    }

}
