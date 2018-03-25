package co.hyperverge.hyperdocscombinedapp;

import android.app.Application;
import co.hyperverge.hyperdocscombinedapp.Utils.Configs;
import co.hyperverge.hyperdocscombinedapp.Utils.SpUtils;
import co.hyperverge.hyperdocssdk.HyperDocsSDK;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by sanchit on 10/05/17.
 */

public class HyperDocsCombinedApp extends Application {
    private volatile static HyperDocsCombinedApp instance = null;

    public static synchronized HyperDocsCombinedApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        HyperDocsSDK.init(this, Configs.APP_ID, Configs.APP_KEY);

        SpUtils.init(this);
    }
}
