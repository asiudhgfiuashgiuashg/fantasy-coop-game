package com.mygdx.game.server.model;

/**
 * Represents a condition such as poisoned, slowed, on fire
 * The act() method of a given effect will be where the effect actually does
 *  what it is supposed to do. For example, a poison effect may remove health
 *  from its victim every time act() is called.
 * @author elimonent
 *
 */
public abstract class Effect implements Actable {


}
