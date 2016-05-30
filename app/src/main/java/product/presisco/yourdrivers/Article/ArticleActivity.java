package product.presisco.yourdrivers.Article;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;

import product.presisco.yourdrivers.Comment.CommentFragment;
import product.presisco.yourdrivers.DataModel.Article;
import product.presisco.yourdrivers.Network.Request.ArticleRequest;
import product.presisco.yourdrivers.Network.Request.ExtendedRequest;
import product.presisco.yourdrivers.Network.VolleyPlusRes;
import product.presisco.yourdrivers.R;

public class ArticleActivity extends AppCompatActivity {
    public static final String TAG = ArticleActivity.class.getSimpleName();
    public static final String ARTICLE_ID = "article_id";

    String article_id = "";
    RecyclerView mContentView;
    ArticleContentAdapter mAdapter;
    Article mArticle;
    ProgressBar mLoading;
    CommentFragment mCommentFragment;

    TextView textTitle;
    TextView textWriter;
    TextView textDate;

    View headerView;
    View footerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        article_id = getIntent().getStringExtra(ARTICLE_ID);

        textTitle = (TextView) findViewById(R.id.textTitle);
        textWriter = (TextView) findViewById(R.id.textWriter);
        textDate = (TextView) findViewById(R.id.textDate);

        headerView = findViewById(R.id.includeHeader);
        headerView.setVisibility(View.GONE);
        footerView = findViewById(R.id.includeFooter);

        OnPageListener listener = new OnPageListener();
        footerView.findViewById(R.id.textPrevious).setOnClickListener(listener);
        footerView.findViewById(R.id.textAllLeft).setOnClickListener(listener);
        footerView.findViewById(R.id.textNext).setOnClickListener(listener);
        footerView.setVisibility(View.GONE);

        mContentView = (RecyclerView) findViewById(R.id.contentList);
        mLoading = (ProgressBar) findViewById(R.id.progressBar);

        mAdapter = new ArticleContentAdapter(this);
        mContentView.setLayoutManager(new LinearLayoutManager(this));

        mCommentFragment = CommentFragment.newInstance(false, article_id);
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.commentFrame, mCommentFragment);
        trans.commit();

        getContent(article_id);
    }

    private void getContent(String id) {
        if (id == "" || id == null) {
            return;
        }
        VolleyPlusRes.getRequestQueue().add(
                new ArticleRequest(
                        id,
                        new OnLoadCompleteListener(),
                        new OnLoadFailedListener()));
        mLoading.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
    }

    private class OnLoadCompleteListener implements ExtendedRequest.OnLoadCompleteListener<Article> {

        @Override
        public void onLoadComplete(Article art) {
            mArticle = art;
            textTitle.setText(mArticle.header.title);
            textWriter.setText(mArticle.header.writer);
            textDate.setText(mArticle.header.date);
            headerView.setVisibility(View.VISIBLE);
            mAdapter.updateDataSrc(mArticle);
            mContentView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mContentView.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.GONE);
            if (mArticle.isMultPage) {
                footerView.setVisibility(View.VISIBLE);
            } else {
                footerView.setVisibility(View.GONE);
            }
        }
    }

    private class OnLoadFailedListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(ArticleActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class OnPageListener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            String link = "";
            switch (v.getId()) {
                case R.id.textPrevious:
                    link = mArticle.prev_link;
                    break;
                case R.id.textAllLeft:
                    link = mArticle.all_link;
                    break;
                case R.id.textNext:
                    link = mArticle.next_link;
                    break;
                default:
                    return;
            }
            getContent(link);
        }
    }
}
