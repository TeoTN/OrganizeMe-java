package pl.piotrstaniow.organizeme.Models;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import pl.piotrstaniow.organizeme.R;

/**
 * Created by oszka on 26.05.15.
 */
public class LabelsCompletionView extends TokenCompleteTextView<Label>{

    public LabelsCompletionView(Context context) {
        super(context);
    }

    public LabelsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(Label label) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.label_token, (ViewGroup)LabelsCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.name)).setText(label.getName());

        return view;
    }

    @Override
    protected Label defaultObject(String s) {
        return new Label(s);
    }
}
