package callback;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerOnScrollerListener extends RecyclerView.OnScrollListener {
    private int currentPage = 1;
    //是否正在加载
    private boolean isLoading = false;
    //是否能够加载更多
    private boolean isCanLoadMore = true;
    private RecyclerView mRecyclerView;

    public RecyclerOnScrollerListener(RecyclerView recyclerView) {
        super();
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //能够加载更多
        if (isCanLoadMore) {
            //已滑动到最底部
            if (isSlideToBottom()) {
                currentPage++;
                onLoadMore(currentPage);
                isLoading = true;
            }
        }
    }

    /*
    * 是否滑动到最底部
    *
    * !isLoading, 不在加载过程中
    * lastVisibleItemPosition == totalItemCount - 1, 当前可见的最后一个item是列表的最后一个
    * */
    private boolean isSlideToBottom() {
        if (mRecyclerView == null) {
            return false;
        }

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            return !isLoading && visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1;
        }
        return false;
    }

    //抽象方法，用来传递加载更多事件
    public abstract void onLoadMore(int currentPage);

    public void setCanLoadMore(boolean load) {
        isCanLoadMore = load;
    }

    public void setLoading(boolean load) {
        isLoading = load;
    }
}
