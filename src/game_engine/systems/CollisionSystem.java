package game_engine.systems;

import game_engine.Engine;
import game_engine.Entity;
import game_engine.GameSystem;
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
	 *  Helper method that gets extrema of a sprite (min/max x & y coordinates), used for creating
	 *  an AABB, among other applications, will return in the form [min_x, max_x, min_y, max_y]
	 * @param e
	 * @return
	 */
	protected double[] getExtrema(Entity e){
		PositionComponent p = (PositionComponent) e.getComponent(PositionComponent.class);
		SpriteComponent s = (SpriteComponent) e.getComponent(SpriteComponent.class);

		double angle = Math.toRadians(s.getAngle());
		double width = s.getWidth();
		double height = s.getHeight();
		double centerX = p.getX();
		double centerY = p.getY();

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
		return new double[]{Collections.min(xCoords), Collections.max(xCoords), Collections.min(yCoords), Collections.max(yCoords)};
	}
}
