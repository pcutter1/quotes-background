/*
 *  Copyright 2020 Deep Dive Coding/CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.quotesbackground;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.quotesbackground.service.PeriodicUpdateService;
import edu.cnm.deepdive.quotesbackground.service.QuoteDatabase;
import edu.cnm.deepdive.quotesbackground.service.QuoteRepository;
import io.reactivex.schedulers.Schedulers;

public class QuotesBackgroundApplication extends Application
    implements SharedPreferences.OnSharedPreferenceChangeListener {

  private int pollingInterval = 0;

  @Override
  public void onCreate() {
    super.onCreate();
    QuoteDatabase.setContext(this);
    QuoteDatabase.getInstance().getQuoteDao().delete()
        .subscribeOn(Schedulers.io())
        .subscribe();
    QuoteRepository.setContext(this);
    handlePollingIntervalChange(PreferenceManager.getDefaultSharedPreferences(this));
    Stetho.initializeWithDefaults(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals(getString(R.string.poll_interval_pref_key))) {
      handlePollingIntervalChange(sharedPreferences);
    }
  }

  private void handlePollingIntervalChange(SharedPreferences preferences) {
    int newPollingInterval = preferences.getInt(getString(R.string.poll_interval_pref_key),
        getResources().getInteger(R.integer.poll_interval_pref_default));
    if (newPollingInterval != pollingInterval) {
      if (newPollingInterval != 0) {
        PeriodicUpdateService.schedule(this);
      }
      pollingInterval = newPollingInterval;
    }
  }

}
