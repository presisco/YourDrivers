package product.presisco.yourdrivers.Article;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import product.presisco.yourdrivers.Network.Task.GetAritcle;
import product.presisco.yourdrivers.R;

public class ArticleActivity extends AppCompatActivity {
    public static final String TAG = ArticleActivity.class.getSimpleName();
    public static final String SRC_ADDR = "src_addr";

    String src_addr = "";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        webView = (WebView) findViewById(R.id.webView);
        src_addr = getIntent().getStringExtra(SRC_ADDR);
        new GetAritcle().setOnLoadCompleteListener(new OnLoadComplete()).execute(src_addr);
    }

    private class OnLoadComplete implements GetAritcle.OnLoadCompleteListener {
        @Override
        public void onLoadComplete(String src) {
            webView.loadData(src, "text/html", "UTF-8");
        }
    }
}
