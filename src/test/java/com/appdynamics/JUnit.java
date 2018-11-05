package com.appdynamics;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JUnit  {

    public static final String testString = "POST:/v3.0/jstor/image";
    @Test
    public void test(){

        System.out.println(testString.replaceAll("/$", ""));

    }

}
