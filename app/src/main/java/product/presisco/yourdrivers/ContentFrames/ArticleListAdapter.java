package product.presisco.yourdrivers.ContentFrames;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Topic;
import product.presisco.yourdrivers.R;

/**
 * TODO: document your custom view class.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ArticleListAdapter.class.getSimpleName();

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_CONTENT = 1;
    private static final int VIEWTYPE_FOOTER = 2;

    private List<Topic> mContents = new ArrayList<>();
    private OnContentItemClickedListener mContentItemClickedListener = null;
    private OnFooterShowedListener mFooterShowedListener = null;

    public ArticleListAdapter() {
        this(null);
    }

    public ArticleListAdapter(OnContentItemClickedListener l) {
        this(l, null);
    }

    public ArticleListAdapter(OnContentItemClickedListener itemClickedListener,
                              OnFooterShowedListener footerShowedListener) {
        super();
        mContentItemClickedListener = itemClickedListener;
        mFooterShowedListener = footerShowedListener;
    }

    public void setDataSrc(List<Topic> src) {
        mContents = src;
    }

    @Override
    public int getItemCount() {
        if (mContents.size() == 0) {
            return 0;
        } else {
            return mContents.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mContents.size()) {
            return VIEWTYPE_CONTENT;
        } else {
            return VIEWTYPE_FOOTER;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).mTitle.setText(mContents.get(position).title);
        } else if (holder instanceof FooterViewHolder) {
            Log.d(TAG, "showing footer");
            if (mFooterShowedListener != null) {
                mFooterShowedListener.onFooterShowed();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case VIEWTYPE_CONTENT:
                holder = new ContentViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.cardview_topic, parent, false));
                break;
            case VIEWTYPE_FOOTER:
                holder = new FooterViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_footer, parent, false));
                break;
            default:
                Log.d(TAG, "unrecognized viewtype:" + viewType);
        }
        return holder;
    }

    public interface OnContentItemClickedListener {
        void onContentItemClicked(int pos);
    }

    public interface OnFooterShowedListener {
        void onFooterShowed();
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle;

        public ContentViewHolder(View itemView) {
            super(itemView);
            if (mContentItemClickedListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "clicked content item:" + getAdapterPosition());
                        mContentItemClickedListener.onContentItemClicked(getAdapterPosition());
                    }
                });
            }
            mTitle = (TextView) itemView.findViewById(R.id.textTitle);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
