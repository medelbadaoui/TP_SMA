package Agents;

import java.util.Random;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class VendeurAgent extends GuiAgent {

	VendeurGui vendeurGui;
	@Override
	protected void setup() { 
		if(getArguments().length ==1)
		{
			vendeurGui=(VendeurGui)getArguments()[0];
			vendeurGui.setVendeurAgent(this);
		}
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				DFAgentDescription agentDescription = new DFAgentDescription();
				agentDescription.setName(getAID());
				ServiceDescription serviceDescription = new ServiceDescription();
				serviceDescription.setType("transaction");
				serviceDescription.setName("vente-livre");
				agentDescription.addServices(serviceDescription);
				try {
					DFService.register(myAgent, agentDescription);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
				
			}
		});
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				ACLMessage aclMessage = receive();
				if(aclMessage != null)
				{
					
					switch (aclMessage.getPerformative()) {
					case ACLMessage.CFP:
						vendeurGui.logMessage(aclMessage);
						ACLMessage reply = aclMessage.createReply();
						reply.setContent(String.valueOf(500+new Random().nextInt(1000)));
						reply.setPerformative(ACLMessage.PROPOSE);
						send(reply);
					break ;
					case ACLMessage.ACCEPT_PROPOSAL:
						ACLMessage debug = new ACLMessage();
						debug.setContent("Accept to sell " + aclMessage.getContent());
						vendeurGui.logMessage(debug);
						ACLMessage aclMessage2 = aclMessage.createReply();
						System.out.println("*----------i'm the seller, and i agree pn this offer :: "+aclMessage.getContent()+"-------------*");
						aclMessage2.setPerformative(ACLMessage.AGREE);
						send(aclMessage2);
					break;
					default:
						
					}
					
				}
			}
		});
	}
	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onGuiEvent(GuiEvent event) {
		// TODO Auto-generated method stub
		
	}

}
