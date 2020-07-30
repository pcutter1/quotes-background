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
package edu.cnm.deepdive.quotesbackground.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.quotesbackground.BuildConfig;
import edu.cnm.deepdive.quotesbackground.model.entity.Quote;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ForismaticService {

  @GET("?method=getQuote&lang=en&format=json")
  Single<Quote> get();

  public static ForismaticService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final ForismaticService INSTANCE;

    static {
     Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build();
        Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .build();
        INSTANCE = retrofit.create(ForismaticService.class);
    }

  }

}
