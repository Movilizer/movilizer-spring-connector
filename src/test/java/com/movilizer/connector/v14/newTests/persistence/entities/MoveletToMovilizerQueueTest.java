package com.movilizer.connector.v14.newTests.persistence.entities;

import com.movilitas.movilizer.v14.*;
import com.movilizer.connector.java.persistence.entities.MoveletToMovilizerQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class MoveletToMovilizerQueueTest {
    //Test data
    private MoveletToMovilizerQueue moveletToMovilizerQueue;
    private MovilizerMovelet movelet;

    @Before
    public void before() {
        movelet = new MovilizerMovelet();
        movelet.setMoveletKey("moveletKey");
        movelet.setName("moveletName");
        movelet.setMoveletType(MoveletType.MULTI);

    }

    @After
    public void after() {
    }

    @Test
    public void testDatacontainerFromMovilizerQueueConstructor() throws Exception {
        moveletToMovilizerQueue = new MoveletToMovilizerQueue(movelet);

        assertThat(moveletToMovilizerQueue.getMovelet(), is(movelet));
        assertThat(moveletToMovilizerQueue.getMoveletKey(), is(movelet.getMoveletKey()));
        assertThat(moveletToMovilizerQueue.getName(), is(movelet.getName()));
        assertThat(moveletToMovilizerQueue.getMoveletType(), is(movelet.getMoveletType().toString()));
    }


}
