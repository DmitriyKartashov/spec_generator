package ru.kartashov.specgen.domain;

/**
 * @author Dmitrii Kartashov
 */
public class MethodInfo {
    private final String className;
    private final String methodName;
    private final boolean getter;

    public MethodInfo(String className, String methodName, boolean getter) {
        this.className = className;
        this.methodName = methodName;
        this.getter = getter;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean isGetter() {
        return getter;
    }
}
