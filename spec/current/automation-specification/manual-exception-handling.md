Story: Manual exception handling - pointcut of execution
========================================================
Hooks should be in place for the user to be able to handle important exceptions thrown by the connector (those whose
access doesn't fall directly after user invocation).

Acceptance
----------
- All permanent failures should have their own hook in the same way that triggers are laid out.


Story: Manual exception handling - information available
========================================================
On the hooks there should be enough information for the user to make meaningful actions.

Acceptance
----------
- Reasons that caused the exception should be input parameters of the method in charge of the handling.


Story: Manual exception handling - rejections
=============================================
This special case of exception should provide the item of the queue which was rejected in case the user wants to deal
with it.

Acceptance
----------
- The rejected value is part of the parameters of the method to be called.
