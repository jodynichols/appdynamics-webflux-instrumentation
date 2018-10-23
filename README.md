# AppDynamics WebFlux Instrumentation

## Use Case
Enables HTTP Requests to and from WebFlux components.

## Installation

1. Copy https://github.com/appdynamicsdh/appdynamics-webflux-instrumentation/blob/master/target/AsynciSDK-1.0-SNAPSHOT.jar to /opt/appdynamics/javaagent/verx.x.x.x/sdk-plugins
2. Copy https://github.com/appdynamicsdh/appdynamics-webflux-instrumentation/blob/master/custom-activity-correlation.xml to /opt/appdynamics/javaagent/verx.x.x.x/conf to enable tracking of react threads.
3. Add -Dallow.unsigned.sdk.extension.jars=true to the java agent command line.
4. Restart the Java Agent process.
5. Watch as you see the correlation in the AppDynamics controller (Can take up to 5 minutes).

![Screenshot](https://github.com/appdynamicsdh/appdynamics-webflux-instrumentation/blob/master/webflux.png)

6.Set up a new Async Demarcation point. In the example I was using I had to set up an Async Demarcation point to track the end to end response time for the BT (not the exit call, that's what the plugin does). I did that on "reactor.ipc.netty.http.client.HttpClientOperations/setNettyResponse". There may be a better place to do this.

The results of doing that are below:

![Screenshot](https://github.com/appdynamicsdh/appdynamics-webflux-instrumentation/blob/master/webflux2.png)

![Screenshot](https://github.com/appdynamicsdh/appdynamics-webflux-instrumentation/blob/master/webflux3.png)
