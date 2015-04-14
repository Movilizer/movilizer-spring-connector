Story: Automatic trigger registration - component scan
======================================================
The connector should look for the required business logic automatically coming from a Movilizer App definition. This
means that there should be some component scan with sensible defaults and configurable extensions to look for the
related modules and logic of an app.

Acceptance
----------
- An app without any extra config should be able to find its own triggers.
- An app with specific config should be able to hook triggers out of the default way.
