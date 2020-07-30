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
package edu.cnm.deepdive.quotesbackground.model.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.quotesbackground.view.EmptyStringAsNullAdapter;
import java.util.Date;

@Entity(
    indices = @Index(value = "text", unique = true)
)
public class Quote {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "quote_id")
  private long id;

  @NonNull
  @ColumnInfo(index = true)
  private Date created = new Date();

  @NonNull
  @Expose
  @SerializedName("quoteText")
  @ColumnInfo(collate = ColumnInfo.NOCASE)
  private String text = "";

  @Expose
  @SerializedName("quoteAuthor")
  @JsonAdapter(EmptyStringAsNullAdapter.class)
  @ColumnInfo(collate = ColumnInfo.NOCASE, index = true)
  private String author;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  @NonNull
  public String getText() {
    return text;
  }

  public void setText(@NonNull String text) {
    this.text = text;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

}
