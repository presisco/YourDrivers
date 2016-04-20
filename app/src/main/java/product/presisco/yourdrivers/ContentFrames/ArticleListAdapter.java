package product.presisco.yourdrivers.ContentFrames;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Topic;
import product.presisco.yourdrivers.R;

/**
 * TODO: document your custom view class.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Topic> mContents = new ArrayList<>();
    private OnDifferentStateListener mListener = null;

    public ArticleListAdapter() {
        this(null);
    }

    public ArticleListAdapter(OnDifferentStateListener l) {
        super();
        mListener = l;
    }

    public void setDataSrc(List<Topic> src) {
        mContents = src;
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_news, parent, false);
        return new ContentViewHolder(v);
    }

    public interface OnDifferentStateListener {
        void onItemClicked(int pos);

        void onFooterShowed();
    }

    private static class ContentViewHolder extends RecyclerView.ViewHolder {
        public ContentViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
