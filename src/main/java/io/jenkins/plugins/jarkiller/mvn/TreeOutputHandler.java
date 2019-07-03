package io.jenkins.plugins.jarkiller.mvn;

import org.apache.maven.shared.invoker.InvocationOutputHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Copyright© 2019
 *
 * @author jie.han
 * @date 2019/7/2
 */
public class TreeOutputHandler implements InvocationOutputHandler {
    private OutputChannel outputChannel;
    private List<String> banJars;

    public TreeOutputHandler(OutputChannel outputChannel, List<String> banJars) {
        this.outputChannel = outputChannel;
        this.banJars = banJars;
    }

    @Override
    public void consumeLine(String s) throws IOException {
        outputChannel.getPrintStream().println("mvn line:" + s);
        for (String jar : banJars) {
            if (s.contains(jar)) {
                outputChannel.setInterrupted(true);
                outputChannel.setMessage("must not import jar:" + jar);
                return;
            }
        }
    }

    public static class OutputChannel {
        private boolean interrupted;
        private PrintStream printStream;
        private String message;

        public OutputChannel(boolean interrupted, PrintStream printStream) {
            this(interrupted, printStream, null);
        }

        public OutputChannel(boolean interrupted, PrintStream printStream, String message) {
            this.interrupted = interrupted;
            this.printStream = printStream;
            this.message = message;
        }

        public boolean isInterrupted() {
            return interrupted;
        }

        public void setInterrupted(boolean interrupted) {
            this.interrupted = interrupted;
        }

        public PrintStream getPrintStream() {
            return printStream;
        }

        public void setPrintStream(PrintStream printStream) {
            this.printStream = printStream;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
