package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMovelet;
import com.movilitas.movilizer.v15.MovilizerMoveletDelete;
import com.movilitas.movilizer.v15.MovilizerMoveletSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The consolidation of the moveletsets has to mirror the behavior expected in the MDS.
 *
 * The consolidation logic is based on the fact that the MDS processes the MoveletSets in order. If this ever changes
 * this part should be adjusted accordingly.
 *
 * Since on production cases the use of movelet delete is heavily discouraged, the movelet deletes are ignored. This
 * might arise some doubts on the movelet lifecycle for some use cases but here we propose solutions for several use
 * cases:
 * <ul>
 *     <li>Movelets has to be deleted after some action is completed -> use single movelet</li>
 *     <li>Movelet has to disappear from device -> remove movelet assignment from participant (remember to have a reasonable
 *     "valid till date" so the movelet is removed at some point from the MDS)</li>
 *     <li>Movelet needs to be updated -> increase the version</li>
 * </ul>
 */
public class MoveletCache {

    private List<MovilizerMoveletSet> moveletSets = new ArrayList<>();

    public List<MovilizerMoveletSet> getMoveletSets() {
        return moveletSets;
    }

    public void apply(MovilizerMoveletSet set) {
        moveletSets.add(set);
    }

    public void clear() {
        moveletSets.clear();
    }

    public Long size() {
        Long acc = 0L;
        for (MovilizerMoveletSet set: moveletSets) {
            acc += set.getMovelet().size();
        }
        return acc;
    }
}
