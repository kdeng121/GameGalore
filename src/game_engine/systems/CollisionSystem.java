package game_engine.systems;

import game_engine.Engine;
import game_engine.Entity;
import game_engine.GameSystem;
import game_engine.components.CollidedComponent;
import game_engine.components.HitboxComponent;
import game_engine.components.PositionComponent;
import game_engine.components.SpriteComponent;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Jeremy Chen
 * Interface for Systems that contain logic for collisions
 */
public abstract class CollisionSystem extends GameSystem {

	public CollisionSystem(Engine engine) {
		super(engine);
	}

	/**
	 *
	 * @param e1
	 * @param e2
	 * @return
	 */
	protected abstract boolean intersect(Entity e1, Entity e2);
	
	/**
	 * 
	 * @param e1
	 * @param e2
	 */
	protected void addCollided(Entity e1, Entity e2) {
		addCollidedHelper(e1, e2);
		addCollidedHelper(e2, e1);
	}
	
	/**
	 * 
	 * @param e1
	 * @param e2
	 */
	private void addCollidedHelper(Entity e1, Entity e2) {
		CollidedComponent c1 = (CollidedComponent) e1.getComponent(CollidedComponent.class);
		if(c1!=null) {
			c1.addCollidedWith(e2);
		}
		else {
			c1 = new CollidedComponent();
			c1.addCollidedWith(e2);
			e1.addComponent(c1);
		}
	}
	
	/**
	 *  Helper method that gets extrema of a sprite (min/max x & y coordinates), used for creating
	 *  an AABB, among other applications, will return in the form [min_x, max_x, min_y, max_y]
	 * @param e
	 * @return
	 */
	protected double[] getExtrema(Entity e){
		PositionComponent p = (PositionComponent) e.getComponent(PositionComponent.class);
		HitboxComponent h = (HitboxComponent) e.getComponent(HitboxComponent.class);

		double angle = Math.toRadians(p.getAngle());
		System.out.println(p.getAngle());
		double width = h.getWidth();
		double height = h.getHeight();
		double centerX = p.getX() + h.getXOffset();
		double centerY = p.getY() + h.getYOffset();

		if(p.getAngle()%90 == 0){
			return new double[]{centerX - width/2, centerX + width/2, centerY - height/2, centerY + height/2};
		}

		ArrayList<Double> xCoords = new ArrayList<Double>();
		ArrayList<Double> yCoords = new ArrayList<Double>();

		for(int i = -1; i <=1; i+=2){
			for(int j = -1; j<=1; j+=2){
				double origX = i*width + centerX;
				double origY = j*height + centerY;

				double transformedX = centerX+(origX-centerX)*Math.cos(angle)+(origY-centerY)*Math.sin(angle);
				double transformedY = centerY-(origX-centerX)*Math.sin(angle)+(origY-centerY)*Math.cos(angle);
				xCoords.add(transformedX);
				yCoords.add(transformedY);
			}
		}
//		System.out.println("min x: " + Collections.min(xCoords));
//		System.out.println("max x: " + Collections.max(xCoords));
//		System.out.println("min y: " + Collections.min(yCoords));
//		System.out.println("max y: " + Collections.max(yCoords));
		return new double[]{Collections.min(xCoords), Collections.max(xCoords), Collections.min(yCoords), Collections.max(yCoords)};
	}
}