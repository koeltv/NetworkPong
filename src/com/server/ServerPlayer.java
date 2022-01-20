package com.server;

import com.Player;

import java.net.InetAddress;

public class ServerPlayer extends Player {
	final InetAddress address;

	int port;

	ServerPlayer(InetAddress address, int port) {
		super();
		this.address = address;
		this.port = port;
	}

	public boolean isAddressIdentical(InetAddress inetAddress) {
		return address.equals(inetAddress);
	}
}
