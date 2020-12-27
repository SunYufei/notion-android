package ml.sunyufei.notion;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class UrlModel extends AndroidViewModel {
    private final String key = getApplication().getResources().getString(R.string.key);
    private final String name = getApplication().getResources().getString(R.string.shp_name);

    public UrlModel(@NonNull Application application) {
        super(application);
    }

    private void load() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        String cookies = sharedPreferences.getString(key, "");
    }
}
