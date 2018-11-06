package com.appdynamics;

import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.template.async.AAsyncExitEnd;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

import java.util.ArrayList;
import java.util.List;

/**
 * created by haojun.li on 7/10/18
 */
public class HttpClientOperationsAsyncExitEnd extends AAsyncExitEnd {
    IReflector context = null;

    private static final String CLASS_TO_INSTRUMENT = "reactor.ipc.netty.channel.ChannelOperations";
    private static final String METHOD_TO_INSTRUMENT = "onHandlerTerminate";

    public HttpClientOperationsAsyncExitEnd(){
        super();
        context = getNewReflectionBuilder()
                .invokeInstanceMethod("context", true).build();
    }

    @Override
    public List<Rule> initializeRules() {
        List<Rule> result = new ArrayList<>();

        Rule.Builder bldr = new Rule.Builder(CLASS_TO_INSTRUMENT);
        bldr = bldr.classMatchType(SDKClassMatchType.MATCHES_CLASS).classStringMatchType(SDKStringMatchType.EQUALS);
        bldr = bldr.methodMatchString(METHOD_TO_INSTRUMENT).methodStringMatchType(SDKStringMatchType.EQUALS);
        result.add(bldr.build());
        return result;
    }

    @Override
    public Object getAsyncObject(Object invokedObject, Object[] paramValues, Object returnValue) {
        Object returnObj = null;


        try {
            returnObj = context.execute(invokedObject.getClass().getClassLoader(), invokedObject);

            //Debugging Only
           // long love = (long)Cache.weakHashMap.remove(System.identityHashCode(returnObj));
           // long love2 = System.currentTimeMillis()-love;
            //getLogger().info("HttpClientOperationsAsyncExitEnd.getAsyncObject  "+System.identityHashCode(returnObj)+" Time:"+love2);

        } catch (Exception e) {
            getLogger().debug("HttpClientOperationsAsyncExitEnd.getAsyncObject Exception",e);
        }
        return returnObj;

    }

}