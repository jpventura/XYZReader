/*
 * Copyright 2015 Joao Paulo Fernandes Ventura.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jpventura.xyzreader.remote;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Article implements Parcelable {
    @SerializedName("aspect_ratio")
    public float aspectRatio;

    public String author;

    public String body;

    public String id;

    public String photo;

    @SerializedName("published_date")
    public Date publishedDate;

    public String thumb;

    public String title;

    private Article(Parcel in) {
        aspectRatio = in.readFloat();
        author = in.readString();
        body = in.readString();
        id = in.readString();
        photo = in.readString();
        publishedDate = new Date(in.readLong());
        thumb = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(aspectRatio);
        dest.writeString(author);
        dest.writeString(body);
        dest.writeString(id);
        dest.writeString(photo);
        dest.writeLong(publishedDate.getTime());
        dest.writeString(thumb);
        dest.writeString(title);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
