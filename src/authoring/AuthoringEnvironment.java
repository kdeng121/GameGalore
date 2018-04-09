package authoring;

import authoring.right_components.BasePane;
import authoring.right_components.EntityComponent.EntityPane;
import authoring.right_components.EventPane;
import authoring.right_components.LevelPane;
import authoring.right_components.StoryBoardPane;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import observables.Listener;
import resources.keys.AuthRes;

/**
 * @author Liam Pulsifer
 * @author Jenny Chin
 * @author Elizabeth Schulman
 */

public class AuthoringEnvironment extends GUIBuilder implements Listener {

	private NavigationPane np;
	
	private BasePane base;
	private EntityPane entity;
	private EventPane event;
	private LevelPane level;
	private StoryBoardPane story;
	private BorderPane bp;
	private Canvas canvas;
	
	
	public AuthoringEnvironment(){
		//instantiate leftPane, rightPane, Canvas
			
		base = new BasePane();
		entity = new EntityPane();
		event = new EventPane();
		level = new LevelPane();
		story = new StoryBoardPane();
		canvas = new Canvas(AuthRes.getInt("canvassize"));
		EntityController controller = new EntityController(entity, canvas);
		canvas.setController(controller);
		entity.setController(controller);
		np = new NavigationPane();
		np.addListener(this);
	}
	
	@Override
	public Scene display() {
		bp = new BorderPane();
		Scene scene = initScene(bp);
		
		//set leftPane
		
		update(""); //calls default setting for right pane
		bp.setLeft(np);
		bp.setCenter(canvas.getView());
		//bp.setTop(new Rectangle(1200, 50, Color.GRAY));
		//Scene scene = new Scene(bp, AuthRes.getInt("EnvironmentX"), AuthRes.getInt("EnvironmentY"));
		scene.getStylesheets().add(getClass().getResource("vooga.css").toString());
		//Scene scene = new Scene(bp, AuthRes.getInt("EnvironmentX"), AuthRes.getInt("EnvironmentY"));
		return scene;
		
	}
//	
//	private void dragAndDrop(){
//		
//	}

	@Override
	public void update(String state) { //more concise/less repetitive way to write this?
		switch(state) {
			case "Entity Creator":
			        bp.setRight(entity.getView());
			        break;
			case "Actions and Events":
			        bp.setRight(event.getView());
			        break;
			case "Level Preferences": ;
			        bp.setRight(level.getView());
			        break;
			case "Storyboard": ;
			        bp.setRight(story.getView());
			        break;
			default: 
					bp.setRight(base.getView());
					break;
		}
	}

}
