package product.presisco.yourdrivers.Network.Task;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private static final String PATH = "/api/2/contents/newstoppic";
    private OnLoadCompleteListener mOnLoadCompleteListener;

    public GetTopics setOnLoadCompleteListener(OnLoadCompleteListener l) {
        mOnLoadCompleteListener = l;
        return this;
    }

    @Override
    protected List<Topic> doInBackground(Void... params) {
        List<Topic> topics = new ArrayList<>();
        try {
            List<Pair> get_params = new ArrayList<>();
            get_params.add(new Pair("id", System.currentTimeMillis() + ""));
            HttpURLConnection connection = (HttpURLConnection)
                    //new URL(Constants.HOST + "/api/2/contents/newstoppic" + "?" + "id=" + System.currentTimeMillis()).openConnection();
                    Utils.getURLWithParams(Constants.HOST + PATH, get_params).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() != 200) {

            }
            String result = Utils.getFullStringFromConnection(connection.getInputStream(), "UTF-8");
            Log.d("GetTopics", result);
            Gson gson = new Gson();
            topics = gson.fromJson(result, new TypeToken<ArrayList<Topic>>() {
            }.getType());
            for (Topic topic : topics) {
                Log.d("Parced", "title:" + topic.title);
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
