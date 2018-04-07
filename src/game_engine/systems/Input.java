package game_engine.systems;

import java.util.Arrays;

import java.util.List;

import game_engine.Component;
import game_engine.Engine;
import game_engine.Entity;
import game_engine.GameSystem;
import game_engine.Vector;
import game_engine.components.Physics;
import game_engine.components.Position;

public class Input extends GameSystem {
	
	private static final Class<? extends Component> PHYSICS = Physics.class;
	private static final Class<? extends Component> POSITION = Position.class;

	public Input(Engine engine) {
		super(engine);
	}

	@Override
	public void act(double elapsedTime) {
		List<Class<? extends Component>> args = Arrays.asList(PHYSICS, POSITION);
		for (Entity entity : getEngine().getEntitiesContaining(args)) {
			for (Vector vector : getEngine().getInput()) {
				Physics physics = (Physics) entity.getComponent(PHYSICS);
				Position position = (Position) entity.getComponent(POSITION);
				double delta = horizontal(vector, physics);
				position.setX(position.getX() + delta);
				
				handleJump(vector, position, physics);
			}
		}
	}
	
	private double horizontal(Vector vector, Physics physics) {
		double delta = 0;
		if (vector.getX() > 0) {
			delta = physics.getXVel();
		} else if (vector.getX() < 0){
			delta = physics.getXVel();
		}
		return delta;
	}
	
//	also need to manage "jump" on up vector...are threads the right call here without 
//	a physics engine? Hard to tell until we can test with Game Player
	private void handleJump(Vector vector, Position position, Physics physics) {
		
	}
}
