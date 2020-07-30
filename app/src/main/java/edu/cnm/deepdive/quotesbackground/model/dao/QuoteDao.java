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
package edu.cnm.deepdive.quotesbackground.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.quotesbackground.model.entity.Quote;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface QuoteDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  Single<Long> insert(Quote quote);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  Single<List<Long>> insert(Quote... quotes);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  Single<List<Long>> insert(Collection<Quote> quotes);

  @Update
  Single<Integer> update(Quote... quotes);

  @Update
  Single<Integer> update(Collection<Quote> quotes);

  @Delete
  Single<Integer> delete(Quote... quotes);

  @Delete
  Single<Integer> delete(Collection<Quote> quotes);

  @Query("DELETE FROM Quote WHERE quote_id <= (SELECT quote_id FROM Quote ORDER BY quote_id DESC LIMIT 1 OFFSET :mruCount)")
  Single<Integer> deleteLru(int mruCount);

  @Query("SELECT * FROM Quote WHERE quote_id = :quoteId")
  Single<Quote> selectById(long quoteId);

  @Query("SELECT * FROM Quote ORDER BY RANDOM() LIMIT 1")
  Single<Quote> selectRandom();

  @Query("SELECT * FROM Quote ORDER BY created")
  LiveData<List<Quote>> selectAllOrderByCreated();

  @Query("SELECT * FROM Quote ORDER BY author")
  LiveData<List<Quote>> selectAllOrderByAuthor();

  @Query("SELECT * FROM Quote ORDER BY text")
  LiveData<List<Quote>> selectAllOrderByText();

  @Query("SELECT * FROM Quote WHERE author LIKE :filter OR text LIKE :filter ORDER BY author")
  LiveData<List<Quote>> selectAllByAuthorLikeOrTextLikeOrderByText(String filter);

  @Query("SELECT * FROM Quote ORDER BY created DESC LIMIT 1 OFFSET 0")
  LiveData<Quote> selectMostRecent();

}
