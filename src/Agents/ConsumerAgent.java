package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import mainContainer.ConsumerContainer;

public class ConsumerAgent extends GuiAgent {
	
	private transient ConsumerContainer gui;
	@Override
	protected void setup()
	{
		if(getArguments().length ==1)
		{
			gui = (ConsumerContainer) getArguments()[0];
			gui.setConsumerAgent(this);
		}
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		
		addBehaviour(parallelBehaviour);
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				System.out.println("ahmed");
				// TODO Auto-generated method stub
				ACLMessage aclMessage = receive();
				if(aclMessage != null)
				{
					if(ACLMessage.AGREE == aclMessage.getPerformative()|| ACLMessage.CONFIRM == aclMessage.getPerformative())
						gui.logMessage(aclMessage);
				}
				else block();
				
			}
		});
		
		//behaviour
		
	}
	
	@Override
	protected void afterMove() {
		System.out.println("apres la megration");
	}
	
	@Override
	protected void beforeMove() {
		System.out.println("************");
		System.out.println("avant la megration");
		System.out.println("************");
	}
	@Override
	protected void takeDown() {
		System.out.println("************");
		System.out.println("i'm going to die");
		System.out.println("************");
	}

	@Override
	public void onGuiEvent(GuiEvent params) {
		// TODO Auto-generated method stub
		if(params.getType()==1)
		{
			String livre = params.getParameter(0).toString();
			ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
			aclMessage.setContent(livre);
			aclMessage.addReceiver(new AID("Acheteur",AID.ISLOCALNAME));
			send(aclMessage);
			 
			System.out.println("here in the consumer agent doing the message sending ;)\n the message : " + aclMessage.getContent());
			
		}
		
	}
	
}
