package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.*;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.connector.model.consolidation.*;

import java.util.List;

/**
 * Holds all the utils needed to consolidate requests in the MovilizerRequestSink before sending
 * them to the cloud.
 * <p>
 * The mechanism used is "last in the list wins" for entries that collide. In the case of masterdata
 * updates/creates in the same list as deletes then the following logic is followed:
 * - If the delete is before the create then both items appear in the request. The MDS first will
 * process the deletes and then updates/creates.
 * - If the deletes is after the creates then the creates will not be added to the final request.
 * <p>
 * Since on production cases the use of movelet delete is heavily discouraged, the movelet deletes are ignored. This
 * might arise some doubts on the movelet lifecycle for some use cases but here we propose solutions for several use
 * cases:
 * <ul>
 *     <li>Movelets has to be deleted after some action is completed -> use single movelet</li>
 *     <li>Movelet has to disappear from device -> remove movelet assignment from participant (remember to have a reasonable
 *     "valid till date" so the movelet is removed at some point from the MDS)</li>
 *     <li>Movelet needs to be updated -> increase the version</li>
 * </ul>
 * <p>
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
        MoveletAttributesCache moveletAttributesCache = new MoveletAttributesCache(metrics);
        MoveletCloneCache moveletCloneCache = new MoveletCloneCache(metrics);
        MoveletUpdateCache moveletUpdateCache = new MoveletUpdateCache(metrics);
        MoveletResetCache moveletResetCache = new MoveletResetCache(metrics);
        AssignmentCache assignmentCache = new AssignmentCache(metrics);
        AssignmentDeleteCache assignmentDeleteCache = new AssignmentDeleteCache(metrics);
        MasterdataCache masterdataCache = new MasterdataCache(metrics);
        MasterdataAttributesCache masterdataAttributesCache = new MasterdataAttributesCache(metrics);
        SystemConfigurationCache systemConfigurationCache = new SystemConfigurationCache(metrics);
        ParticipantConfigurationCache participantConfigurationCache = new ParticipantConfigurationCache(metrics);
        ParticipantResetCache participantResetCache = new ParticipantResetCache(metrics);
        TransientContainerCache transientContainerCache = new TransientContainerCache(metrics);

        for (MovilizerRequest request : requests) {
            moveletCache.apply(request);
            moveletAttributesCache.apply(request);
            moveletCloneCache.apply(request);
            moveletUpdateCache.apply(request);
            moveletResetCache.apply(request);
            assignmentCache.apply(request);
            assignmentDeleteCache.apply(request);
            masterdataCache.apply(request);
            masterdataAttributesCache.apply(request);
            systemConfigurationCache.apply(request);
            participantConfigurationCache.apply(request);
            participantResetCache.apply(request);
            transientContainerCache.apply(request);
        }

        MovilizerRequest outputRequest = new MovilizerRequest();

        boolean isCallToCloudNeeded = moveletCache.addToRequest(outputRequest) ||
                moveletAttributesCache.addToRequest(outputRequest) ||
                moveletCloneCache.addToRequest(outputRequest) ||
                moveletUpdateCache.addToRequest(outputRequest) ||
                moveletResetCache.addToRequest(outputRequest) ||
                assignmentCache.addToRequest(outputRequest) ||
                assignmentDeleteCache.addToRequest(outputRequest) ||
                masterdataCache.addToRequest(outputRequest) ||
                masterdataAttributesCache.addToRequest(outputRequest) ||
                systemConfigurationCache.addToRequest(outputRequest) ||
                participantConfigurationCache.addToRequest(outputRequest) ||
                participantResetCache.addToRequest(outputRequest) ||
                transientContainerCache.addToRequest(outputRequest);

        return isCallToCloudNeeded ? outputRequest : null;
    }
}
