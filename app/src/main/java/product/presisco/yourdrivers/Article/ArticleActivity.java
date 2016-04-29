package product.presisco.yourdrivers.Article;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import product.presisco.yourdrivers.DataModel.Article;
import product.presisco.yourdrivers.Network.Constants;
import product.presisco.yourdrivers.Network.Task.ArticleRequest;
import product.presisco.yourdrivers.Network.VolleyPlusRes;
import product.presisco.yourdrivers.R;

public class ArticleActivity extends AppCompatActivity {
    public static final String TAG = ArticleActivity.class.getSimpleName();
    public static final String PAGE_LINK = "page_link";

    String page_link = "";
    RecyclerView mContentView;
    ArticleContentAdapter mAdapter;
    Article mArticle;
    ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        page_link = getIntent().getStringExtra(PAGE_LINK);

        mContentView = (RecyclerView) findViewById(R.id.contentContainer);
        mLoading = (ProgressBar) findViewById(R.id.progressBar);

        mAdapter = new ArticleContentAdapter(this, new OnPageListener());
        mContentView.setLayoutManager(new LinearLayoutManager(this));

        getContent(page_link);

    }

    private void getContent(String relative_url) {
        if (relative_url == "" || relative_url == null || relative_url.equals(Constants.MOBILE_WEB_HOST)) {
            return;
        }
        VolleyPlusRes.getRequestQueue().add(new ArticleRequest(Request.Method.GET,
                Constants.MOBILE_WEB_HOST + relative_url,
                new OnLoadCompleteListener(),
                new OnLoadFailedListener()));
        mLoading.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.INVISIBLE);
    }

    private class OnLoadCompleteListener implements ArticleRequest.OnLoadCompleteListener {
        @Override
        public void onLoadComplete(Article art) {
            mArticle = art;
            mAdapter.updateDataSrc(mArticle);
            mContentView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mContentView.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.INVISIBLE);
        }
    }

    private class OnLoadFailedListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(ArticleActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class OnPageListener implements ArticleContentAdapter.OnPageSelectionListener {
        @Override
        public void onAllLeft() {
            getContent(mArticle.all_link);
        }

        @Override
        public void onNextPage() {
            getContent(mArticle.next_link);
        }

        @Override
        public void onPreviousPage() {
            getContent(mArticle.prev_link);
        }
    }
}
