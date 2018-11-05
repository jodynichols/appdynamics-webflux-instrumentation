package com.appdynamics;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.contexts.ISDKUserContext;
import com.appdynamics.instrumentation.sdk.template.async.AAsyncExitStart;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by haojun.li on 7/10/18
 */
public class HttpClientOperationsAsyncExitStart extends AAsyncExitStart {
    private IReflector requestHeadersReflector = null;
    private IReflector header = null;

    private static final String CLASS_TO_INSTRUMENT = "reactor.ipc.netty.http.client.HttpClientOperations";
    private static final String METHOD_TO_INSTRUMENT = "preSendHeadersAndStatus";

    public HttpClientOperationsAsyncExitStart(){
        super();
        requestHeadersReflector = getNewReflectionBuilder()
                .invokeInstanceMethod("requestHeaders", true).build();

        String[] types = new String[]{String.class.getCanonicalName(),Object.class.getCanonicalName()};

        header = getNewReflectionBuilder().invokeInstanceMethod("add", true, types)
                .build();

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
    public void marshalTransactionContext(String correlationHeader, Object invokedObject, String className,
                                          String methodName, Object[] paramValues, Throwable thrownException, Object returnValue,
                                          ISDKUserContext context) throws ReflectorException {

        try {
            Object requestHeaders = requestHeadersReflector.execute(invokedObject.getClass().getClassLoader(), invokedObject);
            header.execute(invokedObject.getClass().getClassLoader(),requestHeaders,new Object[]{ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER,correlationHeader});
        } catch (Exception e) {
            getLogger().debug("HttpClientOperationsAsyncExitStart.marshalTransactionContext Exception",e);

        }

    }

    @Override
    public Map<String, String> identifyBackend(Object invokedObject, String className, String methodName,
                                               Object[] paramValues, Throwable thrownException, Object returnValue, ISDKUserContext context)
            throws ReflectorException {

        Map<String, String> retVal = new HashMap();
        try {
            retVal.put("path", invokedObject.toString().replaceAll("/$", ""));
        } catch (Exception e) {
            getLogger().debug("HttpClientOperationsAsyncExitStart.identifyBackend Exception", e);
        }
        return retVal;

    }

    @Override
    public Object getAsyncObject(Object invokedObject, Object[] paramValues, Object returnValue) {
        Object returnObj = null;

        try {

            returnObj = requestHeadersReflector.execute(invokedObject.getClass().getClassLoader(), invokedObject);
            //Debugging only.
            //Cache.weakHashMap.put(System.identityHashCode(returnObj),System.currentTimeMillis());
        } catch (Exception e) {
            getLogger().debug("HttpClientOperationsAsyncExitStart.getAsyncObject Exception",e);
        }

        return returnObj;

    }

    @Override
    public boolean isCorrelationEnabled() {
        return true;
    }

    @Override
    public boolean isCorrelationEnabledForOnMethodBegin() {
        return true;
    }

    @Override
    public boolean resolveToNode() {
        return true;
    }

    @Override
    public boolean identifyOnEnd() {
        return false;
    }

    @Override
    public boolean getAsyncObjectOnEnd() {
        return false;
    }

}