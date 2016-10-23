package up.client;

import java.util.HashMap;
import java.util.Map;

public class Command {
	
	Map<Runnable, Integer>      runs = new HashMap<Runnable, Integer>();
	Runnable action;
	
	void order(Runnable comLine){
		runs.put(comLine, 0);
		}
	
	void report(Runnable comLine){
		runs.put(comLine, 1);
		checkExec();
		}
	
	void action(Runnable act){
		this.action = act;
		}
	
	void checkExec(){
		if ( ! this.runs.entrySet().contains(0) ) 
				action.run();
	}

	public void report() {
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 

		String calleeMethod = elements[0].getMethodName(); 
		String callerMethodName = elements[1].getMethodName(); 
		String callerClassName = elements[1].getClassName(); 

		System.out.println("CallerClassName=" + callerClassName + " , Caller method name: " + callerMethodName); 
		System.out.println("Callee method name: " + calleeMethod); 
		
	}

	
	public static void main(String[] args){
		Command c = new Command();
		c.report();
		
	} 
	
}
