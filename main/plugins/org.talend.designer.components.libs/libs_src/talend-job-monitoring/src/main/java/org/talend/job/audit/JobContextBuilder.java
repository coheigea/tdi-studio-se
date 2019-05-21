package org.talend.job.audit;

import org.talend.logging.audit.Context;
import org.talend.logging.audit.ContextBuilder;

public class JobContextBuilder {

    private final ContextBuilder builder;

    public JobContextBuilder(ContextBuilder builder) {
        this.builder = builder;
    }

    public static JobContextBuilder create() {
        return new JobContextBuilder(ContextBuilder.create());
    }

    public JobContextBuilder jobName(String jobName) {
        builder.with("jobName", jobName);
        return this;
    }
    
    public JobContextBuilder jobVersion(String jobVersion) {
        builder.with("jobVersion", jobVersion);
        return this;
    }
    
    public JobContextBuilder jobId(String jobId) {
        builder.with("jobId", jobId);
        return this;
    }
    
    /**
     * component type like "tXMLMap"
     * @param connectorType
     * @return self
     */
    public JobContextBuilder connectorType(String connectorType) {
        builder.with("connectorType", connectorType);
        return this;
    }
    
    /**
     * component type like "tXMLMap_1"
     * @param connectorType
     * @return self
     */
    public JobContextBuilder connectorId(String connectorId) {
        builder.with("connectorId", connectorId);
        return this;
    }
    
    public JobContextBuilder parameters(String parameters) {
        builder.with("parameters", parameters);
        return this;
    }

    public JobContextBuilder rows(long rowCount) {
        builder.with("rows", String.valueOf(rowCount));
        return this;
    }
    
    //output or reject
    public JobContextBuilder connectionType(String connectionType) {
        builder.with("connectionType", connectionType);
        return this;
    }
    
    //like "row1"
    public JobContextBuilder connectionName(String connectionName) {
        builder.with("connectionName", connectionName);
        return this;
    }
    
    //100s
    public JobContextBuilder time(String time) {
        builder.with("time", time);
        return this;
    }
    
    //500rows/s
    public JobContextBuilder speed(String speed) {
        builder.with("speed", speed);
        return this;
    }

    public Context build() {
        return builder.build();
    }

}