package authoring.controllers;

import java.util.ArrayList;

import game_engine.Level;


public class LevelController {

	ArrayList<Level> currentLevels;			
	
	public LevelController() {
		currentLevels = new ArrayList<Level>();
		//manipData = new ManipData();
	}
	
	public void addLevel(Level l) {
		currentLevels.add(l);
	}
	
	public void saveGame() {
		//manipData.saveData(currentLevels) //or .saveData(currentLevels, currentAttributes)
	}
}