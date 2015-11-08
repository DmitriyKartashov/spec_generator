package ru.kartashov.specgen.engine;

import ru.kartashov.specgen.domain.MethodInfo;
import ru.kartashov.specgen.domain.Usage;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
* @author Dmitrii Kartashov
*/
class UsageMethodVisitor extends MethodVisitorAdapter {
    private final Consumer<Usage> registerUsage;
    private final List<MethodInfo> methodInfos;
    private final String jarName;
    private final String className;
    private final String methodName;

    public UsageMethodVisitor(Consumer<Usage> registerUsage, List<MethodInfo> methodInfos, String jarName, String className, String methodName) {
        this.registerUsage = registerUsage;
        this.methodInfos = methodInfos;
        this.jarName = jarName;
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        Optional<MethodInfo> methodInfo = methodInfos.stream().filter(i -> name.equals(i.getMethodName()) && owner.equals(i.getClassName())).findAny();
        methodInfo.ifPresent(i -> registerUsage.accept(new Usage(jarName, className, methodName, i)));
    }
}
