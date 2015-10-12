package com.movilizer.connector.persistence.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.movilizer.connector.persistence.entities.ReplyFromMovilizerQueue;


@RepositoryRestResource(collectionResourceRel = "replys", path = "replys")
public interface ReplyFromMovilizerRepository extends
        PagingAndSortingRepository<ReplyFromMovilizerQueue, Long> {

    List<ReplyFromMovilizerQueue> findByKey(@Param("key") String key);

    List<ReplyFromMovilizerQueue> findByMoveletKey(@Param("moveletKey") String moveletKey);

    List<ReplyFromMovilizerQueue> findByDeviceAddress(
            @Param("deviceAddress") String deviceAddress);

    List<ReplyFromMovilizerQueue> findAllByOrderByCreationTimestampAsc();

    List<ReplyFromMovilizerQueue> findAllByOrderBySyncTimestampAsc();

    List<ReplyFromMovilizerQueue> deleteByKeyIn(List<String> key);
}