package IO;

import java.io.File;

public interface InputOutput {

    void serialize(Object object, File file);
    Object deserialize(Class c, File file);
}