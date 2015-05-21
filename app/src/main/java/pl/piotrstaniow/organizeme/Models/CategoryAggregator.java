package pl.piotrstaniow.organizeme.Models;


import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.List;

import pl.piotrstaniow.organizeme.DatabaseUtils.LocalQueryManager;
import pl.piotrstaniow.organizeme.ItemAggregator;

/**
 * OrganizeMe
 * Author: Piotr Staniów, Zuzanna Gniewaszewska, S³awomir Domaga³a
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created on 17.05.15.
 */
public class CategoryAggregator implements ItemAggregator<Category> {
    private static CategoryAggregator instance = null;
    LocalQueryManager localQueryManager;
    private List<Category> categoryList;

    private CategoryAggregator() {
        categoryList = new ArrayList<>();
        localQueryManager = LocalQueryManager.getInstance();
        localQueryManager.openWritable();
        categoryList = localQueryManager.getAllCategories();
        localQueryManager.close();
    }

    public static CategoryAggregator getInstance() {
        if (instance == null) {
            synchronized (CategoryAggregator.class) {
                if (instance == null) {
                    instance = new CategoryAggregator();
                }
            }
        }
        return instance;
    }

    @Override
    public void add(Category item) {
        localQueryManager.openWritable();
        localQueryManager.createCategory(item);
        localQueryManager.close();
        categoryList.add(item);
    }

    @Override
    public void remove(Category item) {
    }

    @Override
    public int getSize() {
        return categoryList.size();
    }

    @Override
    public Category getItem(int i) {
        return categoryList.get(i);
    }

    @Override
    public List<Category> filter(Predicate<Category> predicate) {
        return null;
    }
}
