package product.presisco.yourdrivers.Network.Task;

import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
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
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Constants.HOST + "/api/2/contents/newstoppic" + "?" + "id=" + System.currentTimeMillis()).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            String result = Utils.getFullStringFromConnection(connection.getInputStream(), "UTF-8");
            Log.d("GetTopics", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
