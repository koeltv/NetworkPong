package com.server;

import com.Player;

import java.net.InetAddress;

/**
 * The type Server player.
 */
public class ServerPlayer extends Player {
	/**
	 * The address of the corresponding user.
	 */
	final InetAddress address;

	/**
	 * The port used by the corresponding user.
	 */
	int port;

	/**
	 * Instantiates a new Server player.
	 *
	 * @param address the address
	 * @param port    the port
	 */
	ServerPlayer(InetAddress address, int port) {
		super();
		this.address = address;
		this.port = port;
	}

	/**
	 * Is address identical.
	 *
	 * @param inetAddress the address to compare
	 * @return whether both address are identical
	 */
	public boolean isAddressIdentical(InetAddress inetAddress) {
		return address.equals(inetAddress);
	}
}
