package game_engine.components.position;

import game_engine.Component;
import game_engine.event.conditions.DataConditionable;

/**
 * Specifies the current x coordinate of an entity (based on JavaFX grid)
 * @author Kevin Deng, Ben Hubsch, Andy Nguyen, Jeremy Chen
 *
 */
@DataConditionable
public class XPosComponent extends Component<Double> {
	
	public XPosComponent(String arg) {
		super(Double.parseDouble(arg));
	}
	
}
