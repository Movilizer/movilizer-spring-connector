package com.movilizer.mds.connector.model;

import com.movilitas.movilizer.v15.MovilizerMovelet;
import com.movilitas.movilizer.v15.MovilizerMoveletDelete;
import com.movilitas.movilizer.v15.MovilizerMoveletSet;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.model.consolidation.MasterdataCache;
import com.movilizer.mds.connector.model.consolidation.MoveletCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds all the utils needed to consolidate requests in the MovilizerRequestSink before sending
 * them to the cloud.
 * <p>
 * The mechanism used is "last in the list wins" for entries that collide. In the case of
 * updates/creates in the same list as deletes then the following logic is followed:
 * - If the delete is before the create then both items appear in the request. The MDS first will
 * process the deletes and then updates/creates.
 * - If the deletes is after the creates then the creates will not be added to the final request.
 *
 * This ordering mechanism is applied to:
 * - Movelets with the same version
 * - Masterdata
 * - Documents
 * - participantAssignments
 *
 * In the case of versioned items such as Movelets then the version will be taken into account for
 * the replacement. So in the case of a movelet being in different versions of the same list then
 * only the one with the highest version will be taken into account. If there's deletes in between
 * then the "last in the list wins" mechanism will be applied.
 *
 * IMPORTANT NOTE: the consolidation is not taking into account the SYSTEM ID since this are
 * overridden by the MovilizerRequestSink
 */
class RequestConsolidationUtil {

    /**
     * Consolidates a list of requests into a single request.
     * <p>
     * The mechanism used is "last in the list wins" for entries that collide. For specifics corner
     * cases see RequestConsolidationUtil main docs.
     *
     * @param requests list to be consolidated
     * @return single request containing all the items from the input list
     */
    public static MovilizerRequest consolidateRequests(List<MovilizerRequest> requests) {

        MoveletCache moveletCache = new MoveletCache();
        MasterdataCache masterdataCache = new MasterdataCache();

        for (MovilizerRequest request : requests) {
            for (MovilizerMoveletDelete delete : request.getMoveletDelete()) {
                moveletCache.apply(delete);
            }
            for (MovilizerMoveletSet set : request.getMoveletSet()) {
                for (MovilizerMovelet movelet : set.getMovelet()) {
                    moveletCache.apply(movelet);
                }
            }
        }

        MovilizerRequest outputRequest = new MovilizerRequest();
        outputRequest.getMoveletDelete().addAll(moveletCache.getDeletes());
        List<MovilizerMovelet> movelets = moveletCache.getMovelets();
        if (!movelets.isEmpty()) {
            MovilizerMoveletSet consolidatedMoveletSet = new MovilizerMoveletSet();
            outputRequest.getMoveletSet().add(consolidatedMoveletSet);
            consolidatedMoveletSet.getMovelet().addAll(movelets);
        }
        return outputRequest;
    }

    private static String getMoveletKeyExtended(MovilizerMovelet movelet) {
        return movelet.getMoveletKey() + movelet.getMoveletKeyExtension();
    }

    private static String getMoveletKeyExtended(MovilizerMoveletDelete delete) {
        return delete.getMoveletKey() + delete.getMoveletKeyExtension();
    }


}
