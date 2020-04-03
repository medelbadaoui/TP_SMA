package mainContainer;

import java.util.Observable;

import Agents.ConsumerAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
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
import javafx.stage.Stage;

public class ConsumerContainer extends Application {
	private ConsumerAgent consumerAgent;
	ObservableList<String> observableListMessage;
public static void main(String[] args) throws Exception {
	launch(args);
}
void demmarerContainer() throws Exception {
	Runtime runetime =Runtime.instance();
	ProfileImpl profileImpl = new ProfileImpl();
	profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
	AgentContainer container = runetime.createAgentContainer(profileImpl);
	AgentController agentController= container.createNewAgent("Consumer", "Agents.ConsumerAgent", new Object[] {this});
	agentController.start();
		
	
	
	container.start();
}
@Override
public void start(Stage window) throws Exception {
	demmarerContainer();
	window.setTitle("Consumer");
	HBox hBox =new HBox(20);
	observableListMessage = FXCollections.observableArrayList();
	Label label= new Label("Livres :  ");
	TextField textField = new TextField();
	Button button =new Button("Acheter");
	button.setOnAction(e->{
		String livre = textField.getText();
		GuiEvent guiEvent =new GuiEvent(consumerAgent, 1);
		guiEvent.addParameter(livre);
		
		consumerAgent.onGuiEvent(guiEvent);
		//observableListMessage.add(livre);
	});
	hBox.getChildren().addAll(label,textField,button);
	
	ListView<String> listViewMessage = new ListView<String>(observableListMessage);
	
	
	BorderPane mainPane= new BorderPane();
	mainPane.setTop(hBox);
	mainPane.setCenter(listViewMessage);
	Scene myScene = new Scene(mainPane,600,400);
	window.setScene(myScene);
	window.show();
	
}
public void logMessage(ACLMessage aclMessage) {
	// TODO Auto-generated method stub
	Platform.runLater(()->{
		observableListMessage.add(aclMessage.getContent() + ", " + aclMessage.getSender());
	});

}
public ConsumerAgent getConsumerAgent() {
	return consumerAgent;
}
public void setConsumerAgent(ConsumerAgent consumerAgent) {
	this.consumerAgent = consumerAgent;
}
}
