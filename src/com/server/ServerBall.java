package com.server;

import com.Ball;

/**
 * The type Server ball.
 */
public class ServerBall extends Ball {
	/**
	 * The X speed.
	 */
	int xSpeed = 7;

	/**
	 * The Y speed.
	 */
	int ySpeed = 6;

	/**
	 * The X speed coefficient.
	 */
	private int xSpeedCoefficient = 1;

	/**
	 * The Y speed coefficient.
	 */
	private int ySpeedCoefficient = 1;

	/**
	 * Move the ball 1 time.
	 */
	public void move() {
		if(xSpeed < 0) x += xSpeed - xSpeedCoefficient;
		if(xSpeed >= 0) x += xSpeed + xSpeedCoefficient;
		if(ySpeed < 0) y += ySpeed - (Math.random() * (4 + 1)) - ySpeedCoefficient;
		if(ySpeed >= 0) y += ySpeed + (Math.random() *(4 + 1)) + ySpeedCoefficient;

		xSpeedCoefficient *= 1.002;
		ySpeedCoefficient *= 1.002;
	}

	/**
	 * Reset the ball to its original position.
	 */
	public void reset() {
		xSpeedCoefficient = 1;
		ySpeedCoefficient = 1;
		x = 700;
		y = 355;
		xSpeed = -xSpeed;
	}
}
