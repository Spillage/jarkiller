package io.jenkins.plugins.jarkiller;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import io.jenkins.plugins.jarkiller.mvn.MvnInvoker;
import io.jenkins.plugins.jarkiller.mvn.TreeOutputHandler;
import jenkins.tasks.SimpleBuildStep;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationResult;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringJoiner;

public class JKBuilder extends Builder implements SimpleBuildStep {
    private final String jenkinsHome;
    private final String banJars;

    @DataBoundConstructor
    public JKBuilder(String jenkinsHome, String banJars) {
        this.jenkinsHome = jenkinsHome;
        this.banJars = banJars;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("jarkiller start...");
        if (StringUtils.isEmpty(jenkinsHome) || StringUtils.isEmpty(banJars)) {
            listener.getLogger().println("jenkinsHome:" + jenkinsHome);
            listener.getLogger().println("banJars:" + banJars);
            listener.getLogger().println("wrong config of jenkinsHome or banJars");
            return;
        }
        listener.getLogger().println("jenkinsHome:" + jenkinsHome);
        listener.getLogger().println("banJars:" + banJars);
        TreeOutputHandler.OutputChannel outputChannel = new TreeOutputHandler.OutputChannel(false, listener.getLogger());
        InvocationOutputHandler callback = new TreeOutputHandler(outputChannel, Arrays.asList(banJars.split(",")));
        File root = new File(new StringJoiner("/")
                .add(jenkinsHome)
                .add(workspace.getParent().getName())
                .add(workspace.getName())
                .toString());
        MvnInvoker mvnInvoker = new MvnInvoker(root, "dependency:tree -Dverbose", callback);
        InvocationResult result = mvnInvoker.invoke(true);
        listener.getLogger().println("mvn invoker end...");
        if (result.getExecutionException() != null) return;
        if (outputChannel.isInterrupted()) {
            listener.getLogger().println(outputChannel.getMessage());
            throw new InterruptedException();
        }
        listener.getLogger().println("jarkiller killed none...");
    }

    @Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation doCheckName(@QueryParameter String value, @QueryParameter boolean useFrench)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error(Messages.JKBuilder_DescriptorImpl_errors_missingName());
            if (value.length() < 4)
                return FormValidation.warning(Messages.JKBuilder_DescriptorImpl_warnings_tooShort());
            if (!useFrench && value.matches(".*[éáàç].*")) {
                return FormValidation.warning(Messages.JKBuilder_DescriptorImpl_warnings_reallyFrench());
            }
            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.JKBuilder_DescriptorImpl_DisplayName();
        }

    }

}
