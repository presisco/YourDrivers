package product.presisco.yourdrivers.Network.Task;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Article;
import product.presisco.yourdrivers.Network.Constants;

/**
 * Created by presisco on 2016/4/29.
 */
public class ArticleRequest extends ExtendedRequest<Article> {
    String id;

    public ArticleRequest(String article_id, ExtendedRequest.OnLoadCompleteListener<Article> loadCompleteListener, Response.ErrorListener listener) {
        super(Method.GET, Constants.REQUEST_ARITCLE.replace(Constants.REQUEST_ARITCLE_REPLACE, article_id), loadCompleteListener, listener);
        id = article_id;
    }

    @Override
    protected Response<Article> parseNetworkResponse(NetworkResponse networkResponse) {
        Article parsed = new Article();
        parsed.header.tid = id;
        Document doc = Jsoup.parse(getStringFromResponse(networkResponse));
        Element bodyEle = doc.body();
        addArticleHead(parsed.header, bodyEle.getElementsByClass("news_content").first());
        addArticleContent(parsed, bodyEle.getElementById("content"));
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    private void addArticleHead(Article.Header head, Element headEle) {
        head.title = headEle.getElementsByClass("news_t").first().text();
        Elements writerndate = headEle.getElementsByClass("news_t1").first().child(0).children();
        head.writer = writerndate.first().text();
        head.date = writerndate.get(2).text();
    }

    private void addArticleContent(Article art, Element src) {
        Elements contentEles = src.children();
        List<String> images = new ArrayList<>();
        boolean isMergingImgs = false;
        String textBuff = "";
        for (Element ele : contentEles) {
            switch (ele.tagName()) {
                case "p":
                    if (ele.hasAttr("align")) {
                        if (!isMergingImgs) {
                            art.contents.add(new Article.Text(textBuff));
                            isMergingImgs = true;
                        }
                        images.add(ele.getElementsByTag("img").first().attr("src"));
                        String text = ele.text();
                        if (ele.text() != "") {
                            art.contents.add(new Article.Images(images.toArray(new String[0])));
                            images.clear();
                            isMergingImgs = false;
                            textBuff = "";
                            art.contents.add(new Article.Text(text));
                        }
                    } else {
                        if (isMergingImgs) {
                            art.contents.add(new Article.Images(images.toArray(new String[0])));
                            images.clear();
                            isMergingImgs = false;
                            textBuff = ele.text();
                        } else {
                            textBuff = textBuff + ele.toString();
                        }
                    }
                    break;
                case "div":
                    art.isMultPage = true;
                    art.all_link = ele.child(0).attr("href");
                    art.prev_link = ele.child(1).child(0).attr("href");
                    art.next_link = ele.child(1).child(1).attr("href");
                    break;
                case "a":
                    art.ref_name = ele.text();
                    art.ref_link = ele.attr("href");
                    break;
            }
        }
        if (isMergingImgs) {
            art.contents.add(new Article.Images(images.toArray(new String[0])));
            images.clear();
        } else {
            art.contents.add(new Article.Text(textBuff));
        }
        images = null;
        textBuff = null;
    }

}
