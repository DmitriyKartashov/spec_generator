package ru.kartashov.specgen.engine;

import org.objectweb.asm.ClassReader;
import ru.kartashov.specgen.domain.MethodInfo;
import ru.kartashov.specgen.domain.Usage;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Dmitrii Kartashov
 */
public class OutputGatherer {
    private final List<MethodInfo> methodInfoList;

    public OutputGatherer(List<MethodInfo> methodInfoList) {
        this.methodInfoList = methodInfoList;
    }

    List<Usage> visitJar(String jarPath) throws Exception
    {
        List<Usage> usages = new ArrayList<>();
        JarFile jarFile = new JarFile(jarPath);

        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            if (entry.getName().endsWith(".class")) {
                InputStream stream = new BufferedInputStream(jarFile.getInputStream(entry), 1024);
                ClassReader reader = new ClassReader(stream);

                OutputClassVisitor classVisitor = new OutputClassVisitor(usages::add, methodInfoList, jarPath, entry.getName().substring(0, entry.getName().length() - 6));
                reader.accept(classVisitor, 0);

                stream.close();
            }
        }
        return usages;
    }
}
