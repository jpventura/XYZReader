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
package com.jpventura.xyzreader.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jpventura.xyzreader.BuildConfig;
import com.jpventura.xyzreader.remote.Article;
import com.jpventura.xyzreader.remote.GetFeedListCallback;
import com.jpventura.xyzreader.remote.IFeedBackend;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class UpdaterService extends IntentService implements GetFeedListCallback {
    private static final String TAG = "UpdaterService";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.jpventura.xyzreader.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.jpventura.xyzreader.intent.extra.REFRESHING";

    private IFeedBackend mBackend;

    public UpdaterService() {
        super(TAG);
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(IFeedBackend.BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)
                .build();

        mBackend = restAdapter.create(IFeedBackend.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");
            return;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        mBackend.getFeedList(this);
    }

    @Override
    public void success(List<Article> articles, Response response) {
        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

        Uri dirUri = ItemsContract.Items.buildDirUri();

        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(dirUri).build());

        for (Article article : articles) {
            ContentValues values = new ContentValues();
            values.put(ItemsContract.Items.SERVER_ID, article.id);
            values.put(ItemsContract.Items.AUTHOR, article.author);
            values.put(ItemsContract.Items.TITLE, article.title);
            values.put(ItemsContract.Items.BODY, article.body);
            values.put(ItemsContract.Items.THUMB_URL, article.thumb);
            values.put(ItemsContract.Items.PHOTO_URL, article.photo);
            values.put(ItemsContract.Items.ASPECT_RATIO, article.aspectRatio);
            values.put(ItemsContract.Items.PUBLISHED_DATE, article.publishedDate.getTime());
            cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
        }

        try {
            getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Error updating content.", e);
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }

    @Override
    public void failure(RetrofitError error) {
    }
}
