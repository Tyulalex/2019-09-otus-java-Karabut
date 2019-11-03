package automatic_logging.agent;

import org.objectweb.asm.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Map;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class MethodLoggerAdapterVisitor extends MethodVisitor {

    private boolean hasLogAnnotation = false;
    private MethodData methodData;
    private static String LOG_ANNOTATION = "Lautomatic_logging/annotation/Log;";

    public MethodLoggerAdapterVisitor(int api) {
        super(api);
    }

    public MethodLoggerAdapterVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    public MethodLoggerAdapterVisitor(int api, MethodVisitor methodVisitor, MethodData methodData) {
        this(api, methodVisitor);
        this.methodData = methodData;

    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(LOG_ANNOTATION)) {
            this.hasLogAnnotation = true;
        }
        return mv.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitCode() {
        mv.visitCode();
        if (this.hasLogAnnotation) {
            addLogMethodNameAndParams(mv);
        }
    }

    private void addLogMethodNameAndParams(MethodVisitor mv) {
        Handle handle = new Handle(
                H_INVOKESTATIC, Type.getInternalName(java.lang.invoke.StringConcatFactory.class), "makeConcatWithConstants",
                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class,
                        Object[].class).toMethodDescriptorString(), false);

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System",
                "out", "Ljava/io/PrintStream;");

        visitVarInst(mv, this.methodData.getArgumentsDescriptors());

        mv.visitInvokeDynamicInsn("makeConcatWithConstants",
                getDescriptorForMakeConcatWithConstants(),
                handle,
                getBootStrapMethodArguments(
                        this.methodData.getArgumentsDescriptors().getSize()));

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V", false);

    }

    private String getBootStrapMethodArguments(int argCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("executed method: ")
                .append(methodData.getName());
        if (argCount <= 0) {
            return sb.toString();
        }
        sb.append(", params: ");
        sb.append("\u0001;".repeat(argCount));
        return sb.toString();
    }

    private String getDescriptorForMakeConcatWithConstants() {
        return "(" + this.methodData.getArgumentsDescriptors().toString() + ")Ljava/lang/String;";
    }

    private void visitVarInst(MethodVisitor mv, MethodData.ArgumentsDescriptors args) {
        if (args.isEmpty()) {
            return;
        }
        Map<String, Integer> map = Map.of(Descriptor.STRING_D.getValue(), Opcodes.ALOAD,
                Descriptor.INT_D.getValue(), Opcodes.ILOAD);
        ArrayList<String> descriptors = args.get();
        for (int i = 0; i < args.getSize(); i++) {
            mv.visitVarInsn(map.get(descriptors.get(i)), i + 1);
        }
    }
}
