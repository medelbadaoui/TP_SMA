package Agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AcheteurGui extends Application {

	private AcheteurAgent acheteurAgent;
	ObservableList<String> observableListAcheteur;
	public static void main(String[] args) {
		launch(args);
	}
	
	private void startContainer() throws Exception {
		Runtime runetime = Runtime.instance();
		ProfileImpl profileImpl = new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		AgentContainer agentContainer = runetime.createAgentContainer(profileImpl);
		AgentController agentController = agentContainer.createNewAgent("Acheteur", "Agents.AcheteurAgent", new Object[] {this});
		agentController.start();
	}  
	@Override
	public void start(Stage window) throws Exception {
		// TODO Auto-generated method stub
		startContainer();

		window.setTitle("Acheteur");
		BorderPane mainPane = new BorderPane();
		Scene mainScene = new Scene(mainPane);
		VBox vBox =new VBox(20);
		observableListAcheteur = FXCollections.observableArrayList();
		ListView<String> listViewAcheteur = new ListView<String>(observableListAcheteur);
		
		vBox.getChildren().add(listViewAcheteur);
		mainPane.setCenter(vBox);
		window.setScene(mainScene);
		window.show();
		
		
	}
	
	
	public void logMessage(ACLMessage message)
	{
		Platform.runLater(()->{
			observableListAcheteur.add(message.getContent() + ", " + message.getSender());
		});
		
	}

	public void setAcheteurAgent(AcheteurAgent acheteurAgent) {
		this.acheteurAgent = acheteurAgent;
	}

}
