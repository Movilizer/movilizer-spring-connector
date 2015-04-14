Story: Automatic request response cycle - rate
==============================================
The requests in the connector will be managed entirely by the connector itself and not by the user in any case. The user
will only be able to adjust the rate and stop the loop altogether for the app that requested it. Important: stopping an
app with an specific system id shared with other apps that are still running will do nothing.

Acceptance
----------
- Can adjust the rate in configuration.
- Can stop the just the app that requested (others must be able to still run).


Story: Automatic request response cycle - exceptions policy
===========================================================
Any transient exceptions in the request-response cycle will be handled by the connector. Only exceptions that lead to
permanent failure will be pass to the user. Examples of exceptions handled by the connector are: cloud request failure,
timeouts, parsing errors, queue rejection (since the user will be already notified), etc.

Acceptance
----------
- Request failure due to connection problems to the cloud (not available) is not passed to the user.
- Request timeout is not passed to the user.
- Queue rejected new items is not immediately reflected to the user.
- Parsing errors in the connector during the request response cycle are not passed to the user (just logged).


Story: Automatic request response cycle - permanent failure
===========================================================
For some special exceptions user action must be taken. This case includes: incorrect auth credentials, cloud still
unreachable after a period of time, database unreachable, etc.

Acceptance
----------
- When user credentials are incorrect an exception should be thrown to the user and the cycle should be stopped for this
particular app.
- When cloud is unreachable after a specific period of time a an exception should be thrown to the user and the cycle
should be stopped for apps affected.
- If database is unreachable throw exception to user (this behaviour could be changed in a future letting the database
actions some chance to retry until the permanent failure is thrown). This exception should stop all the apps since
persistence is needed to properly manage the request response cycle.


Story: Automatic request response cycle - response size
=======================================================
The number of responses requested to the cloud must be in accordance with the queue available capacity and request
process time in relation with the request rate. There will be a minimum reply/data container so the connector will never
fetch 0 replies. This will effectively fill up the queues until they throw and overflow exception. The number of data to
be requested should be no bigger of 50% (configurable) of the space available in the queue and should estimate of
processing time of no more of 50%(configurable). Time estimation requires process time per reply coming from the
business logic metrics.

Acceptance
----------
- No processing time taking into account, queue with more than 40% capacity used, then next request should have the
number of replies set to 50% of what left in the queue.
- Queue is empty, estimated time per reply is given, then next request should have the
number of replies set to 50% of time left given the request rate.
- Using a mixed case the number of replies set for the next request should be the minimum between queue capacity and
processing time available.


Story: Automatic request response cycle - health
================================================
Health status for the cloud access show be exposed for every app of the connector via internal JSON REST API.

Acceptance
----------
- Should be an internal port with a summary with all the apps and connector health in /health
- Per app health can be check in /health/${appName}
- Per app health with version can be check in /health/${appName}/${appVersion}


Story: Automatic request response cycle - timeouts
==================================================
Timeouts for accessing the cloud should be configurable and not lead to undesirable exceptions thrown to the final user.

Acceptance
----------
- Force a timeout in the request with the configured threshold.


Story: Automatic request response cycle - logging policy
========================================================
Logging policy for cloud events should be DEBUG for sync (request send, response received), TRACE (for any individual
events triggered such as movelets acknowledged), ERROR (for any errors coming from the cloud, http connection errors and
timeouts) and INFO for data containers and replies.

Acceptance
----------
- Comprehensive testing in this story is not needed just some manual review for at least one of the levels.
