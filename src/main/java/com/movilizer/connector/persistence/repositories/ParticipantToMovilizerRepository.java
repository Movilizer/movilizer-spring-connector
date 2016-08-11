package com.movilizer.connector.persistence.repositories;


import com.movilizer.connector.persistence.entities.ParticipantToMovilizerQueue;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "participants", path = "participants")
public interface ParticipantToMovilizerRepository extends
        PagingAndSortingRepository<ParticipantToMovilizerQueue, Long> {

    List<ParticipantToMovilizerQueue> findByParticipantKey(
            @Param("participantKey") String participantKey);

    List<ParticipantToMovilizerQueue> findByName(@Param("name") String name);

    List<ParticipantToMovilizerQueue> findByDeviceAddress(@Param("deviceAddress") String deviceAddress);

    List<ParticipantToMovilizerQueue> findByMoveletKey(@Param("moveletKey") String moveletKey);

    List<ParticipantToMovilizerQueue> findByMoveletKeyAndMoveletKeyExtension(
            @Param("moveletKey") String moveletKey, @Param("moveletKeyExtension") String moveletKeyExtension);

    List<ParticipantToMovilizerQueue> findAllByOrderBySyncTimestampAsc();

    List<ParticipantToMovilizerQueue> deleteByDeviceAddressIn(Collection<String> deviceAddresses);

}
