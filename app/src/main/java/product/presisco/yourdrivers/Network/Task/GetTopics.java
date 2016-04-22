package product.presisco.yourdrivers.Network.Task;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Topic;
import product.presisco.yourdrivers.Network.Constants;
import product.presisco.yourdrivers.Network.Utils;

/**
 * Created by presisco on 2016/4/20.
 */
public class GetTopics extends AsyncTask<Void, Void, List<Topic>> {
    private OnLoadCompleteListener mOnLoadCompleteListener;

    public GetTopics setOnLoadCompleteListener(OnLoadCompleteListener l) {
        mOnLoadCompleteListener = l;
        return this;
    }

    @Override
    protected List<Topic> doInBackground(Void... params) {
        List<Topic> topics = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(new URL(Constants.MOBILE_WEB_HOST), 5000);
            Elements eles = doc.getElementById("mynewslist").children();
            for (Element ele : eles) {
                String display = "";
                if (ele.hasAttr("data-id")) {
                    display += "data-id:" + ele.attributes().get("data-id") + " ";
                } else if (ele.child(0).tagName() == "script") {
                    display += "a script";
                } else if (ele.hasAttr("id")) {
                    display += "id:" + ele.attributes().get("id") + " is ad";
                } else {
                    display += "is top msg";
                }
                Log.d("Jsoup", display);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topics;
    }

    @Override
    protected void onPostExecute(List<Topic> s) {
        super.onPostExecute(s);
        if (mOnLoadCompleteListener != null) {
            mOnLoadCompleteListener.onLoadComplete(s);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public interface OnLoadCompleteListener {
        void onLoadComplete(List<Topic> src);
    }
}
