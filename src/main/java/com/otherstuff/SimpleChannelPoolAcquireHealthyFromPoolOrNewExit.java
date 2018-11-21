package com.otherstuff;

import com.appdynamics.Cache;
import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.contexts.ISDKUserContext;
import com.appdynamics.instrumentation.sdk.template.AExit;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleChannelPoolAcquireHealthyFromPoolOrNewExit {//extends AExit {


    public List<Rule> initializeRules() {
        List<Rule> result = new ArrayList<>();

        Rule.Builder bldr = new Rule.Builder("io.netty.channel.pool.SimpleChannelPool");
        bldr = bldr.classMatchType(SDKClassMatchType.MATCHES_CLASS).classStringMatchType(SDKStringMatchType.EQUALS);
        bldr = bldr.methodMatchString("acquireHealthyFromPoolOrNew").methodStringMatchType(SDKStringMatchType.EQUALS);
        result.add(bldr.build());
        return result;
    }


    public void marshalTransactionContext(String s, Object o, String s1, String s2, Object[] objects, Throwable throwable, Object o1, ISDKUserContext isdkUserContext) throws ReflectorException {
        Cache.weakHashMap.put(System.identityHashCode(objects[0]),s);

    }

    public Map<String, String> identifyBackend(Object o, String s, String s1, Object[] objects, Throwable throwable, Object o1, ISDKUserContext isdkUserContext) throws ReflectorException {
        Map<String, String> retVal = new HashMap();
        try {
            retVal.put("path", o.toString());
        } catch (Exception e) {
            //getLogger().debug("HttpClientOperationsAsyncExitStart.identifyBackend Exception", e);
        }
        return retVal;
    }

    public boolean isCorrelationEnabled() {
        return true;
    }

    public boolean isCorrelationEnabledForOnMethodBegin() {
        return true;
    }

    public boolean resolveToNode() {
        return false;
    }
}
