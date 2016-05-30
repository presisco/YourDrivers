package product.presisco.yourdrivers.Utils;

import android.content.Intent;
import android.os.AsyncTask;

import product.presisco.yourdrivers.LauncherActivity;
import product.presisco.yourdrivers.MainActivity;
import product.presisco.yourdrivers.Network.VolleyPlusRes;

/**
 * Created by presisco on 2016/5/30.
 */
public class AlarmTask extends AsyncTask<Long, Void, Void> {
    public AlarmTask() {
        super();
    }

    @Override
    protected Void doInBackground(Long... params) {
        try {
            Thread.sleep(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public interface Action {
        void onPostExecute();
    }
}
