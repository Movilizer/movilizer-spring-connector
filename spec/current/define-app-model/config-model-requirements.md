Story: Config requirements - easy to use
========================================
The configuration of an app can be done via .properties file, .yml file or java class.

Acceptance
----------
- Can be configured with .properties file.
- Can be configured with .yml file.
- Can be configured with java class.
- Documentation for config with .properties file.
- Documentation for config with .yml file.
- Documentation for config java class.


Story: Config requirements - accessibility
==========================================
The configuration of an app should be accessible from within the app and from outside the app.

Acceptance
----------
- Can be reach within the business logic of a Movilizer App.
- Can be reach from outside a Movilizer App.
- Documentation for config access within and outside the Movilizer app.


Story: Config requirements - modularity
=======================================
The configuration of an app should be capable of living in different files in case the configuration is extensive.

Acceptance
----------
- Multiple config files consolidated in one config instance.
- Documentation for config modularization.


Story: Config requirements - threadsafe access
==============================================
The configuration of an app should be accessible from several threads and read-only so there's no inconsistency in
between calls.

Acceptance
----------
- Multiple threads accessing the same value should have same result.
- Any value change is ignored.
- Documentation for config threadsafe and read-only behaviour.


Story: Config requirements - decoupled
======================================
The configuration of an app should be decoupled from the container (Spring). External resources such as datasource or
external queue should be managed by the container via explicit configuration or JNDI.

Acceptance
----------
- External resources are managed by naming beans in Spring configuration and holding a copy of that name in the
Movilizer app configuration.
- One app can have multiple datasources.
- Two apps running simultaneously can have the same datasource.
- Same app, two different version can use a different datasource.
- Documentation to setup one app can have multiple datasources.
- Documentation to setup two apps running simultaneously can have the same datasource.
- Documentation to setup same app, two different version can use a different datasource.


Story: Config requirements - version robustness
===============================================
Futures versions of the configuration specs shouldn't prevent the old ones from compiling. New added properties
shouldn't prevent old versions from running.

Acceptance
----------
- Can implement a newer version without touching the already existing code (no new properties needs to be
implemented/extended).
- Documentation making the users aware of this feature.


Story: Config requirements - minimum configuration and default values
=====================================================================
The Movilizer apps should be capable of running with almost no configuration. This implies that all non-required
parameters will have defaults values which will be easily overridden in case the users wants to. The only required
values should be: app name, system id and password.

Acceptance
----------
- Can configure an app with minimum values: app name, system id and password.
- Documentation with all the properties available, their default values and when they can be changed at run time.
