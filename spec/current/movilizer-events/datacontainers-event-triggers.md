Story: Datacontainers event triggers - uploadContainer
======================================================
When there are new datacontainers coming from the cloud the user must be able to hook logic having available a list of
the new items. The hooks/triggers must be able to specifically target: system id, upload priority, datacontainer key,
movelet key, movelet key extension, movelet version, participant key and device address.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, upload priority,
datacontainer key, movelet key, movelet key extension, movelet version, participant key and device address.
