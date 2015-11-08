package ru.kartashov.specgen.engine;

import org.objectweb.asm.MethodVisitor;
import ru.kartashov.specgen.domain.MethodInfo;
import ru.kartashov.specgen.domain.Usage;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Dmitrii Kartashov
 */
public class OutputClassVisitor extends ClassVisitorAdapter {
    private final Consumer<Usage> usageRegister;
    private final List<MethodInfo> methodInfos;
    private final String jarName;
    private final String className;

    public OutputClassVisitor(Consumer<Usage> usageRegister, List<MethodInfo> methodInfos, String jarName, String className) {
        this.usageRegister = usageRegister;
        this.methodInfos = methodInfos;
        this.jarName = jarName;
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new UsageMethodVisitor(usageRegister, methodInfos, jarName, className, name);
    }
}
