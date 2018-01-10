package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.*;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.connector.model.consolidation.*;

import java.util.List;

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
public class RequestConsolidationUtil {

    /**
     * Consolidates a list of requests into a single request or returns null if there's no updates.
     *
     * @param requests list to be consolidated
     * @return single request containing all the items from the input list or null if there's no updates
     */
    public static MovilizerRequest consolidateRequests(List<MovilizerRequest> requests,
                                                       MovilizerMetricService metrics) {

        MoveletCache moveletCache = new MoveletCache(metrics);
        AssignmentCache assignmentCache = new AssignmentCache(metrics);
        MoveletAttributesCache moveletAttributesCache = new MoveletAttributesCache(metrics);
        MasterdataCache masterdataCache = new MasterdataCache(metrics);
        MasterdataAttributesCache masterdataAttributesCache = new MasterdataAttributesCache(metrics);

        for (MovilizerRequest request : requests) {
            for (MovilizerMoveletSet set : request.getMoveletSet()) {
                    moveletCache.apply(set);
            }
            for (MovilizerMasterdataPoolUpdate poolUpdate : request.getMasterdataPoolUpdate()) {
                masterdataCache.apply(poolUpdate);
            }
            for (MovilizerMoveletAssignment assignment : request.getMoveletAssignment()) {
                assignmentCache.apply(assignment);
            }
            for (MovilizerMoveletAssignmentDelete delete : request.getMoveletAssignmentDelete()) {
                assignmentCache.apply(delete);
            }
            for (MovilizerMoveletAttributeUpdate attributes : request.getMoveletAttributeUpdate()) {
                moveletAttributesCache.apply(attributes);
            }
            for (MovilizerMasterdataAttributeUpdate attributes : request.getMasterdataAttributeUpdate()) {
                masterdataAttributesCache.apply(attributes);
            }
        }

        MovilizerRequest outputRequest = new MovilizerRequest();

        boolean isCallToCloudNeeded = moveletCache.addToRequest(outputRequest);
        isCallToCloudNeeded = isCallToCloudNeeded || masterdataCache.addToRequest(outputRequest);
        isCallToCloudNeeded = isCallToCloudNeeded || assignmentCache.addToRequest(outputRequest);

        return isCallToCloudNeeded? outputRequest : null;
    }
}
