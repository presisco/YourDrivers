package product.presisco.yourdrivers.Comment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Article;
import product.presisco.yourdrivers.DataModel.CommentSet;
import product.presisco.yourdrivers.Network.Constants;
import product.presisco.yourdrivers.Network.GeneralLoadFailedListener;
import product.presisco.yourdrivers.Network.Request.CommentHeaderRequest;
import product.presisco.yourdrivers.Network.Request.CommentRequest;
import product.presisco.yourdrivers.Network.Request.ExtendedRequest;
import product.presisco.yourdrivers.Network.VolleyPlusRes;
import product.presisco.yourdrivers.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String HAVE_HEADER = "have_header";
    private static final String ARTICLE_ID = "article_id";
    TextView textTitle;
    TextView textWriter;
    TextView textFloor;
    ProgressBar progressBar;
    // TODO: Rename and change types of parameters
    private boolean have_header;
    private String article_id;
    private String type = Constants.REQUEST_COMMENT_TYPE_FLOOR;
    private String orderby = Constants.REQUEST_COMMENT_ORDER_NEW;
    private int page = 1;
    private List<CommentSet.Comment> commentList = new ArrayList<>();
    private RecyclerView mCommentRecyclerView;
    private CommentAdapter mCommentAdapter;
    private TextView mClickToLoad;

    public CommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param have_header show header.
     * @param article_id  article id.
     * @return A new instance of fragment CommentFragment.
     */
    public static CommentFragment newInstance(boolean have_header, String article_id) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putBoolean(HAVE_HEADER, have_header);
        args.putString(ARTICLE_ID, article_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            have_header = getArguments().getBoolean(HAVE_HEADER);
            article_id = getArguments().getString(ARTICLE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (have_header) {
            return inflater.inflate(R.layout.fragment_comment, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_comment_without_header, container, false);
        }
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCommentRecyclerView = (RecyclerView) view.findViewById(R.id.commentListView);
        mCommentAdapter = new CommentAdapter();
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCommentRecyclerView.setAdapter(mCommentAdapter);

        view.findViewById(R.id.textTypeDate).setOnClickListener(this);
        view.findViewById(R.id.textTypeHeat).setOnClickListener(this);
        view.findViewById(R.id.textAscending).setOnClickListener(this);
        view.findViewById(R.id.textDescending).setOnClickListener(this);
        mClickToLoad = (TextView) view.findViewById(R.id.textClickToLoad);
        mClickToLoad.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        if (have_header) {
            textTitle = (TextView) view.findViewById(R.id.textTitle);
            textWriter = (TextView) view.findViewById(R.id.textWriter);
            textFloor = (TextView) view.findViewById(R.id.textDate);
            requestHeader();
        }
        requestContent();
    }

    private void requestHeader() {
        VolleyPlusRes.getRequestQueue().add(
                new CommentHeaderRequest(
                        article_id
                        , new OnLoadHeaderComplete()
                        , new GeneralLoadFailedListener(getContext())));
    }

    private void requestContent() {
        progressBar.setVisibility(View.VISIBLE);
        VolleyPlusRes.getRequestQueue().add(
                new CommentRequest(
                        article_id
                        , type
                        , page
                        , orderby
                        , new OnLoadCommentComplete()
                        , new GeneralLoadFailedListener(getContext())));
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textTypeDate:
                page = 1;
                commentList.clear();
                type = Constants.REQUEST_COMMENT_TYPE_FLOOR;
                break;
            case R.id.textTypeHeat:
                page = 1;
                commentList.clear();
                type = Constants.REQUEST_COMMENT_TYPE_HEAT;
                break;
            case R.id.textAscending:
                page = 1;
                commentList.clear();
                orderby = Constants.REQUEST_COMMENT_ORDER_NEW;
                break;
            case R.id.textDescending:
                page = 1;
                commentList.clear();
                orderby = Constants.REQUEST_COMMENT_ORDER_OLD;
                break;
            case R.id.textClickToLoad:
                page++;
                break;
            default:
                return;
        }
        requestContent();
    }

    private class OnLoadHeaderComplete implements ExtendedRequest.OnLoadCompleteListener<Article.Header> {
        @Override
        public void onLoadComplete(Article.Header data) {
            textTitle.setText(data.title);
            textWriter.setText(data.writer);
            textFloor.setText(data.date);
        }
    }

    private class OnLoadCommentComplete implements ExtendedRequest.OnLoadCompleteListener<CommentSet> {
        @Override
        public void onLoadComplete(CommentSet data) {
            if (data == null) {
                return;
            }
            commentList.addAll(data.All);
            if ((commentList.size() + "").equals(data.Count.get(0).ReviewCount)) {
                mClickToLoad.setVisibility(View.GONE);
            }
            mCommentAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ContentHolder> {

        @Override
        public int getItemCount() {
            if (commentList == null) {
                return 0;
            } else {
                return commentList.size();
            }
        }

        @Override
        public void onBindViewHolder(ContentHolder holder, int position) {
            CommentSet.Comment comment = commentList.get(position);
            //holder.mContent.setText(comment.Content);
            holder.mContent.setText(Html.fromHtml(comment.Content));
            holder.mDate.setText(comment.PostDate);
            if (!comment.UserName.equals("")) {
                holder.mUsername.setText(comment.UserName);
            } else {
                holder.mUsername.setText(getResources().getString(R.string.text_anonymous));
            }
            holder.mFloor.setText(comment.Floor);
            holder.mSupport.setText(comment.Support);
            holder.mOppose.setText(comment.Oppose);
            holder.mLocation.setText(comment.IPAdd);
            if (comment.RevertContent != null && !comment.RevertContent.equals("")) {
                holder.mQuoteContent.setText(Html.fromHtml(comment.RevertContent));
                holder.mQuoteUsername.setText(comment.RevertUsername);
            } else {
                holder.mQuoteCard.setVisibility(View.GONE);
            }
        }

        @Override
        public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContentHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.cardview_comment, parent, false));
        }

        @Override
        public void onViewRecycled(ContentHolder holder) {
            super.onViewRecycled(holder);
        }

        public class ContentHolder extends RecyclerView.ViewHolder {
            final View rootView;
            final TextView mContent;
            final TextView mUsername;
            final TextView mLocation;
            final TextView mDate;
            final TextView mSupport;
            final TextView mOppose;
            final TextView mQuoteContent;
            final TextView mQuoteUsername;
            final TextView mFloor;
            final View mQuoteCard;

            public ContentHolder(View itemView) {
                super(itemView);
                rootView = itemView;
                mContent = get(R.id.textContent);
                mUsername = get(R.id.textUsername);
                mLocation = get(R.id.textLocation);
                mDate = get(R.id.textDate);
                mSupport = get(R.id.textSupportCount);
                mOppose = get(R.id.textOpposeCount);
                mQuoteContent = get(R.id.textQuoteContent);
                mQuoteUsername = get(R.id.textQuoteUsername);
                mFloor = get(R.id.textFloor);
                mQuoteCard = rootView.findViewById(R.id.quoteCard);
            }

            private TextView get(int resid) {
                return (TextView) rootView.findViewById(resid);
            }
        }
    }

}
