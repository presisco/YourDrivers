package product.presisco.yourdrivers.Network.Task;

import android.os.AsyncTask;

import java.util.List;

import product.presisco.yourdrivers.DataModel.Topic;

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
