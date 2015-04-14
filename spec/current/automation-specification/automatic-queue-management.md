Story: Automatic queue management - transparency
================================================
Actions enqueued by the connector API should be enqueued and dequeued in a transparent user manner. This means that the
connector is in charge of the addition to the queue and the deletion after the action is being executed (without taking
into account the result of the operation, success or failure).

Acceptance
----------
- Add correct action -> execute a successful cloud sync -> action is no longer in the queue.
- Add incorrect action -> execute a successful cloud sync -> action is no longer in the queue.


Story: Automatic queue management - robustness
==============================================
Enqueued actions should survive failures of communication with the cloud.

Acceptance
----------
- Add correct action -> execute a non successful cloud sync -> action is still present in the queue.


Story: Automatic queue management - overflow and rejection mechanism
====================================================================
Enqueued actions should have a configurable limit, signal 90% size and reject when 100% is reached.

Acceptance
----------
- Can configure limit.
- Signal is send out to the user business logic code when 90% is reached.
- Any more actions after 100% get rejected.


Story: Automatic queue management - user exception interactions
===============================================================
Data dequeued can be re-enqueued (or not be dequeued in the first place) in case the user explicitly requires so.
The intention behind this feature is to empower the user's business logic with the ability to rollback when reading from
the data containers queue or the replies in case there's an error in its business logic.

Acceptance
----------
- Read from queue, throw exception in user's code, read again and the queue should have at least the same data.
