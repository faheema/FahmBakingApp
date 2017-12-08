/*
 * Copyright 2018.  FahmApps.com
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

package com.ap.fahm.bakingapp.model.remotedata;

import android.support.annotation.NonNull;
import android.util.Log;


import com.ap.fahm.bakingapp.model.pojo.Recipe;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteRecipeDataSourceImpl implements RemoteRecipeDataSource {

    private static final String TAG = RemoteRecipeDataSourceImpl.class.getSimpleName();
    private EndPoints endPoint;
    private CompositeDisposable mCompositeDisposable;

    public RemoteRecipeDataSourceImpl() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        endPoint = retrofit.create(EndPoints.class);
    }

    @Override
    public void getRecipes(final RemoteResponseListener<Recipe> responseListener) {
        Call<ArrayList<Recipe>> call = endPoint.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call,
                                   @NonNull Response<ArrayList<Recipe>> response) {
                if (response.body() == null) {
                    responseListener.onFailure(new Throwable());
                    Log.e("Fatal Error", "JSON Mal-formatted");
                } else {
                    responseListener.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call,
                                  @NonNull Throwable t) {
                t.printStackTrace();
                responseListener.onFailure(t);
            }
        });
    }
}
