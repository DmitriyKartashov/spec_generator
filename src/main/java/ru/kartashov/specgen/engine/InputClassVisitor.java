package ru.kartashov.specgen.engine;

import org.objectweb.asm.MethodVisitor;
import ru.kartashov.specgen.domain.MethodInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitrii Kartashov
 */
public class InputClassVisitor extends ClassVisitorAdapter {
    private String className;
    private List<MethodInfo> methodInfoList = new ArrayList<>();

    public InputClassVisitor(String className) {
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int i, String name, String s1, String s2, String[] strings) {
        methodInfoList.add(new MethodInfo(className, name, name.startsWith("get")));
        return null;
    }

    public List<MethodInfo> getMethodInfoList() {
        return methodInfoList;
    }
}
