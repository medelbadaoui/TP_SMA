package mainContainer;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class MainContainer {
	
	public static void main(String[] args) throws Exception {
		Runtime runtime = Runtime.instance();
		ProfileImpl profileImpl = new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.GUI, "true");
		AgentContainer mainContainer = runtime.createMainContainer(profileImpl);
		mainContainer.start();
	}
}
