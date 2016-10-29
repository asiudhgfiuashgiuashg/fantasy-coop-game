package com.mygdx.game.shared.exceptions;

public class MapLoaderException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = "Invalid entity name or type.";
	
	public MapLoaderException() {
		this(DEFAULT_MESSAGE);
	}

	public MapLoaderException(String message) {
		super(message);
	}

}
