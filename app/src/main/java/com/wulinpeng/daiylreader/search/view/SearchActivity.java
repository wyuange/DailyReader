package com.wulinpeng.daiylreader.search.view;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wulinpeng.daiylreader.R;
import com.wulinpeng.daiylreader.base.BaseActivity;
import com.wulinpeng.daiylreader.search.adapter.HistoryAdapter;
import com.wulinpeng.daiylreader.search.contract.ISearchPresenter;
import com.wulinpeng.daiylreader.search.contract.ISearchView;
import com.wulinpeng.daiylreader.search.presenter.SearchPresenterImpl;
import com.wulinpeng.daiylreader.search.ui.FlowLayout;
import com.wulinpeng.daiylreader.search.ui.SearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author wulinpeng
 * @datetime: 17/2/19 下午8:55
 * @description:
 */
public class SearchActivity extends BaseActivity implements ISearchView {

    @BindView(R.id.back)
    public ImageView back;

    @BindView(R.id.search_view)
    public SearchView searchView;

    @BindView(R.id.search)
    public TextView search;

    @BindView(R.id.flow_layout)
    public FlowLayout flowLayout;

    @BindView(R.id.change)
    public TextView change;

    @BindView(R.id.clear_history)
    public TextView clearHistory;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    private HistoryAdapter adapter;

    private List<String> history = new ArrayList<>();

    private ISearchPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViews() {
        back.setOnClickListener(v -> finish());
        searchView.setListener(text -> search(text));
        search.setOnClickListener(v -> search(searchView.getInputText()));

        change.setOnClickListener(v -> flowLayout.nextPage());
        flowLayout.setListener(content -> {
            search(content);
        });

        adapter = new HistoryAdapter(this, history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        clearHistory.setOnClickListener(v -> presenter.clearHistory());
    }

    @Override
    protected void initData() {
        presenter = new SearchPresenterImpl(this, this);
        presenter.getHotWords();
        presenter.getHistory();
    }

    private void search(String content) {
        presenter.addHistory(content);
        // search
    }

    @Override
    public void onHotWordsFinish(List<List<String>> pages) {
        flowLayout.setPages(pages);
    }

    @Override
    public void onHistoryFinish(List<String> history) {
        this.history.clear();
        this.history.addAll(history);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String msg) {

    }
}