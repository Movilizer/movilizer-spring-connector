package com.movilizer.mds.connector.model.consolidation;

import com.movilizer.mds.connector.MovilizerMetricService;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public abstract class RequestConsolidationBaseTest {

    @Mock
    MovilizerMetricService metrics;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}
