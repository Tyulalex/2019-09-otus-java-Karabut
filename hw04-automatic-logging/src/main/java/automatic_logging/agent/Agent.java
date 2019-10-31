package automatic_logging.agent;

import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (className.equals("automatic_logging/app/LoggingImpl")) {
                    return modifyClass(classfileBuffer);
                }
                return classfileBuffer;
            }
        });
    }


    private static byte[] modifyClass(byte[] originalClass) {
        ClassReader cr = new ClassReader(originalClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {

            private boolean isAConstructor(String name) {
                return name.equals("<init>");
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc,
                                             String signature, String[] exceptions) {
                if (isAConstructor(name)) {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
                MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
                return new MethodLoggerAdapterVisitor(Opcodes.ASM5, mv,
                        new MethodData(access, name, desc, signature, exceptions)) {
                };
            }

        };
        cr.accept(cv, Opcodes.ASM5);
        return writeToFileAndReturn(cw.toByteArray(), "proxyASM.class");
    }

    private static byte[] writeToFileAndReturn(byte[] finalClass, String fileName) {
        try (OutputStream fos = new FileOutputStream(fileName)) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }
}
