package gameV;
import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle{

	int id;
	int yVelocity;
	int speed = 10;
	int PADDLE_HEIGHT;
	
	Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id){
		super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
		this.id=id;
		this.PADDLE_HEIGHT=PADDLE_HEIGHT;
	}
	
	public void keyPressed(KeyEvent e) {
		switch(id) {
		case 1:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirection(-speed);
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirection(speed);
			}
			break;
		case 2:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirection(-speed);
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirection(speed);
			}
			break;
		}
	}
	public void keyReleased(KeyEvent e) {
		switch(id) {
		case 1:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirection(0);
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirection(0);
			}
			break;
		case 2:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirection(0);
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirection(0);
			}
			break;
		}
	}
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}
	public void move() {
		y= y + yVelocity;
	}
	
    int targetY; 
    int aiSpeed = 6; 

    public void moveAI(int ballY) {
        if (id == 2) {
            if (y + PADDLE_HEIGHT / 2 < ballY) {
                y += aiSpeed;
            } else if (y + PADDLE_HEIGHT / 2 > ballY) {
                y -= aiSpeed;
            }
            if (y <= 0) {
                y = 0;
            } else if (y >= GamePanel.GAME_HEIGHT - PADDLE_HEIGHT) {
                y = GamePanel.GAME_HEIGHT - PADDLE_HEIGHT;
            }
        }
    }
    
	public void draw(Graphics g) {
		if(id==1)
			g.setColor(Color.blue);
		else
			g.setColor(Color.yellow);
		g.fillRect(x, y, width, height);
	}
}