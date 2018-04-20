package game_engine.event.conditions;

import java.math.BigDecimal;


import com.udojava.evalex.Expression;

import game_engine.Component;
import game_engine.Entity;
import game_engine.event.Condition;

public class DataCondition implements Condition {
	private Entity myEntity;
	private Class<? extends Component<?>> myComponentClass;
	private String myComparison;
	private String myExpected;
	
//	private Component myComponent;
	
	public DataCondition(Entity entity, Class<? extends Component<?>> componentClass, String comparison, String expected) {
		myEntity = entity;
		myComponentClass = componentClass;
		myComparison = comparison;
		myExpected = expected;
	}
//	
//	public DataCondition(Component component, String comparison, String expected) {
//		myComponent = component;
//		myComparison = comparison;
//		myExpected = expected;
//	}
	
	@Override
	public boolean evaluate() {
		String expression;
//		if(myzEntity == null && myComponent != null) {
//			zexpression = myComponent.getValue() + myComparison + myExpected; 
//		}z
//		else {
			expression = myEntity.getComponent(myComponentClass).getValue() + myComparison + myExpected; 
//		}
		BigDecimal result =  new Expression(expression).eval();
		return result.equals(BigDecimal.ONE);
	}

}