package com.idega.formbuilder.tests.basic;


import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.EndState;
import org.jbpm.graph.node.StartState;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.Task;

import com.idega.formbuilder.business.process.XFormsToTask;
import com.idega.jbpm.def.DefaultViewImpl;
import com.idega.jbpm.def.ViewToTask;

import junit.framework.TestCase;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/09/17 13:31:13 $ by $Author: civilis $
 *
 */
public class XFormsToTaskTest extends TestCase {
	
	static ProcessDefinition process = null;
	
	static {
		try {
			process = 
			      ProcessDefinition.parseXmlInputStream(XFormsToTaskTest.class.getResourceAsStream("oneTask.xml"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	  // get the nodes for easy asserting
	static StartState start = (StartState)process.getStartState();
	static TaskNode taskNode = (TaskNode)process.getNode("task");
	static EndState end = (EndState) process.getNode("end");

	ProcessInstance processInstance;
	
	// the main path of execution
	Token token;

	public void setUp() {
		// create a new process instance for the given process definition
		processInstance = new ProcessInstance(process);

		// the main path of execution is the root token
		token = processInstance.getRootToken();
	}
  
	public void testMainScenario() {
		
		// after process instance creation, the main path of 
		// execution is positioned in the start state.
		assertSame(start, token.getNode());

		token.signal();
		
		// after the signal, the main path of execution has 
		// moved to the auction state
		
		assertSame(taskNode, token.getNode());
		
		ViewToTask v2t = new XFormsToTask();
		
		System.out.println("xsx: "+taskNode.getTasks().iterator().next());
		System.out.println("v2t: "+v2t);
		DefaultViewImpl view = new DefaultViewImpl();
		view.setViewId("view id");
		
		//v2t.bind(view, (Task)taskNode.getTasks().iterator().next());
		
		processInstance.getContextInstance().createVariable("somevar", "theval", token);
		
		System.out.println("fst time: "+processInstance.getContextInstance().getVariable("somevar", token));

		token.signal();

		// after the signal, the main path of execution has 
		// moved to the end state and the process has ended
	    assertSame(end, token.getNode());
	    assertTrue(processInstance.hasEnded());
	}
}