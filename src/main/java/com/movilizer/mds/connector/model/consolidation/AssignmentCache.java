package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMoveletAssignment;
import com.movilitas.movilizer.v15.MovilizerMoveletAssignmentDelete;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.ArrayList;
import java.util.List;


class AssignmentCache {

    private final MovilizerMetricService metrics;

    private List<MovilizerMoveletAssignment> moveletAssignments = new ArrayList<>();
    private List<MovilizerMoveletAssignmentDelete> assignmentsDelete = new ArrayList<>();

    AssignmentCache(MovilizerMetricService metrics) {
        this.metrics = metrics;
    }

    /**
     * Applies the assignment to the current cache.
     *
     * @param assignment to be added to the cache
     */
    public void apply(MovilizerMoveletAssignment assignment) {
        moveletAssignments.add(assignment);
    }

    /**
     * Applies the assignment deletes to the current cache.
     *
     * @param assignmentDeletes to be added to the cache
     */
    public void apply(MovilizerMoveletAssignmentDelete assignmentDeletes) {
        assignmentsDelete.add(assignmentDeletes);
    }

    /**
     * Add current cache to the outbound request and clears the cache.
     *
     * @param request to add the movelets to
     * @return if there's a need to send
     */
    public boolean addToRequest(MovilizerRequest request) {
        if (!moveletAssignments.isEmpty()) {
            request.getMoveletAssignment().addAll(moveletAssignments);
            return true;
        }
        if (!assignmentsDelete.isEmpty()) {
            request.getMoveletAssignmentDelete().addAll(assignmentsDelete);
            return true;
        }
        clear();
        return false;
    }

    private void clear() {
        moveletAssignments.clear();
        assignmentsDelete.clear();
    }

    private Long size() {
        Long acc = 0L;
        acc += moveletAssignments.size();
        acc += assignmentsDelete.size();
        return acc;
    }
}
