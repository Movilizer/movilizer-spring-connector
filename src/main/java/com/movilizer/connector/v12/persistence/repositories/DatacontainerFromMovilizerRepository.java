package com.movilizer.connector.v12.persistence.repositories;


import com.movilizer.connector.v12.persistence.entities.DatacontainerFromMovilizerQueue;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "datacontainers", path = "datacontainers")
public interface DatacontainerFromMovilizerRepository extends
        PagingAndSortingRepository<DatacontainerFromMovilizerQueue, Long> {

    List<DatacontainerFromMovilizerQueue> findByKey(@Param("key") String key);

    List<DatacontainerFromMovilizerQueue> findByMoveletKey(@Param("moveletKey") String moveletKey);

    List<DatacontainerFromMovilizerQueue> findByDeviceAddress(
            @Param("deviceAddress") String deviceAddress);

    List<DatacontainerFromMovilizerQueue> findAllByOrderByCreationTimestampAsc();

    List<DatacontainerFromMovilizerQueue> findAllByOrderBySyncTimestampAsc();

    List<DatacontainerFromMovilizerQueue> deleteByKeyIn(List<String> key);

    //    @Query("delete from DatacontainerFromMovilizerQueue d where d.key=?1")
    //    void deleteByKey(@Param("key") String key);
}
