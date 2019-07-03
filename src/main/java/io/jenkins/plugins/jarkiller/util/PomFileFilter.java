package io.jenkins.plugins.jarkiller.util;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Copyright© 2019
 *
 * @author jie.han
 * @date 2019/7/2
 */
public class PomFileFilter implements FileFilter, Serializable {
    private Pattern pattern = Pattern.compile(".*pom\\.xml");

    @Override
    public boolean accept(File pathname) {
        String fileName = pathname.getName();
        return pattern.matcher(fileName).matches();
    }
}
