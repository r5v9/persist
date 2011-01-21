
// $Id$

package net.sf.persist.tests.performance;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.Translator;

/**
 * Poor man's profiler. Instruments the Persist class with Jamon monitors that allow for a report generation.
 */
public class Profiler {

	public static void main(String[] args) throws Throwable {
		
		// add translator to class loader
		Translator t = new JamonTranslator();
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader();
		cl.addTranslator(pool, t);
		
		// execute main class using the class loader
		// let it receive args[] as if it was invoked directly
		String[] argsShifted = new String[args.length-1];
		System.arraycopy(args, 1, argsShifted, 0, args.length-1);
		cl.run(args[0], argsShifted);
	}

}

/**
 * Translator that adds Jamon monitors around every method of the Persist and TableMapping classes
 */
class JamonTranslator implements Translator {
   
	public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
		// do nothing
	}
   
    public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
    	
    	if (classname.equals("net.sf.persist.Persist")) /* || classname.endsWith("net.sf.persist.TableMapping")) */ {
    		
	        CtClass cc = pool.get(classname);
	        for (CtMethod method : cc.getDeclaredMethods()) {
	        	addJamonMonitor(cc, method);
	        }
        
    	}
    }
    
    private static void addJamonMonitor(CtClass cls, CtMethod method) throws NotFoundException, CannotCompileException {
    	
    	// see http://www.ibm.com/developerworks/java/library/j-dyn0916.html
 
         //  rename old method to synthetic name, then duplicate the method with original name for use as interceptor
         String oname = method.getName();
    	 String nname = oname+"$impl";
         method.setName(nname);
         CtMethod mnew = CtNewMethod.copy(method, oname, cls, null);
         
         // start the body text generation by saving the jamon monitor to a local variable, then call the timed method; 
         // the actual code generated needs to depend on whether the timed method returns a value
         String type = method.getReturnType().getName();
         StringBuffer body = new StringBuffer();
         body.append("{\ncom.jamonapi.Monitor mon = com.jamonapi.MonitorFactory.start(\"" + cls.getSimpleName() + "." + oname + "\");\n");
         if (!"void".equals(type)) {
             body.append(type + " result = ");
         }
         body.append(nname + "($$);\n");
         
         //  finish body text generation stopping jamon monitor, and return saved value (if not void)
         body.append("mon.stop();\n");
         if (!"void".equals(type)) {
             body.append("return result;\n");
         }
         body.append("}");
         
         //  replace the body of the interceptor method with generated code block and add it to class
         mnew.setBody(body.toString());
         cls.addMethod(mnew);
         
         //  print the generated code block just to show what was done
         //System.out.println(body.toString());
     }
    
}