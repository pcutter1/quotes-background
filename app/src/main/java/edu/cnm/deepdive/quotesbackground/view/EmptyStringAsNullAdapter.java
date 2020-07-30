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
package edu.cnm.deepdive.quotesbackground.view;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;

public class EmptyStringAsNullAdapter<T> implements JsonDeserializer<T> {

  @Override
  public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    boolean treatAsNull;
    if (json.isJsonPrimitive()) {
      JsonPrimitive primitive = json.getAsJsonPrimitive();
      treatAsNull = primitive.isString() && primitive.getAsString().isEmpty();
    } else {
      treatAsNull = false;
    }
    return treatAsNull ? null : context.deserialize(json, typeOfT);
  }

}
