Story: Error event triggers - Masterdata
========================================
When there are new incoming masterdata errors coming from the cloud the user must be able to hook logic having available
a list of the new items. The hooks/triggers must be able to specifically target: system id, masterdata pool, masterdata
group, masterdata key and validation error code.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, masterdata pool,
masterdata group, masterdata key and validation error code.


Story: Error event triggers - Document
======================================
When there are new incoming document errors coming from the cloud the user must be able to hook logic having available a
list of the new items. The hooks/triggers must be able to specifically target: system id, document pool, document key
and validation error code.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, document pool,
document key and validation error code.


Story: Error event triggers - Movelet
=====================================
When there are new incoming movelet errors coming from the cloud the user must be able to hook logic having available a
list of the new items. The hooks/triggers must be able to specifically target: system id, movelet key, movelet key
extension, movelet version and validation error code.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, movelet key,
movelet key extension, movelet version and validation error code.


Story: Error event triggers - Participant install error
=======================================================
When there are new incoming participant install errors coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id, participant key,
device address, ip address, user agent and error code.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, participant key,
device address, ip address, user agent and error code.

