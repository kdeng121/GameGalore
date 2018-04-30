package game_player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game_engine.Component;
import game_engine.Engine;
import game_engine.Entity;
import game_engine.components.PrimeComponent;
import game_engine.components.position.XPosComponent;
import game_engine.components.position.YPosComponent;
import game_engine.components.sprite.FilenameComponent;
import game_engine.components.sprite.HeightComponent;
import game_engine.components.sprite.SpritePolarityComponent;
import game_engine.components.sprite.WidthComponent;
import game_engine.components.sprite.ZHeightComponent;
import game_engine.level.Level;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 * 
 * @author Dana Park Class that handles animations and updating of animations in game
 *
 */
public class PlayerView {

	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private static final double DOUBLE_RATE = 1.05;
	private static final double HALF_RATE = 0.93;

	private Timeline animation;
	private PulldownFactory pullDownFactory;
	private Engine myEngine;
	private Map<Entity, Map<String, ImageView>> spriteMap;
	private Group root;
	private ViewManager viewManager;
	private SubScene subScene;
	private ParallelCamera cam;
	private DataManager dataManager;
	private boolean notSet;

	private Entity primary;

	/**
	 * @param pdf
	 * @param engine
	 * @param view constructor for PlayerView
	 *
	 */
	public PlayerView(PulldownFactory pdf, ViewManager view, DataManager dtm) {
		pullDownFactory = pdf;
		viewManager = view;
		dataManager = dtm;
		notSet = true;
	}

	public void setEngine(Engine e) {
		this.myEngine = e;
	}

	/**
	 * method that instantiates the scene with the camera for the game with all necessary sprites
	 *
	 */
	public void instantiate() {
		Scene scene = viewManager.getScene();
		subScene = viewManager.getSubScene();
		root = viewManager.getSubRoot();
		scene.setOnKeyPressed(e -> {
			myEngine.receiveInput(e);
		});
		scene.setOnKeyReleased(myEngine::receiveInput);
		scene.setOnKeyPressed(myEngine::receiveInput);
//		scene.setOnMouseClicked(myEngine::receiveInput);
		scene.setOnMousePressed(e -> calcTranslation(e));
		cam = new ParallelCamera();
		subScene.setCamera(cam);
		Level level = myEngine.getLevel();

		spriteMap = new HashMap<>();
		List<Entity> spriteEntities = level.getEntitiesContaining(
				Arrays.asList(FilenameComponent.class, HeightComponent.class, WidthComponent.class));
		for (Entity e : spriteEntities) {
<<<<<<< HEAD
			ImageView imageView = getImageView(e);
=======
			String imageName = e.getComponent(FilenameComponent.class).getValue();
			Double height = e.getComponent(HeightComponent.class).getValue();
			Double width = e.getComponent(WidthComponent.class).getValue();
			Image image = new Image(imageName);
			ImageView imageView = new ImageView(image);
			imageView.setFitWidth(width);
			imageView.setFitHeight(height);
			spriteMap.put(imageName, imageView);
>>>>>>> 75b38619c7b070687ab53b35519ef751d7cbcaa1
			root.getChildren().add(imageView);
		}

		animationFrame();
	}

	private void calcTranslation(MouseEvent e) {
		double xPosClick = e.getX();
		double yPosClick = e.getY();
		primary = myEngine.getLevel().getEntitiesContaining(Arrays.asList(PrimeComponent.class)).get(0);
		setGamePlayerOnce();
		double xPosPrim = primary.getComponent(XPosComponent.class).getValue();
		double yPosPrim = primary.getComponent(YPosComponent.class).getValue();
		
		System.out.println("Click -- x: " + xPosClick + " y: " + yPosClick);
		System.out.println("Prim -- x: " + xPosPrim + " y: " + yPosPrim);
		
	}

	/**
	 * @param vm method that sets viewManager as the param
	 */
	public void setViewManager(ViewManager vm) {
		viewManager = vm;
	}

	private void animationFrame() {
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	private void step(double delay) {
		//animation.stop();
		myEngine.update(delay);
		render();
		//handleUI();
	}

	private void render() {
		root.getChildren().clear();
		
		primary = myEngine.getLevel().getEntitiesContaining(Arrays.asList(PrimeComponent.class)).get(0);
		setGamePlayerOnce();
		Double xPos = primary.getComponent(XPosComponent.class).getValue();
		Double yPos = primary.getComponent(YPosComponent.class).getValue();
		cam.relocate(xPos - ViewManager.SUBSCENE_WIDTH / 2, yPos - ViewManager.SUBSCENE_HEIGHT / 2);
		
		myEngine.getLevel().getEntities().stream().filter(entity -> isInView(entity, xPos, yPos)).sorted(this::compareZ).forEach(this::display);
	}

	private int compareZ(Entity a, Entity b) {
		return a.getComponent(ZHeightComponent.class).getValue()
				.compareTo(b.getComponent(ZHeightComponent.class).getValue());
	}
	
	private void setGamePlayerOnce() {
		if(notSet) {
			notSet = false;
			dataManager.setGamePlayer(primary);
		}
	}

	private ImageView getImageView(Entity entity) {
		String filename = entity.getComponent(FilenameComponent.class).getValue();
<<<<<<< HEAD
		if (!spriteMap.containsKey(entity)) {
			Map<String, ImageView> imageMap = new HashMap<>();
			spriteMap.put(entity, imageMap);
		}
		
		if (!spriteMap.get(entity).containsKey(filename)) {
			ImageView imageView = new ImageView(filename);
			imageView.setOnMousePressed(event -> clickInput(imageView));
			spriteMap.get(entity).put(filename, imageView);
		}
		
		ImageView imageView = spriteMap.get(entity).get(filename);
		return imageView;
=======
		if (!spriteMap.containsKey(filename)) {
			spriteMap.put(filename, new ImageView(filename));
		}
		return spriteMap.get(filename);
>>>>>>> 75b38619c7b070687ab53b35519ef751d7cbcaa1
	}

	private void display(Entity entity) {
		Double xPos = entity.getComponent(XPosComponent.class).getValue();
		Double yPos = entity.getComponent(YPosComponent.class).getValue();
		Double width = entity.getComponent(WidthComponent.class).getValue();
		Double height = entity.getComponent(HeightComponent.class).getValue();

		ImageView imageView = getImageView(entity);
		imageView.setX(xPos - width / 2);
		imageView.setY(yPos - height / 2);

		Component<Integer> polarity = entity.getComponent(SpritePolarityComponent.class);
		// changes which direction the imageview faces based off of movement direction of entity
		if (polarity != null) {
			imageView.setScaleX(Math.signum(polarity.getValue()));
		}
		root.getChildren().add(imageView);
	}


	private boolean isInView(Entity entity, double centerX, double centerY) {
		double xPos = entity.getComponent(XPosComponent.class).getValue();
		double yPos = entity.getComponent(YPosComponent.class).getValue();
		double height = entity.getComponent(HeightComponent.class).getValue();
		double width = entity.getComponent(WidthComponent.class).getValue();

		double minX = xPos - width / 2;
		double maxX = xPos + width / 2;
		double minY = yPos - height / 2;
		double maxY = yPos + height / 2;

		return checkCorner(minX, minY, centerX, centerY) || checkCorner(minX, maxY, centerX, centerY)
				|| checkCorner(maxX, minY, centerX, centerY) || checkCorner(maxX, maxY, centerX, centerY);
	}

	private boolean checkCorner(double entityX, double entityY, double centerX, double centerY) {
		double sceneMinX = centerX - ViewManager.SUBSCENE_WIDTH / 2;
		double sceneMaxX = centerX + ViewManager.SUBSCENE_WIDTH / 2;
		double sceneMinY = centerY - ViewManager.SUBSCENE_HEIGHT / 2;
		double sceneMaxY = centerY + ViewManager.SUBSCENE_HEIGHT / 2;
		return ((sceneMinX <= entityX && entityX <= sceneMaxX) && (sceneMinY <= entityY && entityY <= sceneMaxY));
	}

	/**
	 * method that handles reactions when buttons are pressed on Menu. Ex: When Play button is pressed,
	 * the method will make the game play
	 *
	 */
	public void handleUI() {
		String selectedAction = pullDownFactory.getSpeedBox().getSelectionModel().getSelectedItem();
		String statusAction = pullDownFactory.getStatusBox().getSelectionModel().getSelectedItem();

		if (selectedAction.equals("Speed Up")) {

			animation.setRate(animation.getRate() * DOUBLE_RATE);
		}
		if (selectedAction.equals("Slow Down")) {
			animation.setRate(animation.getRate() * HALF_RATE);
		}
		if (statusAction.equals("Pause Game")) {
			animation.stop();
		}
		if (statusAction.equals("Play Game")) {
			animation.play();
		}
	}

}
