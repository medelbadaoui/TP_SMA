package Agents;

import java.util.ArrayList;

import java.util.List;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFDBKB;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class AcheteurAgent extends GuiAgent {

	private transient AcheteurGui AcheteurGui ;
	private AID[] listVendeur;
	@Override
	protected void setup() {
		if(getArguments().length==1) 
		{
			AcheteurGui =(AcheteurGui) getArguments()[0];
			AcheteurGui.setAcheteurAgent(this);
		}
		ParallelBehaviour paralleleBehaviour = new ParallelBehaviour();
		addBehaviour(paralleleBehaviour);
		paralleleBehaviour.addSubBehaviour(new TickerBehaviour(this,6000) {
			
			@Override
			protected void onTick() {
				
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription serviceDescription = new ServiceDescription();
				serviceDescription.setType("transaction");
				serviceDescription.setName("vente-livre");
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template);
					listVendeur = new AID[result.length];
					for(int i = 0;i<result.length;i++)
						listVendeur[i] = result[i].getName();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		paralleleBehaviour.addSubBehaviour(new CyclicBehaviour() {
			List<ACLMessage> listPropos = new ArrayList<>();
			@Override
			public void action() {
				// TODO Auto-generated method stub
				ACLMessage aclMessage =receive();
				if(aclMessage!=null)
				{
					
					switch (aclMessage.getPerformative()) {
					case ACLMessage.REQUEST:
						String livre= aclMessage.getContent();
						AcheteurGui.logMessage(aclMessage);
						ACLMessage reply = aclMessage.createReply();
						reply.setPerformative(ACLMessage.CONFIRM);
						reply.setContent("ok pour => "+livre);
						send(reply);
						ACLMessage aclMessage2 = new ACLMessage(ACLMessage.CFP);
						aclMessage2.setContent(livre); 
						for(AID v : listVendeur)
						{
							aclMessage2.addReceiver(v);
						}
						send(aclMessage2);
						break ;
					case ACLMessage.PROPOSE:
						listPropos.add(aclMessage);
						if(listPropos.size() == listVendeur.length)
						{
							double min = Double.parseDouble(listPropos.get(0).getContent());
							ACLMessage melleurOffre = listPropos.get(0);
							for(ACLMessage msg : listPropos)
							{
								if(Double.parseDouble(msg.getContent())  <  min)
								{
									min = Double.parseDouble(msg.getContent());
									melleurOffre = msg;
								}
									
							}
							ACLMessage replAclMessage = melleurOffre.createReply();
							AcheteurGui.logMessage(melleurOffre);
							ACLMessage debug = new ACLMessage();
							debug.setContent("Accept on " + melleurOffre.getContent());
							AcheteurGui.logMessage(debug);
							replAclMessage.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
							send(replAclMessage);
							
						}
						break ;
					case ACLMessage.AGREE:
						ACLMessage aclMessage3 = new ACLMessage(ACLMessage.CONFIRM);
						aclMessage3.addReceiver(new AID("Consumer", AID.ISLOCALNAME));
						aclMessage3.setContent(aclMessage.getContent());
						send(aclMessage3);
						ACLMessage debug = new ACLMessage();
						debug.setContent("buy " + aclMessage3.getContent());
						AcheteurGui.logMessage(debug);
						break;
						
					default:
						throw new IllegalArgumentException("Unexpected value: " + aclMessage.getPerformative());
					}
//					System.out.println("daz hna lACLMessage");
//					String livre= aclMessage.getContent();
//					AcheteurGui.logMessage(aclMessage);
//					ACLMessage reply = aclMessage.createReply();
//					reply.setContent("ok pour => "+livre);
//					send(reply);
//					ACLMessage aclMessage2 = new ACLMessage(ACLMessage.CFP);
//					aclMessage2.setContent(livre); 
//					aclMessage2.addReceiver(new AID("Vendeur", AID.ISLOCALNAME));
//					send(aclMessage2);
//					System.out.println("*******here in the acheteur agent doing the message sending ;)\nthe message : " + aclMessage.getContent());
				}else block();
				
			}
		});
		
		// TODO Auto-generated method stub
		super.setup();
	}
	
	@Override
	protected void afterMove() {
		// TODO Auto-generated method stub
		super.afterMove();
	}
	
	@Override
	protected void beforeMove() {
		// TODO Auto-generated method stub
		super.beforeMove();
	}
	@Override
	protected void onGuiEvent(GuiEvent event) {
		
		
	}

	public void setAcheteurGui(AcheteurGui acheteurGui) {
		AcheteurGui = acheteurGui;
	}

}
