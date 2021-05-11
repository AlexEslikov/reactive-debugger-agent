# Reactive debugger agent

This is a java agent that allows you to collect 
information about data flowing through a reactive stream.

## Attaching the agent to a JVM

To attach agent simply specify run a JVM with the parameter:

`-agentpath:/path/to/agent/library`.


You may also install an agent on the currently 
running Java virtual machine and re-process existing classes, using:

```java
    ReactiveDebuggerAgent.INSTANCE.init();
    ReactiveDebuggerAgent.INSTANCE.processExistingClasses();
```

## Limitations

`ReactiveDebuggerAgent` is implemented as a Java Agent and
uses
[ByteBuddy](https://bytebuddy.net/#/)
to perform the self-attach. Self-attach may not work on some
JVMs, please refer to ByteBuddy's documentation for more
details.

