Story: Business logic requirements - modularity
===============================================
Apps should be able to handle simple cases in a simple manner and more complicated cases in a modular fashion.

Acceptance
----------
- Multiple and configurable entry points for business logic that can live in different objects.
- Documentation for business logic modularization.


Story: Business logic requirements - spring autowiring
======================================================
The business logic should be able to autowire using spring IoC container without any special requirements.

Acceptance
----------
- Autowire a service and use it inside movilizer business logic.
- Documentation of the feature.


Story: Business logic requirements - non atomicity in data processing
=====================================================================
Triggers can overlap each other making queue management non explicitly resolved. The strategy to follow in the connector
is to never rollback items in a queue. This prevent double processing by successfully ran triggers and queues overflow
for no good reason. Failed triggers and its related data of that execution should could be saved in a different queue
for later user access (periodic cleanup jobs and the like).

Acceptance
----------
- Any exception thrown during the user business logic does not interfere with the queue management.
- Documentation of this limitation.


Story: Business logic requirements - execution order
====================================================
Triggers on the same category can be executed in a specified order if the user desires it.

Acceptance
----------
- 2 different business logic code snippets with no order or -1 order run in arbitrary order.
- 2 different business logic code snippets, A and B, with order=0 in A and order=1 in B. In execution A always runs
before B and B only runs after A is finished.
- Documentation of this feature.


Story: Business logic requirements - logging
============================================
Logging facilities should be exposed to the user so he can write anything in the remote/persisted log system. This
should be "unified" with he SLF4J logging used in the Spring framework. The logs should automatically include app name,
version and whatever information is relevant to separate information of several app running and the same time.

Acceptance
----------
- The user should no need to call twice to log to SLF4J.
- Log traces are perfectly distinguishable with several apps running at the same time.
- Documentation for logging.


Story: Business logic requirements - automatic metrics
======================================================
Logic should automatically save its metrics of execution, specially times that later will be needed for the request
response cycle.

Acceptance
----------
- Time for executions related to replies and data containers are measured and saved.
- Documentation on how to extract the metrics available in the system.
