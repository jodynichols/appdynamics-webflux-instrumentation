package com.appdynamics;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.contexts.ISDKUserContext;
import com.appdynamics.instrumentation.sdk.template.AEntry;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebfluxServerInstrumentation extends AEntry {

    private static final String CLASS_TO_INSTRUMENT = "reactor.netty.http.server.HttpServerOperations";
    private static final String METHOD_TO_INSTRUMENT = "onInboundNext";
    private IReflector requestHeadersReflector = null;
    private IReflector header = null;

    private boolean identifyBt = true;

    public WebfluxServerInstrumentation() {
        super();
        requestHeadersReflector = getNewReflectionBuilder()
                .invokeInstanceMethod("requestHeaders", true).build();
        String[] types = new String[]{String.class.getCanonicalName()};

        header = getNewReflectionBuilder().invokeInstanceMethod("get", true, types)
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
    public String unmarshalTransactionContext(Object invokedObject, String className, String methodName,
                                              Object[] paramValues, ISDKUserContext context) throws ReflectorException {
        try {

            Object requestHeaders = requestHeadersReflector.execute(invokedObject.getClass().getClassLoader(), invokedObject);
            String result = header.execute(invokedObject.getClass().getClassLoader(),requestHeaders,new Object[]{ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER});
            return result;

        } catch (Exception et) {
            getLogger().debug("WebfluxConsumerInstrumentation.unmarshalTransactionContext", et);
        }
        return null;
    }

    @Override
    public String getBusinessTransactionName(Object invokedObject, String className,
                                             String methodName, Object[] paramValues, ISDKUserContext context) throws ReflectorException {
        String result = "SDK Default BT";
        String input;

        if (identifyBt) {
            input = invokedObject.toString().replaceAll("/$", "");
            int paramStart = input.indexOf("?");

            // trim off any parameters - prob not needed with regex but oh well
            if(paramStart>0)
                result = input.substring(0, paramStart);
            else
                result = input;

            //"bad" URLs contain account numbers as part of URL, trying to remove those from string by finding numbers after slash
            String patternString = "(?:(?!\\/\\d).)*";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(result);

            if (matcher.find()) {
                result = result.substring(0, matcher.end());
            }
        }
        return result;
    }

    @Override
    public boolean isCorrelationEnabled() {
        return true;
    }

    @Override
    public boolean isCorrelationEnabledForOnMethodBegin() {
        return true;
    }
}