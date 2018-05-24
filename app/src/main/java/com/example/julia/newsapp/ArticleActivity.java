package com.example.julia.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final int ARTICLE_LOADER_ID = 1;
    private ArticleAdapter mAdapter;

    private TextView mEmptyState;
    private View mProgressState;
    private static final String API_KEY = "test";
    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?section=science%7Cenvironment%7Ctechnology&" +
                    "show-fields=byline%2Cthumbnail&api-key=" + API_KEY;

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
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL);
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
}
