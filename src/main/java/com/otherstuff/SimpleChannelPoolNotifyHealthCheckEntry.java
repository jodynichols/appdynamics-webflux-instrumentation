package com.otherstuff;

import com.appdynamics.Cache;
import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.contexts.ISDKUserContext;
import com.appdynamics.instrumentation.sdk.template.AEntry;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

import java.util.ArrayList;
import java.util.List;

public class SimpleChannelPoolNotifyHealthCheckEntry {//extends AEntry {
    private static final String CLASS_TO_INSTRUMENT = "io.netty.channel.pool.SimpleChannelPool";
    private static final String METHOD_TO_INSTRUMENT = "notifyHealthCheck";


    private boolean identifyBt = true;

    public SimpleChannelPoolNotifyHealthCheckEntry() {
        super();
    }

    public List<Rule> initializeRules() {
        List<Rule> result = new ArrayList<>();

        Rule.Builder bldr = new Rule.Builder(CLASS_TO_INSTRUMENT);
        bldr = bldr.classMatchType(SDKClassMatchType.MATCHES_CLASS).classStringMatchType(SDKStringMatchType.EQUALS);
        bldr = bldr.methodMatchString(METHOD_TO_INSTRUMENT).methodStringMatchType(SDKStringMatchType.EQUALS);
        result.add(bldr.build());



        return result;
    }

    public String unmarshalTransactionContext(Object invokedObject, String className, String methodName,
                                              Object[] paramValues, ISDKUserContext context) throws ReflectorException {
        String transactionId = (String)Cache.weakHashMap.get(System.identityHashCode(paramValues[2]));
        if(transactionId!=null) {
            return transactionId;
        }
        return null;
    }

    public String getBusinessTransactionName(Object invokedObject, String className,
                                             String methodName, Object[] paramValues, ISDKUserContext context) throws ReflectorException {
        String result = null;
        if (identifyBt)
            result = new String(invokedObject.toString().replaceAll("/$", ""));
        return result;
    }

    public boolean isCorrelationEnabled() {
        return true;
    }

    public boolean isCorrelationEnabledForOnMethodBegin() {
        return true;
    }
}
