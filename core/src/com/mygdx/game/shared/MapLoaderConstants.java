package com.mygdx.game.shared;

/**
 * String contants used in .tmx files and server and client map loaders.
 * 
 * @author Sawyer Harris
 *
 */
public class MapLoaderConstants {
	public static final String STATIC_ENTITIES_LAYER_NAME = "Static Entities";
	public static final String DYNAMIC_ENTITIES_LAYER_NAME = "Dynamic Entities";
	public static final String TRIGGERS_LAYER_NAME = "Triggers";
	
	public static final String CUTSCENE_TRIGGER_TYPE = "Cutscene";
	public static final String MAPLOAD_TRIGGER_TYPE = "MapLoad";
	
	public static final String BASE_PACKAGE = "com.mygdx.game.server.model";
	
	public static final String TRIGGER_PACKAGE = "trigger";
	public static final String ENEMY_PACKAGE = "entity.enemy";
	public static final String FRIENDLY_PACKAGE = "entity.friendly";
	
	public static final String ENEMY_TYPE = "Enemy";
	public static final String FRIENDLY_TYPE = "FriendlyCharacter";
}
