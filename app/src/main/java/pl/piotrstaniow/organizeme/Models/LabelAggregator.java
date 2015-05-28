package pl.piotrstaniow.organizeme.Models;

import com.android.internal.util.Predicate;

import java.util.List;

import pl.piotrstaniow.organizeme.DatabaseUtils.LocalQueryManager;

/**
 * Created by oszka on 26.05.15.
 */
public class LabelAggregator implements ItemAggregator<Label> {
    private List<Label> labelList;
    LocalQueryManager localQueryManager;

    private static LabelAggregator ourInstance = new LabelAggregator();

    public static LabelAggregator getInstance() {
        return ourInstance;
    }

    private LabelAggregator() {
        localQueryManager = LocalQueryManager.getInstance();
        localQueryManager.openWritable();
        labelList = localQueryManager.getAllLabels();
        localQueryManager.close();
    }

    @Override
    public void add(Label newLabel) {
        if(!labelList.contains(newLabel)){
            labelList.add(newLabel);
        }

    }

    public void addLabelsToDB(){
        localQueryManager.openWritable();
        List<Label> alreadyInDB = localQueryManager.getAllLabels();
        for(Label l: labelList){
            if(!alreadyInDB.contains(l)) {
                localQueryManager.createLabel(l.getName());
                alreadyInDB.add(l);
            }
        }
        localQueryManager.close();
    }

    @Override
    public void remove(Label label) {
        if(labelList.contains(label)){
            labelList.remove(label);
        }
    }

    @Override
    public void update(Label createdCategory) {

    }

    @Override
    public int getSize() {
        return labelList.size();
    }

    @Override
    public Label getItem(int i) {
        return labelList.get(i);
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
