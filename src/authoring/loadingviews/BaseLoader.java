package authoring.loadingviews;

import java.io.File;
import java.util.Map;

import org.codehaus.groovy.util.SingleKeyHashMap.Entry;

import authoring.Toolbar;
import authoring.GUI_Heirarchy.GUIGridPaneSuper;
import gameData.ManipData;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import resources.keys.AuthRes;

/**
 * @author Elizabeth Shulman
 * @author Jennifer Chin
 * Extends GUIGridPaneSuper. This class is a GUIBuilder object because it has its own
 * Scene, and it extends GUIGridPaneSuper because it uses a gridpane as the root of that
 * Scene. 
 */
public abstract class BaseLoader extends GUIGridPaneSuper {

	private Stage myStage;
	private ManipData data;
	
	/**
	 * Constructor that takes in a Stage in order to change the scene of the stage to 
	 * the GameChooserScreen
	 * @param stage
	 */
	public BaseLoader(Stage stage){
		//uses stage to switch scene once game is chosen
		myStage = stage;
		data = new ManipData();
	}

	/**
	 * Builds the basic view for the GameChooser screen, factoring out all the common elements
	 * between the Load Game for Play view and the Load Game for Edit view
	 * @param gridpane
	 */
	public Pane addCoreFinishingElements(GridPane gridpane) {
		double chooserWidth = AuthRes.getInt("EnvironmentX") - (AuthRes.getInt("Margin") * 10);
		double chooserHeight = AuthRes.getInt("EnvironmentY") - (AuthRes.getInt("Margin") * 10);
		VBox vbox = new VBox();
		vbox.setPrefWidth(chooserWidth);
		vbox.setPrefHeight(chooserHeight);
		vbox.getStyleClass().add("chooser-back");
		gridpane.add(vbox, 20, 13);
		//testLoad(vbox);
		File folder = new File("games");
		//System.out.println(games.listFiles());
//		for (File f: games.listFiles()){
//			System.out.println(f.getPath());
//		}
		File[] games = folder.listFiles();
		for (File game: games){
			//System.out.println(game.getPath());
			if (! game.getPath().equals("games/.DS_Store")){
				//System.out.println(game.getPath());
				String filePath = game.getName() + "/" + game.getName() + "config";
				//System.out.println(game.getPath());
//				for (File f: game.listFiles()){
//					System.out.println(f.getPath());
//				}
				Map<String, String> configMap = data.openConfig(filePath);
				for (String key: configMap.keySet()){
					System.out.println("key: " + key + " value: " + configMap.get(key));
				}
				String thumbPath = configMap.get(AuthRes.getStringKeys("key1"));
			}

		}
		buildThumbnails(vbox);
		return new Toolbar(myStage).integrateToolbar(gridpane);
	}
	
	/**
	 * Adds a title specific to the type of view (e.g. "Load Game for Edit" vs. "Load Game
	 * for Play"
	 * @param gridpane	current pane set as root in scene
	 * @param type		which specific GameChooser view is needed; determines end of title string
	 */
	public void addTitle(GridPane gridpane, String type) {
		try {
			Text title = new Text(AuthRes.getString("ChooserTitle") + AuthRes.getString(type));
			title.getStyleClass().add("title2");
			gridpane.add(title, 20, 10);
		} catch (NullPointerException e) {
			Alert noType = new Alert(AlertType.ERROR);
			noType.setContentText(AuthRes.getString("NoChooserType"));
			noType.showAndWait();
		}
	}
	
	public abstract void buildThumbnails(VBox vb);
	
	
	//TEST LOADING
	//class also needs to load saved games to be edited/played - each game needs thumbnail
	/**
	 * Loads a thumbnail for a game. Creates a button out of the thumbnail so that when
	 * pressed, user can play that game
	 * @param vbox
	 */
	//test loader
//	public void testLoad(VBox vbox) {
//		Text mtncap = new Text("   Mountain ~vIbes~");
//		mtncap.getStyleClass().add("game-chooser");
//		vbox.getChildren().addAll(
//				ButtonFactory.makeButton(null,new ImageView(new Image(AuthRes.getString("mtnthumb"))), 
//						e -> new PlayerMain().start(myStage), "button-nav"),
//				mtncap);
//	}
}
