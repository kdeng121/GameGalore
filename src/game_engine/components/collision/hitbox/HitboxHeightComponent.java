package game_engine.components.collision.hitbox;

import game_engine.Component;

public class HitboxHeightComponent extends Component<Double>{

	public HitboxHeightComponent(String arg) {
		super(Double.parseDouble(arg));
	}	
}
