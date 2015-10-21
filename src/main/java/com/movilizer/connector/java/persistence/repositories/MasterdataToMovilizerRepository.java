package com.movilizer.connector.java.persistence.repositories;


import com.movilizer.connector.java.persistence.entities.MasterdataToMovilizerQueue;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "masterdata", path = "masterdata")
public interface MasterdataToMovilizerRepository extends
        PagingAndSortingRepository<MasterdataToMovilizerQueue, Long> {

    List<MasterdataToMovilizerQueue> findByPool(@Param("pool") String pool);

    List<MasterdataToMovilizerQueue> findByKey(@Param("key") String key);

    List<MasterdataToMovilizerQueue> findByGroup(@Param("group") String group);

    List<MasterdataToMovilizerQueue> findByFilter1(@Param("filter1") String filter1);

    List<MasterdataToMovilizerQueue> findByFilter2(@Param("filter2") String filter2);

    List<MasterdataToMovilizerQueue> findByFilter3(@Param("filter3") String filter3);

    List<MasterdataToMovilizerQueue> findByFilter4(@Param("filter4") Long filter4);

    List<MasterdataToMovilizerQueue> findByFilter5(@Param("filter5") Long filter5);

    List<MasterdataToMovilizerQueue> findByFilter6(@Param("filter6") Long filter6);

    List<MasterdataToMovilizerQueue> findAllByOrderBySyncTimestampAsc();

    List<MasterdataToMovilizerQueue> deleteByKeyIn(Collection<String> key);

    //    @Query("delete from DatacontainerFromMovilizerQueue d where d.key=?1")
    //    void deleteByKey(@Param("key") String key);
}
