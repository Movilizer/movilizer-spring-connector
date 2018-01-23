package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.*;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.connector.model.consolidation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
 * <li>Movelets has to be deleted after some action is completed -> use single movelet</li>
 * <li>Movelet has to disappear from device -> remove movelet assignment from participant (remember to have a reasonable
 * "valid till date" so the movelet is removed at some point from the MDS)</li>
 * <li>Movelet needs to be updated -> increase the version</li>
 * </ul>
 * <p>
 * IMPORTANT NOTE: the consolidation is not taking into account the SYSTEM ID since this are
 * overridden by the MovilizerRequestSink
 */
public class RequestConsolidationService {

    private final MovilizerMetricService metrics;
    private final List<ConsolidationCache> consolidationCaches;

    public RequestConsolidationService(MovilizerMetricService metrics) {
        this.metrics = metrics;
        consolidationCaches = new ArrayList<>();
        consolidationCaches.add(new MoveletCache(metrics));
        consolidationCaches.add(new MoveletAttributesCache(metrics));
        consolidationCaches.add(new MoveletCloneCache(metrics));
        consolidationCaches.add(new MoveletUpdateCache(metrics));
        consolidationCaches.add(new MoveletResetCache(metrics));
        consolidationCaches.add(new AssignmentCache(metrics));
        consolidationCaches.add(new AssignmentDeleteCache(metrics));
        consolidationCaches.add(new MasterdataCache(metrics));
        consolidationCaches.add(new MasterdataAttributesCache(metrics));
        consolidationCaches.add(new SystemConfigurationCache(metrics));
        consolidationCaches.add(new ParticipantConfigurationCache(metrics));
        consolidationCaches.add(new ParticipantResetCache(metrics));
        consolidationCaches.add(new TransientContainerCache(metrics));
    }

    /**
     * Consolidates a list of requests into a single request or returns null if there's no updates.
     *
     * @param requests list to be consolidated
     * @return single request containing all the items from the input list or null if there's no updates
     */
    public MovilizerRequest consolidateRequests(List<MovilizerRequest> requests) {

        consolidationCaches.stream()
                .forEach(cache -> requests.forEach(cache::apply));

        MovilizerRequest outputRequest = new MovilizerRequest();

        boolean isCallToCloudNeeded = consolidationCaches.stream()
                .map(cache -> cache.addToRequest(outputRequest))
                .reduce(false, (acc, newValue) -> acc || newValue);

        return isCallToCloudNeeded ? outputRequest : null;
    }
}
