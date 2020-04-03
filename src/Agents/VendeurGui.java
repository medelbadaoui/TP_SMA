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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox; 
import javafx.stage.Stage;

public class VendeurGui extends Application {

	private VendeurAgent vendeurAgent;
	ObservableList<String> observableListAcheteur;
	AgentContainer agentContainer;
	public static void main(String[] args) {
		launch(args);
	}
	
	private void startContainer() throws Exception {
		Runtime runetime = Runtime.instance();
		ProfileImpl profileImpl = new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		agentContainer = runetime.createAgentContainer(profileImpl);
		agentContainer.start();
	}
	@Override
	public void start(Stage window) throws Exception {
		// TODO Auto-generated method stub
		startContainer();

		window.setTitle("Vendeur");
		HBox hBox =new HBox(20);
		Label label = new Label("Agent Name");
		TextField textFieldAgentName = new TextField();
		Button butonDepoly = new Button("deploy");		
		hBox.getChildren().addAll(label,textFieldAgentName,butonDepoly);
		BorderPane mainPane = new BorderPane();
		Scene mainScene = new Scene(mainPane);
		VBox vBox =new VBox(20);
		observableListAcheteur = FXCollections.observableArrayList();
		ListView<String> listViewAcheteur = new ListView<String>(observableListAcheteur);
		vBox.getChildren().addAll(hBox ,listViewAcheteur);
		mainPane.setCenter(vBox);
		window.setScene(mainScene);
		butonDepoly.setOnAction(e->{
			AgentController agentController;
			try {
				agentController = agentContainer.createNewAgent(textFieldAgentName.getText(), "Agents.VendeurAgent", new Object[] {this});
				agentController.start();
			} catch (Exception exp) {
				// TODO Auto-generated catch block
				exp.printStackTrace();
			}
			
		});
		window.show();
		
		
	}
	
	
	public void logMessage(ACLMessage message)
	{
		Platform.runLater(()->{
			observableListAcheteur.add(message.getContent() + ", " + message.getSender());
		});
		
	}

	public void setVendeurAgent(VendeurAgent vendeurAgent) {
		this.vendeurAgent = vendeurAgent;
	}

}
