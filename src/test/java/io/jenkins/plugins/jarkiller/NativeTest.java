package io.jenkins.plugins.jarkiller;

import io.jenkins.plugins.jarkiller.mvn.MvnInvoker;
import org.junit.Test;

/**
 * Copyright© 2019
 *
 * @author jie.han
 * @date 2019/7/2
 */
public class NativeTest {
    @Test
    public void test() {
        MvnInvoker mvnInvoker = new MvnInvoker(null, "dependency:tree -Dverbose");
        mvnInvoker.invoke(false);
    }
}
