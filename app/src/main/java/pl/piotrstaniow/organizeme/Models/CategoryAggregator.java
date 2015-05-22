package pl.piotrstaniow.organizeme.Models;


import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalQueryManager;

import java.util.ArrayList;
import java.util.List;

/**
 * OrganizeMe
 * Author: Piotr Staniow, Zuzanna Gniewaszewska, Slawomir Domagala
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
        rearrangeIds();
        localQueryManager.close();
    }

    private void rearrangeIds() {
        long id = 0;
        for(Category c: categoryList) {
            c.setId(id);
            id += 1;
        }
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
        rearrangeIds();
    }

    @Override
    public void remove(Category item) {
        localQueryManager.openWritable();
        localQueryManager.removeCategory(item);
        localQueryManager.close();
        categoryList.remove(item);
        rearrangeIds();
    }

    @Override
    public void update(Category item) {
        localQueryManager.openWritable();
        localQueryManager.editCategory(item);
        localQueryManager.close();
        categoryList.remove((int)item.getId());
        categoryList.add(item);
        rearrangeIds();
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

    @Override
    public List<Category> getAll() {
        return categoryList;
    }

}
