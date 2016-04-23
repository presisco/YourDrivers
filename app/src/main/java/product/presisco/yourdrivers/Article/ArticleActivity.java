package product.presisco.yourdrivers.Article;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import product.presisco.yourdrivers.Network.Task.GetAritcle;
import product.presisco.yourdrivers.R;

public class ArticleActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        webView = (WebView) findViewById(R.id.webView);
    }

    private class OnLoadComplete implements GetAritcle.OnLoadCompleteListener {
        @Override
        public void onLoadComplete(String src) {
            webView.loadData(src, "text/html", "UTF-8");
        }
    }
}
