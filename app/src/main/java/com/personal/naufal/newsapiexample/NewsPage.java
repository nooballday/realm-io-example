package com.personal.naufal.newsapiexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsPage extends AppCompatActivity {

    private Realm realm;

    @BindView(R.id.rv_news_list)
    RecyclerView mRvNewsList;

    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        ButterKnife.bind(this);

        Realm.init(this);

        realm = Realm.getInstance(new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        );

        addDataToRv();

        fetchAPI();
    }

    public void fetchAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .build();

        retrofit2.Call<okhttp3.ResponseBody> service = retrofit.create(NetworkService.class).getNews("bbc-news", "b6bf002748e1447caed5e0dbb2730c04");

        service.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();
                    JSONObject responseObject = new JSONObject(responseBody);
                    JSONArray responseArray = responseObject.getJSONArray("articles");

                    //parse data
                    insertDataToRealm(responseArray);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void insertDataToRealm(JSONArray dataArray) throws JSONException {
        for (int i = 0; i < dataArray.length(); i++) {
            //getting the data from JSON Array
            JSONObject article = dataArray.getJSONObject(i);
            String author = article.getString("author");
            String title = article.getString("title");
            String description = article.getString("description");
            String urlToImage = article.getString("description");
            String publishedAt = article.getString("publishedAt");

            //check if the data exists
            RealmResults<NewsModel> dataChecker = realm.where(NewsModel.class).equalTo("title", title).findAll();

            if (dataChecker.size() == 0) {

                realm.beginTransaction();
                //create model object that extends to RealmObject to store our data
                NewsModel model = realm.createObject(NewsModel.class);
                model.setAuthor(author);
                model.setTitle(title);
                model.setDesc(description);
                model.setUrlToImage(urlToImage);
                model.setPublishedAt(publishedAt);
                //commiting to realm db
                realm.commitTransaction();

            }

            addDataToRv();
        }
    }

    private void addDataToRv() {
        //get all data that available
        RealmResults<NewsModel> newsModels = realm.where(NewsModel.class).findAll();
        //initialize our adapter
        adapter = new NewsAdapter(new ArrayList<>(newsModels), this);
        mRvNewsList.setLayoutManager(new LinearLayoutManager(this));
        mRvNewsList.setAdapter(adapter);
        //notify the adapter that we have new data
        adapter.notifyDataSetChanged();
    }
}
