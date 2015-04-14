Story: Replies event triggers - Reply movelet
=============================================
When there are new incoming movelet replies coming from the cloud the user must be able to hook logic having available a
list of the new items. The hooks/triggers must be able to specifically target: system id, movelet key, movelet extension
key, movelet version, participant key, device address and reply upload priority.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, movelet key,
movelet extension key, movelet version, participant key, device address and reply upload priority.


Story: Replies event triggers - Meta movelet reply
==================================================
When there are new incoming meta movelet replies coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id, movelet key,
movelet extension key, movelet version, participant key, device address, ip address and meta action.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, movelet key,
movelet extension key, movelet version, participant key, device address, ip address and meta action.
