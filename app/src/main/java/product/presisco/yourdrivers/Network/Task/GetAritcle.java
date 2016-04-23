package product.presisco.yourdrivers.Network.Task;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Topic;
import product.presisco.yourdrivers.Network.Constants;
import product.presisco.yourdrivers.Network.Utils;

/**
 * Created by presisco on 2016/4/22.
 */
public class GetAritcle extends AsyncTask<String, Void, String> {
    private OnLoadCompleteListener mListener;

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(Constants.MOBILE_WEB_HOST + params[0]).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.connect();
            result = Utils.getFullStringFromConnection(conn.getInputStream(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (mListener != null) {
            mListener.onLoadComplete(s);
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

    public void setOnLoadCompleteListener(OnLoadCompleteListener l) {
        mListener = l;
    }

    public interface OnLoadCompleteListener {
        void onLoadComplete(String src);
    }
}
