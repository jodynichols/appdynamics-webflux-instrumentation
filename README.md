# AppDynamics WebFlux Instrumentation

## Use Case
Enables HTTP Requests to and from WebFlux components.

## Installation

1. Copy https://github.com/appdynamicsdh/appdynamics-webflux-instrumentation/blob/master/target/AsynciSDK-1.0-SNAPSHOT.jar to /opt/appdynamics/javaagent/verx.x.x.x/sdk-plugins
2. Copy
3. Add -Dallow.unsigned.sdk.extension.jars=true to the java agent command line.
4. Restart the Java Agent process.
5. Watch as you see the correlation in the AppDynamics controller (Can take up to 5 minutes).

![Screenshot](https://github.com/appdynamicsdh/appdynamics-webflux-instrumentation/blob/master/webflux.png)
