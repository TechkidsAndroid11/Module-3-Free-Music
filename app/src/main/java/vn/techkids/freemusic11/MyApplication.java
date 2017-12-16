package vn.techkids.freemusic11;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by qklahpita on 12/16/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
