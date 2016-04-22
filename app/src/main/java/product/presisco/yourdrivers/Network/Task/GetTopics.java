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

    private class TopicsListParser {
        private final String ns = null;
        private String path[];
        private String elementEntry;

        public void parse(InputStream in, String _path[], String entry) throws XmlPullParserException, IOException {
            try {
                path = _path;
                elementEntry = entry;
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                readFeed(parser, 0);
            } finally {
                in.close();
            }
        }

        private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "html");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("body")) {
                    break;
                } else {
                    skip(parser);
                }
            }
        }

        private void readFeed(XmlPullParser parser, int depth) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, path[depth++]);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (depth == path.length) {
                    if (name.equals(elementEntry))
                        readData(parser);
                    else
                        skip(parser);
                } else if (name.equals(path[depth])) {
                    readFeed(parser, depth);
                } else {
                    skip(parser);
                }
            }
        }

        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

        // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
        // to their respective "read" methods for processing. Otherwise, skips the tag.
        private void readData(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "input");
            String name = parser.getAttributeValue(null, "name");
            String id = parser.getAttributeValue(null, "id");
            if (name.equals("__VIEWSTATE") && id.equals("__VIEWSTATE")) {
                //    view_state=parser.getAttributeValue(null,"value");
            } else if (name.equals("__EVENTVALIDATION") && id.equals("__EVENTVALIDATION")) {
                //    event_validation=parser.getAttributeValue(null,"value");
            }
            parser.nextTag();
            parser.require(XmlPullParser.END_TAG, ns, "input");
        }
    }
}
