Story: App definition - accessible
==================================
The app definition class should contain all of the information needed (configuration and runtime) to be able to be
monitored and managed from the outside.

Acceptance
----------
- Can read movilizer specific info from the outside.
- Can access to runtime state from the outside.
- Can shutdown from the outside.
- Can access to business logic methods/classes related to the movilizer connector from the outside.
- Documentation for accessing the exposed properties.


Story: App definition - version robustness
==========================================
It should be easily enhanced and app objects should be able to survive version changes without having to adjust to newer
APIs.

Acceptance
----------
- Can add new methods/fields that are not in the spec.
- Can implement a newer version without touching the already existing code (no new methods needs to be
implemented/extended).
- Documentation making the users aware of this feature.


Story: App definition - unnecessary repetition
==============================================
Boilerplate should always be avoidable.

Acceptance
----------
- 1 class app possible.
- Documentation with the explanation of how to create a need app with just one class.


Story: App definition - modularity
==================================
Apps should be able to handle simple cases in a simple manner and more complicated cases in a modular fashion.

Acceptance
----------
- Multiple and configurable entry points for business logic that can live in different objects.
- Documentation for business logic modularization.


Story: App definition - multi system ids
========================================
Apps should be able to handle multi system ids cases.

Acceptance
----------
- Handle 1 app with 2 system ids.
- Documentation for app with 2 systems ids.


Story: App definition - context awareness
=========================================
Apps should be context aware allowing the developer to run several version (demo and prod) of the same app without
interfering with each other.

Acceptance
----------
- Handle 2 apps with same system id but different version.
- Documentation explaining running 2 versions of the same app.
