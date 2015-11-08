package ru.kartashov.specgen.domain;

/**
 * @author Dmitrii Kartashov
 */
public class Usage {
    private final String jarName;
    private final String className;
    private final String methodName;
    private final MethodInfo methodInfo;

    public Usage(String jarName, String className, String methodName, MethodInfo methodInfo) {
        this.jarName = jarName;
        this.className = className;
        this.methodName = methodName;
        this.methodInfo = methodInfo;
    }

    public String getJarName() {
        return jarName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public MethodInfo getMethodInfo() {
        return methodInfo;
    }
}
