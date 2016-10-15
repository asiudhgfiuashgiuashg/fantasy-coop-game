package com.mygdx.game.shared.exceptions;

public class MapLoaderException extends Exception {
	private static final String DEFAULT_MESSAGE = "Invalid entity name.";
	
	public MapLoaderException() {
		this(DEFAULT_MESSAGE);
	}

	public MapLoaderException(String message) {
		super(message);
	}
	
}
