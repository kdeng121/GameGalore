package game_player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import game_engine.Component;
import game_engine.Engine;
import game_engine.Entity;
import game_engine.Tuple;
import game_engine.Vector;
import game_engine.components.PrimeComponent;
import game_engine.components.position.XPosComponent;
import game_engine.components.position.YPosComponent;
import game_engine.components.sprite.FilenameComponent;
import game_engine.components.sprite.HeightComponent;
import game_engine.components.sprite.SpritePolarityComponent;
import game_engine.components.sprite.VisibilityComponent;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
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
	private UUID myId;
	private Text score;
	private Text health;
	private Text highScore;
	private Integer scoreData = 0;
	private Integer healthData = 0;
	private int highScoreData = 0;

	private Entity primary;

	/**
	 *
	 */
	public PlayerView() {
		// TODO something
	}

	public void initialize(InstanceStorage storage) {
		pullDownFactory = storage.getPullDownFactory();
		viewManager = storage.getViewManager();
		dataManager = storage.getDataManager();
		notSet = true;
		myId = UUID.randomUUID();
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
		scene.setOnKeyReleased(event -> myEngine.receiveKeyInput(new Tuple<UUID, KeyEvent>(myId, event)));
		scene.setOnKeyPressed(event -> myEngine.receiveKeyInput(new Tuple<UUID, KeyEvent>(myId, event)));
		cam = new ParallelCamera();
		subScene.setCamera(cam);
		Level level = myEngine.getLevel();

		if (!assignId(level)) {
			System.out.println("no one assigned");
			return;
		}

		primary = myEngine.getLevel().getEntitiesContaining(Arrays.asList(PrimeComponent.class)).get(0);
		setGamePlayerOnce();

		spriteMap = new HashMap<>();
		List<Entity> spriteEntities = level.getEntitiesContaining(
				Arrays.asList(FilenameComponent.class, HeightComponent.class, WidthComponent.class));
		for (Entity e : spriteEntities) {
			getImageView(e);
		}

		animationFrame();
	}

	private boolean assignId(Level level) {
		for (Entity entity : level.getEntities()) {
			if (entity.hasAll(Arrays.asList(PrimeComponent.class))
					&& (entity.getComponent(PrimeComponent.class).getValue() == null)) {
				entity.getComponent(PrimeComponent.class).setValue(myId);
				return true;
			}
		}
		return false;
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
		myEngine.update(delay);
		render();
		// scoreData = myEngine.getScore();
		// healthData = myEngine.getHealth();
		// highScoreData = myEngine.getHighScore();
		score = viewManager.createText(5, 15, "Score: " + scoreData);
		health = viewManager.createText(150, 15, "Health: " + healthData);
		highScore = viewManager.createText(150, 40, "HighScore: " + highScoreData);
	}

	private void render() {
		root.getChildren().clear();

		Double xPos = primary.getComponent(XPosComponent.class).getValue();
		Double yPos = primary.getComponent(YPosComponent.class).getValue();
		cam.relocate(xPos - ViewManager.SUBSCENE_WIDTH / 2, yPos - ViewManager.SUBSCENE_HEIGHT / 2);

		myEngine.getLevel().getEntities().stream().filter(entity -> isInView(entity, xPos, yPos)).sorted(this::compareZ)
				.forEach(this::display);
	}

	private int compareZ(Entity a, Entity b) {
		Component<Double> zCompA = a.getComponent(ZHeightComponent.class);
		Component<Double> zCompB = b.getComponent(ZHeightComponent.class);
		Double zHeightA = (zCompA == null) ? 0.0 : zCompA.getValue();
		Double zHeightB = (zCompB == null) ? 0.0 : zCompB.getValue();
		return zHeightA.compareTo(zHeightB);
	}

	private void setGamePlayerOnce() {
		if (notSet) {
			notSet = false;
			dataManager.setGamePlayer(primary);
		}
	}

	private void clickInput(ImageView imageView) {
		double middleX = imageView.getX() + imageView.getFitWidth() / 2;
		double middleY = imageView.getY() + imageView.getFitHeight() / 2;
		Vector click = new Vector(middleX, middleY);
		myEngine.receiveMouseInput(new Tuple<UUID, Vector>(myId, click));
	}

	private ImageView getImageView(Entity entity) {
		String filename = entity.getComponent(FilenameComponent.class).getValue();
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
	}

	private void display(Entity entity) {
		Double xPos = entity.getComponent(XPosComponent.class).getValue();
		Double yPos = entity.getComponent(YPosComponent.class).getValue();
		Double width = entity.getComponent(WidthComponent.class).getValue();
		Double height = entity.getComponent(HeightComponent.class).getValue();
		Boolean visibility = entity.getComponent(VisibilityComponent.class).getValue();

		ImageView imageView = getImageView(entity);
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		imageView.setX(xPos - width / 2);
		imageView.setY(yPos - height / 2);
		imageView.setVisible(visibility);

		Component<Integer> polarity = entity.getComponent(SpritePolarityComponent.class);
		// changes which direction the imageview faces based off of movement direction of entity
		if (polarity != null) {
			imageView.setScaleX(Math.signum(polarity.getValue()));
		}
		root.getChildren().add(imageView);
	}

	private boolean isInView(Entity entity, double centerX, double centerY) {
		return true;
//		calculations broken for some reason
//		Bounds cameraBounds = new BoundingBox(centerX - ViewManager.SUBSCENE_WIDTH / 2, centerY - ViewManager.SUBSCENE_HEIGHT / 2, ViewManager.SUBSCENE_WIDTH, ViewManager.SUBSCENE_HEIGHT);
//		ImageView entityView = getImageView(entity);
//		return cameraBounds.intersects(entityView.getBoundsInParent());
	}

	/**
	 * method that handles reactions when buttons are pressed on Menu. Ex: When Play button is pressed,
	 * the method will make the game play
	 *
	 */
	public void handleUI(int index) {

		if (index == 0) {
			animation.stop();
		}
		if (index == 1) {
			animation.play();
		}
		if (index == 2) {
			animation.setRate(animation.getRate() * HALF_RATE);
		}
		if (index == 3) {
			animation.setRate(animation.getRate() * DOUBLE_RATE);
		}
		if (index == 4) {
			pullDownFactory.handleReplay();
		}
		if (index == 5) {
			pullDownFactory.handleSave();
		}
		if (index == 6) {
			pullDownFactory.aboutGame();
		}
	}

}
