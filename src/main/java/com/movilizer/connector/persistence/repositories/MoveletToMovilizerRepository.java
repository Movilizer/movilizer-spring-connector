package com.movilizer.connector.persistence.repositories;


import com.movilizer.connector.persistence.entities.MoveletToMovilizerQueue;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "movelets", path = "movelets")
public interface MoveletToMovilizerRepository extends
        PagingAndSortingRepository<MoveletToMovilizerQueue, Long> {

    List<MoveletToMovilizerQueue> findByMoveletKey(@Param("moveletKey") String moveletKey);

    List<MoveletToMovilizerQueue> findByMoveletKeyAndMoveletKeyExtension(
            @Param("moveletKey") String moveletKey, @Param("moveletKeyExtension") String moveletKeyExtension);

    List<MoveletToMovilizerQueue> findByName(@Param("name") String name);

    List<MoveletToMovilizerQueue> findAllByOrderBySyncTimestampAsc();

}
