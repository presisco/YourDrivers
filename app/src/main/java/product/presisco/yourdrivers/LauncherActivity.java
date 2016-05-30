package product.presisco.yourdrivers;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import product.presisco.yourdrivers.Cache.HeadlineCache;
import product.presisco.yourdrivers.Network.VolleyPlusRes;
import product.presisco.yourdrivers.Utils.AlarmTask;

public class LauncherActivity extends AppCompatActivity implements AlarmTask.Action {
    long start;

    @Override
    public void onPostExecute() {
        startMainActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start = System.currentTimeMillis();
        setContentView(R.layout.activity_launcher);
        VolleyPlusRes.init(getApplicationContext());
        new LoadCache().execute();
    }

    private void startMainActivity() {
        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        finish();
    }

    private class LoadCache extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HeadlineCache.getInstance(getApplicationContext()).fillCacheFromDisk();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            long end = System.currentTimeMillis();
            if (end - start < 2000) {
                new AlarmTask(new AlarmTask.Action() {
                    @Override
                    public void onPostExecute() {
                        startMainActivity();
                    }
                }).execute(2000 - (end - start));
            } else {
                startMainActivity();
            }
        }
    }

}
