package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import android.support.v4.util.ArrayMap;
import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.Task;

import java.util.Iterator;
import java.util.List;

/**
 * OrganizeMe
 * Author: Piotr Staniów, Zuzanna Gniewaszewska, Sławomir Domagała
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created on 17.05.15.
 */
public interface TaskCategoryManager {
    Iterator getTasksCategorizedIterator();
}