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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by haojun.li on 7/10/18
 */
public class HttpClientOperationsAsyncExitStart extends AAsyncExitStart {

    private static final String CLASS_TO_INSTRUMENT = "reactor.ipc.netty.tcp.TcpClient";
    private static final String METHOD_TO_INSTRUMENT = "newHandler";
    private IReflector bridge = null;
    private IReflector activeURI = null;

    public HttpClientOperationsAsyncExitStart(){
        super();
        bridge = getNewReflectionBuilder()
                .accessFieldValue("bridge", true).build();


        activeURI = getNewReflectionBuilder()
                .accessFieldValue("activeURI", true).build();


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

        Cache.weakHashMap.put(System.identityHashCode(paramValues[0]),correlationHeader);

    }

    @Override
    public Map<String, String> identifyBackend(Object invokedObject, String className, String methodName,
                                               Object[] paramValues, Throwable thrownException, Object returnValue, ISDKUserContext context)
            throws ReflectorException {

        Map<String, String> retVal = new HashMap();
        String pathString;

        try {
            if(paramValues[0]!=null) {
                Object parentObj = bridge.execute(paramValues[0].getClass().getClassLoader(), paramValues[0]);
                pathString =  activeURI.execute(paramValues[0].getClass().getClassLoader(), parentObj).toString();

                String patternString = "(http|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))?";
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(pathString);

                if(!pathString.contains("localhost"))
                    if(matcher.find())
                        pathString = pathString.substring(0, matcher.end());

                retVal.put("path", pathString);
            }else{

                retVal.put("path", invokedObject.toString());

            }

        } catch (Exception e) {
            getLogger().debug("HttpClientOperationsAsyncExitStart.identifyBackend Exception", e);
        }

        return retVal;
    }

    @Override
    public Object getAsyncObject(Object invokedObject, Object[] paramValues, Object returnValue) {
        Object returnObj = null;

        try {
            returnObj = paramValues[0];
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