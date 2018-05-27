package com.sqweebloid.interfaces;

public interface Client {
	public com.sqweebloid.interfaces.Player[] getPlayers();
	public com.sqweebloid.interfaces.Player getLocalPlayer();
	public com.sqweebloid.interfaces.NPC[] getNPCs();
	public java.util.EventListener getListener();
	public int[][][] getTileOffsets();
}
