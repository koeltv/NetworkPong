package com.server;

import com.Ball;

public class ServerBall extends Ball {
	int xSpeed = 7, ySpeed = 6;
	private int xSpeedCoefficient = 1, ySpeedCoefficient = 1;

	public void move() {
		if(xSpeed < 0) x += xSpeed - xSpeedCoefficient;
		if(xSpeed >= 0) x += xSpeed + xSpeedCoefficient;
		if(ySpeed < 0) y += ySpeed - (Math.random() * (4 + 1)) - ySpeedCoefficient;
		if(ySpeed >= 0) y += ySpeed + (Math.random() *(4 + 1)) + ySpeedCoefficient;

		xSpeedCoefficient *= 1.002;
		ySpeedCoefficient *= 1.002;
	}

	public void reset() {
		xSpeedCoefficient = 1;
		ySpeedCoefficient = 1;
		x = 700;
		y = 355;
		xSpeed = -xSpeed;
	}
}
