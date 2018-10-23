package com.appdynamics;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JUnit  {

    @Test
    public void test(){
        System.out.println(isPrimeSingleTest(5,2,31));
    }
    private boolean isPrimeSingleTest(long i, long w, long candidate) {
        long iSquared= i * i;
        if ( iSquared <= candidate) {
            if (candidate % i == 0) {
                return false;
            } else {
                return isPrimeSingleTest(i + w, 6 - w, candidate);
            }
        } else {
            return true;
        }
    }

    public String deserializeObject(byte[] rowObject)
    {
        Map extendedHeader = null;

        try {

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(rowObject));

            final Object obj = ois.readObject();

            if (Boolean.TRUE.equals(obj))
            {

                Object processid = (String) ois.readObject();
                Object parentid = (String) ois.readObject();
                Object  spanId = (String) ois.readObject();
                Object  traceId = (String) ois.readObject();
                Object  spanName = (String) ois.readObject();
                boolean export =  ois.readBoolean();
                Object  message = ois.readObject();

                try {
                    Object header = ois.readObject();
                }catch(Exception e){
                    e.printStackTrace();
                }

                extendedHeader = (Map) ois.readObject();
            }
            else
            {
                Object span = (String) ois.readObject();
                Object message = ois.readObject();

                try {
                    Object header = ois.readObject();
                }catch(Exception e){
                    e.printStackTrace();
                }

                extendedHeader = (Map) ois.readObject();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        if(extendedHeader==null){
            System.out.println("EXTENDED HEADER IS NULL: "+extendedHeader);
            try{
                String tempFix = new String(rowObject);
                return tempFix.substring(tempFix.indexOf("appId"),tempFix.indexOf("donotresolve=true")+17);}
            catch(Exception e){
                return "";
            }

        }else{
            return (String)extendedHeader.get(ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER);
        }
    }
}
