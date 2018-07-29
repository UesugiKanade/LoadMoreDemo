package com.kanade.loadmoredemotwo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import adapter.FruitAdapter;
import bean.Fruit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRvFruits;
    private List<Fruit> mFruitList = new ArrayList<>();
    private FruitAdapter mAdapter;
    private static final int PER_PAGE = 10;

    private int mCurrentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {
        initFruits();
        mAdapter = new FruitAdapter(mFruitList);
        mRvFruits = findViewById(R.id.rv_fruits);
        mRvFruits.setLayoutManager(new LinearLayoutManager(this));
        mRvFruits.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new FruitAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int currentPage) {
                mCurrentPage = currentPage;
                loadMoreTest();
            }
        });
    }

    private void initFruits() {
        Log.i("mytest", "initFruits");
        Fruit mango = new Fruit(getRandomLenthName("Mango"), R.drawable.mango);
        mFruitList.add(mango);
        Fruit orange = new Fruit(getRandomLenthName("Orange"), R.drawable.orange);
        mFruitList.add(orange);
        Fruit pear = new Fruit(getRandomLenthName("Pear"), R.drawable.pear);
        mFruitList.add(pear);
        Fruit pineapple = new Fruit(getRandomLenthName("Pineapple"), R.drawable.pineapple);
        mFruitList.add(pineapple);
        Fruit strawberry = new Fruit(getRandomLenthName("Strawberry"), R.drawable.strawberry);
        mFruitList.add(strawberry);
        Fruit watermelon = new Fruit(getRandomLenthName("Watermelon"), R.drawable.watermelon);
        mFruitList.add(watermelon);
        Fruit apple = new Fruit(getRandomLenthName("Apple"), R.drawable.apple);
        mFruitList.add(apple);
        Fruit banana = new Fruit(getRandomLenthName("Banana"), R.drawable.banana);
        mFruitList.add(banana);
        Fruit cherry = new Fruit(getRandomLenthName("Cherry"), R.drawable.cherry);
        mFruitList.add(cherry);
        Fruit grape = new Fruit(getRandomLenthName("Grape"), R.drawable.grape);
        mFruitList.add(grape);
    }

    //模拟加载网络数据
    private void loadMoreTest() {
        Observable.timer(2000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //第一个条件是有没有获取到新数据，第二个条件是新数据是否填满了一整页
                        Log.d("loadingtest", "main current page: " + mCurrentPage);
                        if (mCurrentPage < 4) {
                            List<Fruit> loadMoreList = new ArrayList<>();
                            Fruit pear = new Fruit(getRandomLenthName("Pear"), R.drawable.pear);
                            loadMoreList.add(pear);
                            Fruit pineapple = new Fruit(getRandomLenthName("Pineapple"), R.drawable.pineapple);
                            loadMoreList.add(pineapple);
                            Fruit strawberry = new Fruit(getRandomLenthName("Strawberry"), R.drawable.strawberry);
                            loadMoreList.add(strawberry);
                            Fruit pineapple2 = new Fruit(getRandomLenthName("Pineapple"), R.drawable.pineapple);
                            loadMoreList.add(pineapple2);
                            Fruit strawberry2 = new Fruit(getRandomLenthName("Strawberry"), R.drawable.strawberry);
                            loadMoreList.add(strawberry2);
                            Fruit watermelon = new Fruit(getRandomLenthName("Watermelon"), R.drawable.watermelon);
                            loadMoreList.add(watermelon);
                            Fruit apple = new Fruit(getRandomLenthName("Apple"), R.drawable.apple);
                            loadMoreList.add(apple);
                            Fruit banana = new Fruit(getRandomLenthName("Banana"), R.drawable.banana);
                            loadMoreList.add(banana);
                            Fruit cherry = new Fruit(getRandomLenthName("Cherry"), R.drawable.cherry);
                            loadMoreList.add(cherry);
                            Fruit grape = new Fruit(getRandomLenthName("Grape"), R.drawable.grape);
                            loadMoreList.add(grape);
                            mFruitList.addAll(loadMoreList);

                            //如果未填满一整页，那么肯定没有更多数据了
                            Log.d("loadingtest", "size1: " + mFruitList.size() + "  -  size2: " + mCurrentPage * PER_PAGE);
                            if (mFruitList.size() == mCurrentPage * PER_PAGE) {
                                mAdapter.setCanLoadMore(true);
                            } else {
                                mAdapter.setCanLoadMore(false);
                            }
                            mAdapter.setData(mFruitList);

                        } else {
                            //网络数据已加载完
                            mAdapter.setCanLoadMore(false);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    private String getRandomLenthName(String name) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(name);
        }
        return builder.toString();
    }


}
