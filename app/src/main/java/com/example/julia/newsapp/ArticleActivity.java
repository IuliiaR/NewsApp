package com.example.julia.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ArticleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final int ARTICLE_LOADER_ID = 1;
    private ArticleAdapter mAdapter;

    private TextView mEmptyState;
    private View mProgressState;
    private static final String API_KEY = "test";
    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        RecyclerView newsListView = findViewById(R.id.list);

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        mEmptyState = findViewById(R.id.empty_state);
        mProgressState = findViewById(R.id.progress);

        newsListView.setLayoutManager(new LinearLayoutManager(this));
        newsListView.setAdapter(mAdapter);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        }
        else {
            mProgressState.setVisibility(View.GONE);
            mEmptyState.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> prefSet = sharedPrefs.getStringSet(getString(R.string.choose_sections_key),
                new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.sections_default_value))));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("section", getMultiParamQueryString(prefSet));
        uriBuilder.appendQueryParameter("show-fields", "byline,thumbnail");
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> news) {
        mProgressState.setVisibility(View.GONE);

        mEmptyState.setText(R.string.no_news);
        mAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mEmptyState.setVisibility(View.GONE);
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getMultiParamQueryString(Set<String> prefSet){
        if (prefSet.isEmpty()){
            return "";
        }

        StringBuilder queryStr = new StringBuilder();
        Iterator<String> itr = prefSet.iterator();

        queryStr.append(itr.next().toLowerCase());

        while (itr.hasNext()){
            queryStr.append('|');
            queryStr.append(itr.next().toLowerCase());
        }

        return queryStr.toString();
    }
}
