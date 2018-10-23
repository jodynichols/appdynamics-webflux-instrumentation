package com.appdynamics;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class KafkaMessage {

    String processid ="";
    String parentid = "";
    String spanId = "";
    String traceId = "";
    String spanName = "";
    Boolean export = true;
    String message = "";
    String header = "";
    Map extendedHeader = new HashMap();

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanName() {
        return spanName;
    }

    public void setSpanName(String spanName) {
        this.spanName = spanName;
    }

    public Boolean getExport() {
        return export;
    }

    public void setExport(Boolean export) {
        this.export = export;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Map getExtendedHeader() {
        return extendedHeader;
    }

    public void setExtendedHeader(Map extendedHeader) {
        this.extendedHeader = extendedHeader;
    }

    public byte[] serialize() {
        byte[] yourBytes = null;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;

            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(new Boolean(true));
                out.writeObject(processid);
                out.writeObject(parentid);
                out.writeObject(spanId);
                out.writeObject(traceId);
                out.writeObject(spanName);
                out.writeBoolean(true);
                out.writeObject(message);
                out.writeObject(header);
                out.writeObject(extendedHeader);
                out.flush();
                yourBytes = bos.toByteArray();
            } finally {
                try {
                    bos.close();
                } catch (IOException ex) {
                }
            }
        } catch (Exception e) {

        }

        return yourBytes;
    }
}
