package game_engine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author benhubsch
 * 
 *         This class is simply a convenient data structure to store the Entity objects in a given
 *         level.
 */
public class Level {

	private List<Entity> myEntities = new ArrayList<>();
//	private String myName;
//	private String bgImage;
//	private String musicPath;
//	private double width;
//	private double height;
//	private boolean isInf;
//	private String thumbPath;

	
	/**
	 * Gets the List<Entity> object containing all Entities with only these Components.
	 *
	 * @param args the args
	 * @return List<Entity>
	 */
	public List<Entity> getEntitiesContaining(List<Class<? extends Component<?>>> args) {
		return myEntities.stream().filter(e -> e.hasAll(args)).collect(Collectors.toList());
	}
	
	/**
	 * @param entities
	 * @param args
	 * @return
	 */
	public List<Entity> getEntitiesContaining(List<Entity> entities, List<Class<? extends Component<?>>> args) {
		return entities.stream().filter(e -> e.hasAll(args)).collect(Collectors.toList());
	}

	/**
	 * Gets the List<Entity> object containing all Entities with these Components.
	 *
	 * @param args the args
	 * @return List<Entity>
	 */
	public List<Entity> getEntitiesContainingAny(List<Class<? extends Component<?>>> args) {
		return myEntities.stream().filter(e -> e.hasAny(args)).collect(Collectors.toList());
	}
	

	/**
	 * @param entities
	 * @param args
	 * @return
	 */
	public List<Entity> getEntitiesContainingAny(List<Entity> entities, List<Class<? extends Component<?>>> args) {
		return entities.stream().filter(e -> e.hasAny(args)).collect(Collectors.toList());
	}

	/**
	 * Adds the entity to the backend. This is used during the various instantiation phases.
	 *
	 * @param e the e
	 */
	public void addEntity(Entity e) {
		myEntities.add(e);
	}

	/**
	 * Adds the entity to the backend. This is used during the various instantiation phases.
	 *
	 * @param entities the entities
	 */
	public void addEntities(List<Entity> entities) {
		for (Entity e : entities) {
			myEntities.add(e);
		}
	}
	
	/**
	 * Adds the entity to the backend. This is used during the various instantiation phases.
	 *
	 * @param e the e
	 */
	public void removeEntity(Entity e) {
		myEntities.remove(e);
	}
	
	public List<Entity> getEntities() {
		return myEntities;
	}

//	public void setThumb(String filepath) {
//		this.thumbPath = filepath;
//	}
//
//	public String getThumb() {
//		return this.thumbPath;
//	}
//
//	public void setName(String name) {
//		this.myName = name;
//	}
//
//	public String getName() {
//		return this.myName;
//	}
//
//	public void setBG(String filepath) {
//		this.bgImage = filepath;
//	}
//
//	public String getBG() {
//		return this.bgImage;
//	}
//
//	public void setMusic(String filepath) {
//		this.musicPath = filepath;
//	}
//
//	public String getMusic() {
//		return this.musicPath;
//	}
//
//	public void setWidth(double width) {
//		this.width = width;
//	}
//
//	public double getWidth() {
//		return this.width;
//	}
//
//	public void setHeight(double height) {
//		this.height = height;
//	}
//
//	public double getHeight() {
//		return this.height;
//	}
//
//	public boolean isInfinite() {
//		return isInf;
//	}
//
//	public void setInf(boolean boolin) {
//		this.isInf = boolin;
//	}

}
