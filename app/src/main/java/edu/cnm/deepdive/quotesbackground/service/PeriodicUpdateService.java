package edu.cnm.deepdive.quotesbackground.service;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.RxWorker;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;
import edu.cnm.deepdive.quotesbackground.R;
import io.reactivex.Single;
import java.util.concurrent.TimeUnit;

public class PeriodicUpdateService extends RxWorker {

  private final QuoteRepository quoteRepository;

  public PeriodicUpdateService(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
    super(appContext, workerParams);
    quoteRepository = QuoteRepository.getInstance();
  }

  public static void schedule(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    int interval = preferences.getInt(context.getString(R.string.poll_interval_pref_key),
        context.getResources().getInteger(R.integer.poll_interval_pref_default));
    if (interval > 0) {
      OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(PeriodicUpdateService.class)
          .setInitialDelay(interval, TimeUnit.MINUTES)
          .build();
      WorkManager.getInstance(context).enqueue(request);
    }
  }

  @NonNull
  @Override
  public Single<Result> createWork() {
    return quoteRepository.fetch()
        .andThen(Single.fromCallable(() -> {
          schedule(getApplicationContext());
          return Result.success();
        }));
  }

}
