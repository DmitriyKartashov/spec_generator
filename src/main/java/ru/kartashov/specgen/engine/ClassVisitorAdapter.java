package ru.kartashov.specgen.engine;


import org.objectweb.asm.*;

/**
 * @author Dmitrii Kartashov
 */
public class ClassVisitorAdapter implements ClassVisitor{
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

    }

    @Override
    public void visitSource(String source, String debug) {

    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {

    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    @Override
    public void visitAttribute(Attribute attr) {

    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {

    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return null;
    }

    @Override
    public void visitEnd() {

    }
}
