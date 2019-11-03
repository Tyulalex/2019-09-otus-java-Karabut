package automatic_logging.agent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class MethodData {

    private final int access;
    private final String name;
    private final String desc;
    private final String signature;
    private final String[] exceptions;
    private final ArgumentsDescriptors argumentsDescriptors;

    public MethodData(int access, String name, String desc, String signature, String[] exceptions) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
        this.argumentsDescriptors = new ArgumentsDescriptors();
    }

    @Getter
    public class ArgumentsDescriptors {
        private final ArrayList<String> argumentsDescriptors;
        private final int size;

        private ArgumentsDescriptors() {
            Type[] types = Type.getArgumentTypes(getDesc());
            this.argumentsDescriptors = (ArrayList<String>) Arrays.stream(types)
                    .map(type -> type.getDescriptor())
                    .collect(Collectors.toList());
            this.size = this.argumentsDescriptors.size();
        }

        public ArrayList<String> get() {
            return this.getArgumentsDescriptors();
        }

        public boolean isEmpty() {
            return this.getSize() == 0;
        }

        @Override
        public String toString() {
            return String.join("", this.getArgumentsDescriptors());
        }
    }
}
