Story: Device event triggers - Movelet synced
=============================================
When there are new incoming movelet synced coming from the cloud the user must be able to hook logic having available a
list of the new items. The hooks/triggers must be able to specifically target: system id, movelet key, movelet key
extension, movelet version, participant key, device address and isAcknowledge.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, movelet key,
movelet key extension, movelet version, participant key, device address and isAcknowledge.


Story: Device event triggers - Participant deployment SMS sent
==============================================================
When there are new incoming participant deployment sms coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id and device address.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id and device
address.


Story: Device event triggers - Participant install
==================================================
When there are new incoming participant install coming from the cloud the user must be able to hook logic having
available a list of the new items. The hooks/triggers must be able to specifically target: system id, participant key,
device address, ip address and user agent.

Acceptance
----------
- Business logic code can be attached to the category without indicating any parameter and it will only receive data of
its app (no collision with other apps using the same system id).
- Business logic code can be attached to the category and data is filter using equality with system id, participant key,
device address, ip address and user agent.
