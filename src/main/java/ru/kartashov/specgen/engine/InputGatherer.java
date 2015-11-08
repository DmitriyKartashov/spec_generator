package ru.kartashov.specgen.engine;

import org.objectweb.asm.ClassReader;
import ru.kartashov.specgen.domain.MethodInfo;

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
public class InputGatherer {
    public List<MethodInfo> visitJar(String jarPath, String className) throws Exception
    {
        List<MethodInfo> methods = new ArrayList<>();
        JarFile jarFile = new JarFile(jarPath);

        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            if (entry.getName().endsWith(".class") && entry.getName().contains(className)) {
                InputStream stream = new BufferedInputStream(jarFile.getInputStream(entry), 1024);
                ClassReader reader = new ClassReader(stream);

                InputClassVisitor classVisitor = new InputClassVisitor(entry.getName().substring(0, entry.getName().length() - 6));
                reader.accept(classVisitor, 0);
                methods.addAll(classVisitor.getMethodInfoList());

                stream.close();
            }
        }
        return methods;
    }
}
