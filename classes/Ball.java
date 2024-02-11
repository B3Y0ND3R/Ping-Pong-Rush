package gameV;

import java.awt.*;
import java.util.*;

public class Ball extends Rectangle{

	Random random;
	int xVelocity;
	int yVelocity;
	int initialSpeed = 2;
	private Difficulty difficulty;
	
	Ball(int x, int y, int width, int height, Difficulty difficulty) {
	    super(x, y, width, height);
	    this.difficulty = difficulty; 
	    random = new Random();
	    int randomXDirection = random.nextInt(2);
	    if (randomXDirection == 0)
	        randomXDirection--;
	    setXDirection(randomXDirection * 2 * difficulty.getSpeedMultiplier());

	    int randomYDirection = random.nextInt(2);
	    if (randomYDirection == 0)
	        randomYDirection--;
	    setYDirection(randomYDirection * 2 * difficulty.getSpeedMultiplier());
	}

	
	public void setXDirection(int randomXDirection) {
		xVelocity = randomXDirection;
	}
	public void setYDirection(int randomYDirection) {
		yVelocity = randomYDirection;
	}
	public void move() {
	    x += xVelocity;
	    y += yVelocity;

	    int maxBallSpeed = difficulty.getMaxBallSpeed();

	    if (Math.abs(xVelocity) > maxBallSpeed) {
	        xVelocity = maxBallSpeed * (xVelocity > 0 ? 1 : -1);
	    }
	    if (Math.abs(yVelocity) > maxBallSpeed) {
	        yVelocity = maxBallSpeed * (yVelocity > 0 ? 1 : -1);
	    }
	}

	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, height, width);
	}
}