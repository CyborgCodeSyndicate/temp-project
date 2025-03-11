package com.theairebellion.zeus.maven.plugins.allocator.discovery;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class ClassFileDiscovery {

    private ClassFileDiscovery() {
    }


    public static List<File> findClassFiles(File baseDir) {
        List<File> results = new ArrayList<>();
        Queue<File> queue = new LinkedList<>();
        queue.add(baseDir);

        while (!queue.isEmpty()) {
            File current = queue.poll();

            if (current.isDirectory()) {
                File[] children = current.listFiles();
                if (children != null) {
                    Collections.addAll(queue, children);
                }
            } else if (current.getName().endsWith(".class")) {
                results.add(current);
            }
        }

        return results;
    }


    public static String fileToClassName(File classFile, File baseDir) {
        String absolutePath = classFile.getAbsolutePath();
        String basePath = baseDir.getAbsolutePath();

        return absolutePath.substring(basePath.length() + 1)
                   .replace(File.separatorChar, '.')
                   .replace(".class", "");
    }

}
