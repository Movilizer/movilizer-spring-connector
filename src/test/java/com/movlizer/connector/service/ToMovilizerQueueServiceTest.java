package com.movlizer.connector.service;

import com.movilitas.movilizer.v14.MovilizerGenericDataContainer;
import com.movilitas.movilizer.v14.MovilizerGenericDataContainerEntry;
import com.movilitas.movilizer.v14.MovilizerMasterdataUpdate;
import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.service.queues.ToMovilizerQueueService;
import com.movlizer.connector.config.MovilizerV12TestConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.datatype.DatatypeFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for queries retrieving movilizer users.
 *
 * @author Jesús de Mula Cano <jesus.demula@movilizer.com>
 * @version 0.1-SNAPSHOT, 2014.11.10
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MovilizerV12TestConfig.class})
public class ToMovilizerQueueServiceTest {

    @Autowired
    private ToMovilizerQueueService service;

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() {
    }

    @Test
    public void testAddOneMasterdataUpdate() throws Exception {

        //Plain data
        MasterdataToMovilizerQueue.Action action = MasterdataToMovilizerQueue.Action.UPDATE;
        String syncTimestamp = "2001-07-10T12:08:56.335Z";
        String pool = "pool";
        String key = "key";
        String group = "group";
        String description = "group";
        String filter1 = "group";
        String filter2 = "group";
        String filter3 = "group";
        Long filter4 = 4L;
        Long filter5 = 5L;
        Long filter6 = 6L;
        String encryptionAlgorithm = "";
        String encryptionIV = "";
        String encryptionHMAC = "";
        String validTillDate = "2001-07-12T12:08:56.435Z";

        String entryName = "entry";
        String entryValue = "entryVal";

        //Setup object data
        MovilizerMasterdataUpdate masterdataUpdate = new MovilizerMasterdataUpdate();
        masterdataUpdate.setKey(key);
        masterdataUpdate.setGroup(group);
        masterdataUpdate.setDescription(description);
        masterdataUpdate.setFilter1(filter1);
        masterdataUpdate.setFilter2(filter2);
        masterdataUpdate.setFilter3(filter3);
        masterdataUpdate.setFilter4(filter4);
        masterdataUpdate.setFilter5(filter5);
        masterdataUpdate.setFilter6(filter6);
        masterdataUpdate.setEncryptionAlgorithm(encryptionAlgorithm);
        masterdataUpdate.setEncryptionIV(encryptionIV);
        masterdataUpdate.setEncryptionHMAC(encryptionHMAC);
        masterdataUpdate.setValidTillDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                validTillDate));

        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName(entryName);
        entry.setValstr(entryValue);

        MovilizerGenericDataContainer data = new MovilizerGenericDataContainer();
        data.getEntry().add(entry);

        masterdataUpdate.setData(data);

        //Insert in queue
        MasterdataToMovilizerQueue in = new MasterdataToMovilizerQueue(pool, masterdataUpdate);
        in.setSyncTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(syncTimestamp).toGregorianCalendar());
        service.offer(in);

        List<MasterdataToMovilizerQueue> out = service.getAllMasterdataUpdatesOrdered();

        assertThat(out.isEmpty(), is(false));
        assertThat(out.get(0), is(in));
        assertThat(out.get(0).getAction(), is(action));
        assertThat(out.get(0).getPool(), is(pool));
        assertThat(out.get(0).getKey(), is(key));
        assertThat(out.get(0).getGroup(), is(group));
        assertThat(out.get(0).getMasterdataUpdate(), is(not(nullValue())));
        assertThat(out.get(0).getMasterdataUpdate().getKey(), is(key));
        assertThat(out.get(0).getMasterdataUpdate().getData().getEntry().isEmpty(), is(false));
        assertThat(out.get(0).getMasterdataUpdate().getData().getEntry().get(0).getName(),
                is(entryName));
        assertThat(out.get(0).getMasterdataUpdate().getData().getEntry().get(0).getValstr(),
                is(entryValue));
    }
}
