package com.mygdx.game.client.view.spritesheet;

import java.util.List;

/**
 * 
 * @author elimonent
 *
 * represents metadata for a spritesheet
 */
public class SpritesheetMetadata {
	
	/**
	 * list of frametags
	 * a frame tag describes a sequence of frames which form an animation
	 * a frame tag has a human-readable name
	 */
	List<FrameTagMetadata> frameTags;
	
	@Override
	public String toString() {
		String toReturn = "";
		toReturn += "frameTags: " + frameTags;
		
		return toReturn;
	}
}
