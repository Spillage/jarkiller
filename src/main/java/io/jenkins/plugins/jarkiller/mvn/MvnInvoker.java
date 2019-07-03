package io.jenkins.plugins.jarkiller.mvn;

import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;

/**
 * Copyright© 2019
 *
 * @author jie.han
 * @date 2019/7/2
 */
public class MvnInvoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(MvnInvoker.class);
    private InvocationRequest request;
    private InvocationOutputHandler callback;

    public MvnInvoker() {
        System.setProperty("maven.home", System.getenv("MAVEN_HOME"));
    }

    public MvnInvoker(File pomFile, String goal) {
        this();
        this.request = new DefaultInvocationRequest();
        this.request.setPomFile(pomFile);
        this.request.setGoals(Collections.singletonList(goal));
    }

    public MvnInvoker(File pomFile, String goal, InvocationOutputHandler callback) {
        this(pomFile, goal);
        this.callback = callback;
    }

    public InvocationResult invoke() {
        return invoke(false);
    }

    public InvocationResult invoke(boolean isCallback) {
        if (isCallback && callback != null) {
            request.setOutputHandler(callback);
        }
        try {
            return new DefaultInvoker().execute(request);
        } catch (MavenInvocationException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}
