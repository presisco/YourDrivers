package product.presisco.yourdrivers.Article;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import product.presisco.yourdrivers.DataModel.Article;
import product.presisco.yourdrivers.Network.VolleyPlusRes;
import product.presisco.yourdrivers.R;

/**
 * Created by presisco on 2016/4/29.
 */
public class ArticleContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ArticleContentAdapter.class.getSimpleName();

    private static final int VIEWTYPE_UNKNOWN = -1;
    private static final int VIEWTYPE_TITLE = 0;
    private static final int VIEWTYPE_TEXT = 1;
    private static final int VIEWTYPE_IMAGES = 2;
    private static final int VIEWTYPE_FOOTER = 3;
    private Article mArticle;
    private OnPageSelectionListener mPageSelectionListener;
    private Context mContext;

    public ArticleContentAdapter(Context context, OnPageSelectionListener pageSelectionListener) {
        super();
        mContext = context;
        mPageSelectionListener = pageSelectionListener;
    }

    @Override
    public int getItemCount() {
        int count = 1;
        if (mArticle.isMultPage) {
            ++count;
        }
        count += mArticle.contents.size();
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEWTYPE_TITLE;
        } else if (position == getItemCount() + 1) {
            return VIEWTYPE_FOOTER;
        } else if (mArticle.contents.get(position - 1).type == Article.Text.TAG) {
            return VIEWTYPE_TEXT;
        } else if (mArticle.contents.get(position - 1).type == Article.Images.TAG) {
            return VIEWTYPE_IMAGES;
        } else {
            return VIEWTYPE_UNKNOWN;
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder vh = null;
        switch (viewType) {
            case VIEWTYPE_TITLE:
                vh = new TitleHolder(li.inflate(R.layout.article_title, parent, false));
                break;
            case VIEWTYPE_TEXT:
                vh = new TextHolder(li.inflate(R.layout.article_content_text, parent, false));
                break;
            case VIEWTYPE_IMAGES:
                vh = new ImagesHolder(li.inflate(R.layout.article_content_images, parent, false));
                break;
            case VIEWTYPE_FOOTER:
                vh = new FooterHolder(li.inflate(R.layout.article_page_selection, parent, false));
                break;
            default:
                Log.d(TAG, "unrecognized viewtype:" + viewType);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextHolder) {
            TextHolder textHolder = (TextHolder) holder;
            Article.Text text = (Article.Text) mArticle.contents.get(position - 1);
            textHolder.content.setText(text.text);
        } else if (holder instanceof ImagesHolder) {
            ImagesHolder imagesHolder = (ImagesHolder) holder;
            Article.Images images = (Article.Images) mArticle.contents.get(position - 1);
            if (imagesHolder.imagesContainer.getChildCount() < images.images.length) {
                for (int i = imagesHolder.imagesContainer.getChildCount(); i < images.images.length; ++i) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imagesHolder.imagesContainer.addView(imageView, i,
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT));
                }
            } else {
                imagesHolder.imagesContainer.removeViews(images.images.length,
                        imagesHolder.imagesContainer.getChildCount() - images.images.length);
            }
            for (int i = 0; i < imagesHolder.imagesContainer.getChildCount(); ++i) {
                VolleyPlusRes.getImageLoader().get(images.images[i],
                        ImageLoader.getImageListener((ImageView) imagesHolder.imagesContainer.getChildAt(i),
                                R.drawable.empty_photo, R.drawable.error_image));
            }
        } else if (holder instanceof TitleHolder) {
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.title.setText(mArticle.title);
            titleHolder.writer.setText(mArticle.writer);
            titleHolder.date.setText(mArticle.date);
        }
    }

    public void updateDataSrc(Article art) {
        mArticle = art;
    }

    public interface OnPageSelectionListener {
        void onNextPage();

        void onPreviousPage();

        void onAllLeft();
    }

    private class TextHolder extends RecyclerView.ViewHolder {
        final TextView content;

        public TextHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.textContent);
        }
    }

    private class ImagesHolder extends RecyclerView.ViewHolder {
        final LinearLayout imagesContainer;

        public ImagesHolder(View itemView) {
            super(itemView);
            imagesContainer = (LinearLayout) itemView.findViewById(R.id.imagesContainer);
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder {
        public FooterHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.textPrevious).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPageSelectionListener.onPreviousPage();
                }
            });
            itemView.findViewById(R.id.textAllLeft).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPageSelectionListener.onAllLeft();
                }
            });
            itemView.findViewById(R.id.textNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPageSelectionListener.onNextPage();
                }
            });
        }
    }

    private class TitleHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView writer;
        final TextView date;

        public TitleHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textTitle);
            writer = (TextView) itemView.findViewById(R.id.textWriter);
            date = (TextView) itemView.findViewById(R.id.textDate);
        }
    }
}
