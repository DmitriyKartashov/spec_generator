package ru.kartashov.specgen;

import jdk.internal.org.objectweb.asm.commons.Method;
import org.objectweb.asm.*;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class App {
    private String targetClass;
    private Method targetMethod;

    private AppClassVisitor cv;

    private ArrayList<Callee> callees = new ArrayList<Callee>();

    private static class Callee {
        String className;
        String methodName;
        String methodDesc;
        String source;
        int line;

        public Callee(String cName, String mName, String mDesc, String src, int ln) {
            className = cName; methodName = mName; methodDesc = mDesc; source = src; line = ln;
        }
    }

    private class AppMethodVisitor extends MethodAdapter {

        boolean callsTarget;
        int line;

        public AppMethodVisitor() { super(new MethodVisitor() {
            @Override
            public AnnotationVisitor visitAnnotationDefault() {
                return null;
            }

            @Override
            public AnnotationVisitor visitAnnotation(String s, boolean b) {
                return null;
            }

            @Override
            public AnnotationVisitor visitParameterAnnotation(int i, String s, boolean b) {
                return null;
            }

            @Override
            public void visitAttribute(Attribute attribute) {

            }

            @Override
            public void visitCode() {

            }

            @Override
            public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {

            }

            @Override
            public void visitInsn(int i) {

            }

            @Override
            public void visitIntInsn(int i, int i1) {

            }

            @Override
            public void visitVarInsn(int i, int i1) {

            }

            @Override
            public void visitTypeInsn(int i, String s) {

            }

            @Override
            public void visitFieldInsn(int i, String s, String s1, String s2) {

            }

            @Override
            public void visitMethodInsn(int i, String s, String s1, String s2) {

            }

            @Override
            public void visitJumpInsn(int i, Label label) {

            }

            @Override
            public void visitLabel(Label label) {

            }

            @Override
            public void visitLdcInsn(Object o) {

            }

            @Override
            public void visitIincInsn(int i, int i1) {

            }

            @Override
            public void visitTableSwitchInsn(int i, int i1, Label label, Label[] labels) {

            }

            @Override
            public void visitLookupSwitchInsn(Label label, int[] ints, Label[] labels) {

            }

            @Override
            public void visitMultiANewArrayInsn(String s, int i) {

            }

            @Override
            public void visitTryCatchBlock(Label label, Label label1, Label label2, String s) {

            }

            @Override
            public void visitLocalVariable(String s, String s1, String s2, Label label, Label label1, int i) {

            }

            @Override
            public void visitLineNumber(int i, Label label) {

            }

            @Override
            public void visitMaxs(int i, int i1) {

            }

            @Override
            public void visitEnd() {

            }
        }); }

        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            if (owner.equals(targetClass)
                && name.equals(targetMethod.getName())
                && desc.equals(targetMethod.getDescriptor())) {
                callsTarget = true;
            }
        }

        public void visitCode() {
            callsTarget = false;
        }

        public void visitLineNumber(int line, Label start) {
            this.line = line;
        }

        public void visitEnd() {
            if (callsTarget)
                callees.add(new Callee(cv.className, cv.methodName, cv.methodDesc,
                    cv.source, line));
        }
    }

    private class AppClassVisitor extends ClassAdapter {

        private AppMethodVisitor mv = new AppMethodVisitor();

        public String source;
        public String className;
        public String methodName;
        public String methodDesc;

        public AppClassVisitor() { super(new ClassVisitor() {
            @Override
            public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {

            }

            @Override
            public void visitSource(String s, String s1) {

            }

            @Override
            public void visitOuterClass(String s, String s1, String s2) {

            }

            @Override
            public AnnotationVisitor visitAnnotation(String s, boolean b) {
                return null;
            }

            @Override
            public void visitAttribute(Attribute attribute) {

            }

            @Override
            public void visitInnerClass(String s, String s1, String s2, int i) {

            }

            @Override
            public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {
                return null;
            }

            @Override
            public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
                return null;
            }

            @Override
            public void visitEnd() {

            }
        }); }

        public void visit(int version, int access, String name,
                          String signature, String superName, String[] interfaces) {
            className = name;
        }

        public void visitSource(String source, String debug) {
            this.source = source;
        }

        @Override
        public void visitAttribute(Attribute attribute) {

        }

        public MethodVisitor visitMethod(int access, String name,
                                         String desc, String signature,
                                         String[] exceptions) {
            methodName = name;
            methodDesc = desc;

            return mv;
        }
    }


    public void findCallingMethodsInJar(String jarPath, String targetClass,
                                        String targetMethodDeclaration) throws Exception {

        this.targetClass = targetClass;
        this.targetMethod = Method.getMethod(targetMethodDeclaration);

        this.cv = new AppClassVisitor();

        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            if (entry.getName().endsWith(".class")) {
                InputStream stream = new BufferedInputStream(jarFile.getInputStream(entry), 1024);
                ClassReader reader = new ClassReader(stream);

                reader.accept(cv, 0);

                stream.close();
            }
        }
    }


    public static void main( String[] args ) {
        try {
            App app = new App();

            app.findCallingMethodsInJar(args[0], args[1], args[2]);

            for (Callee c : app.callees) {
                System.out.println(c.source+":"+c.line+" "+c.className+" "+c.methodName+" "+c.methodDesc);
            }

            System.out.println("--\n"+app.callees.size()+" methods invoke "+
                app.targetClass+" "+
                app.targetMethod.getName()+" "+app.targetMethod.getDescriptor());
        } catch(Exception x) {
            x.printStackTrace();
        }
    }

}