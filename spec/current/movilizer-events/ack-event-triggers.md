Story: Ack event triggers - Masterdata
======================================
When there are new incoming masterdata acknowledges coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id, masterdata pool,
masterdata group, masterdata key.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, masterdata pool,
masterdata group and masterdata key.


Story: Ack event triggers - Document
====================================
When there are new incoming document acknowledges coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id, document pool and
document key.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, document pool
and document key.


Story: Ack event triggers - Movelet
===================================
When there are new incoming movelet acknowledges coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id, movelet key and
movelet key extension.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, movelet key and
movelet key extension.


Story: Ack event triggers - Participant
=======================================
When there are new incoming participant acknowledges coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id, participant key,
participant id global, movelet key, movelet key extension and device address.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, participant key,
participant id global, movelet key, movelet key extension and device address.


Story: Ack event triggers - ParticipantInstall
==============================================
When there are new incoming participant install acknowledges coming from the cloud the user must be able to hook logic
having available a list of the new items. The hooks/triggers must be able to specifically target: system id, participant
key, devices address, ip address, user agent.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, participant
key, devices address, ip address, user agent.

