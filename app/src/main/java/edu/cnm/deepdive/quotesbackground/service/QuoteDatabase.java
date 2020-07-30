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

import android.app.Application;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.quotesbackground.model.dao.QuoteDao;
import edu.cnm.deepdive.quotesbackground.model.entity.Quote;
import edu.cnm.deepdive.quotesbackground.service.QuoteDatabase.DateConverters;
import java.util.Date;

@Database(entities = Quote.class, version = 1, exportSchema = true)
@TypeConverters(DateConverters.class)
public abstract class QuoteDatabase extends RoomDatabase {

  private static final String DB_NAME = "quote_db";

  private static Application context;

  public static void setContext(Context context) {
    QuoteDatabase.context = (Application) context.getApplicationContext();
  }

  public static QuoteDatabase getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public abstract QuoteDao getQuoteDao();

  private static class InstanceHolder {

    private static final QuoteDatabase INSTANCE =
        Room.databaseBuilder(context, QuoteDatabase.class, DB_NAME)
            .build();

  }

  public static class DateConverters {

    @TypeConverter
    public static Long dateToLong(Date value) {
      return (value != null) ? value.getTime() : null;
    }

    @TypeConverter
    public static Date longToDate(Long value) {
      return (value != null) ? new Date(value) : null;
    }

  }

}
