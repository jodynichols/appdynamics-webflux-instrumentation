package com.appdynamics;

import com.appdynamics.apm.appagent.api.AgentDelegate;
import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.template.AGenericInterceptor;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleChannelPoolNotifyHealthCheck extends AGenericInterceptor{
    private IReflector requestHeadersReflector = null;
    private IReflector handler = null;

    private IReflector header = null;

    public SimpleChannelPoolNotifyHealthCheck(){
        handler = getNewReflectionBuilder()
                .accessFieldValue("handler", true).build();

        requestHeadersReflector = getNewReflectionBuilder()
                .invokeInstanceMethod("requestHeaders", true).build();


        String[] types = new String[]{String.class.getCanonicalName(),Object.class.getCanonicalName()};
        header = getNewReflectionBuilder().invokeInstanceMethod("add", true, types)
                .build();
    }

    @Override
    public List<Rule> initializeRules() {



        List<Rule> result = new ArrayList<>();

        Rule.Builder bldr = new Rule.Builder("reactor.ipc.netty.http.client.HttpClientOperations");
        bldr = bldr.classMatchType(SDKClassMatchType.MATCHES_CLASS).classStringMatchType(SDKStringMatchType.EQUALS);
        bldr = bld r.methodMatchString("preSendHeadersAndStatus").methodStringMatchType(SDKStringMatchType.EQUALS);
        result.add(bldr.build());




        return result;

    }


    @Override
    public Object onMethodBegin(Object invokedObject, String s, String s1, Object[] objects) {

        try {

            String correlationHeader = (String)Cache.weakHashMap.remove(System.identityHashCode(handler.execute(invokedObject.getClass().getClassLoader(), invokedObject)));
            Object requestHeaders = requestHeadersReflector.execute(invokedObject.getClass().getClassLoader(), invokedObject);
            header.execute(invokedObject.getClass().getClassLoader(),requestHeaders,new Object[]{ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER,correlationHeader});
        } catch (Exception e) {
            getLogger().debug("HttpClientOperationsAsyncExitStart.marshalTransactionContext Exception",e);

        }

        return null;
    }

    @Override
    public void onMethodEnd(Object o, Object o1, String s, String s1, Object[] objects, Throwable throwable, Object o2) {
       // AgentDelegate.getTransactionDemarcator().removeCurrentThreadFromTransaction(throwable);

    }


}
