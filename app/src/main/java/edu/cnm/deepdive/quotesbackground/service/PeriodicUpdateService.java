package edu.cnm.deepdive.quotesbackground.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.RxWorker;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.WorkerParameters;
import edu.cnm.deepdive.quotesbackground.R;
import io.reactivex.Single;
import java.util.concurrent.TimeUnit;

public class PeriodicUpdateService
    implements SharedPreferences.OnSharedPreferenceChangeListener {

  @SuppressLint("StaticFieldLeak")
  private static Context context;

  private final QuoteRepository quoteRepository;
  private final SharedPreferences preferences;
  private final String pollingIntervalPrefKey;
  private final int defaultPollingInterval;

  private WorkRequest request;

  private PeriodicUpdateService() {
    quoteRepository = QuoteRepository.getInstance();
    pollingIntervalPrefKey = context.getString(R.string.poll_interval_pref_key);
    defaultPollingInterval = context.getResources().getInteger(R.integer.poll_interval_pref_default);
    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    preferences.registerOnSharedPreferenceChangeListener(this);
  }

  public static void setContext(Context context) {
    PeriodicUpdateService.context = context;
  }

  public static PeriodicUpdateService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public synchronized void schedule() {
    int pollingInterval = preferences.getInt(pollingIntervalPrefKey, defaultPollingInterval);
    if (pollingInterval > 0) {
      request = new OneTimeWorkRequest.Builder(Worker.class)
          .setInitialDelay(pollingInterval, TimeUnit.MINUTES)
          .build();
      WorkManager.getInstance(context).enqueue(request);
    }
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals(pollingIntervalPrefKey)) {
      schedule();
    }
  }

  private static class InstanceHolder {

    private static final PeriodicUpdateService INSTANCE = new PeriodicUpdateService();

  }

  public static class Worker extends RxWorker {


    public Worker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
      super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
      PeriodicUpdateService service = PeriodicUpdateService.getInstance();
      if (getId().equals(service.request.getId())) {
        return service.quoteRepository.fetch()
            .andThen(Single.fromCallable(() -> {
              service.schedule();
              return Result.success();
            }));
      } else {
        return Single.just(Result.success());
      }
    }

  }

}
