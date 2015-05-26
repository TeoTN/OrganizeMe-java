package pl.piotrstaniow.organizeme.Models;

import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oszka on 26.05.15.
 */
public class LabelAggregator implements ItemAggregator<Label> {
    private List<Label> labelList;

    private static LabelAggregator ourInstance = new LabelAggregator();

    public static LabelAggregator getInstance() {
        return ourInstance;
    }

    private LabelAggregator() {
        labelList = new ArrayList<>();
    }

    @Override
    public void add(Label newLabel) {
        if(!labelList.contains(newLabel)){
            labelList.add(newLabel);
        }
    }

    @Override
    public void remove(Label task) {

    }

    @Override
    public void update(Label createdCategory) {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Label getItem(int i) {
        return null;
    }

    @Override
    public List<Label> filter(Predicate<Label> predicate) {
        return null;
    }

    @Override
    public List<Label> getAll() {
        return labelList;
    }
}
