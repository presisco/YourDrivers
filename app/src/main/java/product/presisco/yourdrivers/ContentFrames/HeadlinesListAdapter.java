package product.presisco.yourdrivers.ContentFrames;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Headline;
import product.presisco.yourdrivers.Network.VolleyPlusRes;
import product.presisco.yourdrivers.R;

/**
 * TODO: document your custom view class.
 */
public class HeadlinesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = HeadlinesListAdapter.class.getSimpleName();

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_CONTENT = 1;
    private static final int VIEWTYPE_FOOTER = 2;
    private static final int VIEWTYPE_CONTENT_NOICON = 10;
    private static final int VIEWTYPE_CONTENT_ICON = 11;
    private static final int VIEWTYPE_CONTENT_ICONS = 12;

    private List<Headline> mContents = new ArrayList<>();
    private OnContentItemClickedListener mContentItemClickedListener = null;
    private OnFooterShowedListener mFooterShowedListener = null;

    public HeadlinesListAdapter() {
        this(null);
    }

    public HeadlinesListAdapter(OnContentItemClickedListener l) {
        this(l, null);
    }

    public HeadlinesListAdapter(OnContentItemClickedListener itemClickedListener,
                                OnFooterShowedListener footerShowedListener) {
        super();
        mContentItemClickedListener = itemClickedListener;
        mFooterShowedListener = footerShowedListener;
    }

    public void setDataSrc(List<Headline> src) {
        mContents = src;
    }

    @Override
    public int getItemCount() {
        if (mContents == null || mContents.size() == 0) {
            return 0;
        } else {
            return mContents.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mContents.size()) {
            return VIEWTYPE_FOOTER;
        }
        if (mContents.get(position).icon == null) {
            return VIEWTYPE_CONTENT_NOICON;
        }
        switch (mContents.get(position).icon.length) {
            case 1:
                return VIEWTYPE_CONTENT_ICON;
            case 3:
                return VIEWTYPE_CONTENT_ICONS;
            default:
                return VIEWTYPE_CONTENT_NOICON;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            ContentViewHolder cHolder = (ContentViewHolder) holder;
            Headline headline = mContents.get(position);
            cHolder.mTitle.setText(headline.title);
            cHolder.mWriter.setText(headline.writer);
            cHolder.mDate.setText("");
            cHolder.mComm.setText(headline.comments_count);
            if (cHolder instanceof IconViewHolder) {
                IconViewHolder iHolder = (IconViewHolder) cHolder;
                for (int i = 0; i < headline.icon.length; ++i) {
                    VolleyPlusRes.getImageLoader().get(headline.icon[i],
                            ImageLoader.getImageListener(iHolder.imageViews[i], R.drawable.empty_photo, R.drawable.error_image));
                }
            }
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
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEWTYPE_CONTENT_NOICON:
                holder = new ContentViewHolder(li.inflate(R.layout.cardview_topic, parent, false));
                break;
            case VIEWTYPE_CONTENT_ICON:
                holder = new IconViewHolder(li.inflate(R.layout.cardview_topic_icon, parent, false), 1);
                break;
            case VIEWTYPE_CONTENT_ICONS:
                holder = new IconViewHolder(li.inflate(R.layout.cardview_topic_icons, parent, false), 3);
                break;
            case VIEWTYPE_FOOTER:
                holder = new FooterViewHolder(li.inflate(R.layout.list_footer, parent, false));
                break;
            default:
                Log.d(TAG, "unrecognized viewtype:" + viewType);
        }
        return holder;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public interface OnContentItemClickedListener {
        void onContentItemClicked(int pos);
    }

    public interface OnFooterShowedListener {
        void onFooterShowed();
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle;
        public final TextView mWriter;
        public final TextView mDate;
        public final TextView mComm;

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
            mWriter = (TextView) itemView.findViewById(R.id.textWriter);
            mDate = (TextView) itemView.findViewById(R.id.textDate);
            mComm = (TextView) itemView.findViewById(R.id.textComm);
        }
    }

    private class IconViewHolder extends ContentViewHolder {
        public final ImageView[] imageViews;

        public IconViewHolder(View itemView, int count) {
            super(itemView);
            if (count == 1) {
                imageViews = new ImageView[]{
                        (ImageView) itemView.findViewById(R.id.iconView)
                };
            } else {
                imageViews = new ImageView[]{
                        (ImageView) itemView.findViewById(R.id.iconView),
                        (ImageView) itemView.findViewById(R.id.iconView2),
                        (ImageView) itemView.findViewById(R.id.iconView3)
                };
            }
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
