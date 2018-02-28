package source.engine;

import java.io.PrintStream;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Debugger {

    public static final PrintStream out = System.out;

    private static int gl_Error;

    public static void printError() {
        StackTraceElement s = Thread.currentThread().getStackTrace()[2];
        String fullClassPath = s.getClassName();
        String packagePath = fullClassPath.substring(0, fullClassPath.lastIndexOf("."));
        String className = fullClassPath.substring(fullClassPath.lastIndexOf(".") + 1);
        out.println("ERROR in " + className + "." + s.getMethodName() + "() in package \"" + packagePath +
                "\" (Line:" + s.getLineNumber() + ")");
    }

    public static void printError(String message) {
        StackTraceElement s = Thread.currentThread().getStackTrace()[2];
        String fullClassPath = s.getClassName();
        String packagePath = fullClassPath.substring(0, fullClassPath.lastIndexOf("."));
        String className = fullClassPath.substring(fullClassPath.lastIndexOf(".") + 1);
        out.println("ERROR in " + className + "." + s.getMethodName() + "() in package \"" + packagePath +
                "\" (Line:" + s.getLineNumber() + ") : " +
                "\n    \"" + message + "\".");
    }

    public static void printWarning() {
        StackTraceElement s = Thread.currentThread().getStackTrace()[2];
        String fullClassPath = s.getClassName();
        String packagePath = fullClassPath.substring(0, fullClassPath.lastIndexOf("."));
        String className = fullClassPath.substring(fullClassPath.lastIndexOf(".") + 1);
        out.println("WARNING in " + className + "." + s.getMethodName() + "() in package \"" + packagePath +
                "\" (Line:" + s.getLineNumber() + ")");
    }

    public static void printWarning(String message) {
        StackTraceElement s = Thread.currentThread().getStackTrace()[2];
        String fullClassPath = s.getClassName();
        String packagePath = fullClassPath.substring(0, fullClassPath.lastIndexOf("."));
        String className = fullClassPath.substring(fullClassPath.lastIndexOf(".") + 1);
        out.println("WARNING in " + className + "." + s.getMethodName() + "() in package \"" + packagePath +
                "\" (Line:" + s.getLineNumber() + ") : " +
                "\n    \"" + message + "\".");
    }

    public static void glErrorCheck() {
        if ((gl_Error = glGetError()) != GL_NO_ERROR) {
            StackTraceElement s = Thread.currentThread().getStackTrace()[2];
            String fullClassPath = s.getClassName();
            String packagePath = fullClassPath.substring(0, fullClassPath.lastIndexOf("."));
            String className = fullClassPath.substring(fullClassPath.lastIndexOf(".") + 1);
            out.println("ERROR in " + className + "." + s.getMethodName() + "() in package \"" + packagePath +
                    "\" (Line:" + s.getLineNumber() + ") : " +
                    "\n    \"GL Error code: " + gl_Error + "\".");
        }
    }

    public static void begin() {
        Thread c = Thread.currentThread();
        StackTraceElement s = c.getStackTrace()[2];
        out.print(s.getClassName() + "." + s.getMethodName() + "() -> Debugger Begin");
        out.println(" On Thread [" + c.getName() + "]");
        out.println();
    }

}
