Story: Deletion event triggers - Masterdata
===========================================
When there are new incoming masterdata deletes coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id, masterdata pool,
masterdata group and masterdata key.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, masterdata pool,
masterdata group and masterdata key.


Story: Deletion event triggers - Document
=========================================
When there are new incoming document deletes coming from the cloud the user must be able to hook logic having available
a list of the new items. The hooks/triggers must be able to specifically target: system id, document pool and document
key.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, document pool
and document key.


Story: Deletion event triggers - Movelet
========================================
When there are new incoming movelet deletes coming from the cloud the user must be able to hook logic having available
a list of the new items. The hooks/triggers must be able to specifically target: system id, movelet key, movelet key
extension and movelet version.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, movelet key,
movelet key extension and movelet version.


Story: Deletion event triggers - Movelet Assignment
===================================================
When there are new incoming movelet assignment deletes coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id, movelet key,
movelet key extension, movelet version, participant key and device address.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, movelet key,
movelet key extension, movelet version, participant key and device address.


Story: Deletion event triggers - Participant
============================================
When there are new incoming participant deletes coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id, participant key,
participant id global and device address.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, participant key,
participant id global and device address.
