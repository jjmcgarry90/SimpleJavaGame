import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


public class PongPanel extends JPanel implements ActionListener, KeyListener {
	private int paddle1X, paddle1Y, paddle2X, paddle2Y;
	private double ballX, ballY;
	private double moveBallX, moveBallY;
	private final int PADDLE_WIDTH = 20, PADDLE_HEIGHT=100, BALL_WIDTH = 21,
					  BALL_HEIGHT = 20;
	private final int BOARD_WIDTH=1000, BOARD_HEIGHT=600;
	private final int DELAY = 1;
	private Timer timer;
	private boolean paddle1MovingUp, paddle1MovingDown;
	private int player1score;
	private int TOLERANCE = 5;
	private int milliseconds;
	private Screen screen;
	private Mode mode;
	private Font font;
	private boolean firing;
	private ArrayList<Bullet> bullets;
	private UFO ufo;
	
	public PongPanel() {
		setOpaque(false);
		setPreferredSize(new Dimension(BOARD_WIDTH,BOARD_HEIGHT));
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(this);
		paddle1X = 0;
		paddle1Y = BOARD_HEIGHT/2 - PADDLE_HEIGHT/2;
		paddle2X = BOARD_WIDTH - PADDLE_WIDTH;
		paddle2Y = BOARD_HEIGHT/2 - PADDLE_HEIGHT/2;
		ballX = BOARD_WIDTH/2;
		ballY = BOARD_HEIGHT/2;
		moveBallX=1;
		moveBallY=1;
		timer = new Timer(DELAY, this);
		timer.start();
		font = new Font("Arial", Font.PLAIN, 20);
		bullets= new ArrayList<Bullet>();
		ufo = new UFO (200,200);
		
	}
	
	public void paintComponent (Graphics page) {
		super.paintComponents(page);
		page.setColor(Color.red);
		page.setFont(font);
		page.drawString(Double.toString(milliseconds/1000.0), 50, 50);
		page.setColor(Color.white);
		page.drawLine(BOARD_WIDTH/2, 0, BOARD_WIDTH/2, BOARD_HEIGHT);
		page.fillRect(paddle1X, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
		page.fillRect((int)ballX, (int)ballY, BALL_WIDTH, BALL_HEIGHT);
		
		for (Bullet bullet : bullets) {
				int x = bullet.getX();
				int y = bullet.getY();
				page.drawLine(x, y, x+3, y);
				page.drawLine(x, y+1, x+3, y+1);
				bullet.setX(x+1);
		}
		
		page.drawString(Integer.toString(bullets.size()),100,100);
		
		page.drawImage(ufo.getImage(), 100, 100, 105, 102, null);
		//if mode is 2 player??
		//page.fillRect(paddle2X, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);
	}

	//updates game state and then repaints
	public void actionPerformed(ActionEvent e) {
		milliseconds+=DELAY;
		
		if (milliseconds%100 == 0){
			if(moveBallX > 0)
				moveBallX+=.005;
			else
				moveBallX-=.005;
			if(moveBallY > 0)
				moveBallY+=.005;
			else
				moveBallY-=.005;
		} 
		//collision checking	
		if(ballX <= paddle1X + PADDLE_WIDTH && ballX >= paddle1X + PADDLE_WIDTH - TOLERANCE)
				if(ballY >= paddle1Y-BALL_HEIGHT && ballY <= paddle1Y + PADDLE_HEIGHT) {
					ballX = paddle1X + PADDLE_WIDTH + 1;  //get ball out of collision zone
					moveBallX *= -1;	
				}
		//end collision detection
		
		//bounds checking		
		if(ballX >= (BOARD_WIDTH-BALL_WIDTH)) {
			ballX = BOARD_WIDTH-BALL_WIDTH - 1; 
			moveBallX *= -1;
		}
		if (ballX <= 0-BALL_WIDTH-Math.abs(moveBallX)) {
			ballX = BOARD_WIDTH/2 - BALL_WIDTH/2 - 1;
			ballY = BOARD_HEIGHT/2 - BALL_HEIGHT/2;
			moveBallX *= -1;
			//timer.stop();
			repaint();
		}
		
		if(ballY >= (BOARD_HEIGHT-BALL_HEIGHT)) {
			ballY = BOARD_HEIGHT - BALL_HEIGHT - 1;
			moveBallY *= -1;
		}
			
		if(ballY <= 0) {
			ballY = 1;
			moveBallY *= -1;
		}
		//end bounds checking
		
		//update ball location
		ballX += moveBallX;
		ballY += moveBallY;
		
		if(paddle1MovingUp)
			paddle1Y-=2;
		if(paddle1MovingDown)
			paddle1Y+=2;
		
		if(firing) {
			bullets.add(new Bullet(0,((int)(Math.random()*BOARD_HEIGHT))));
		}
		
		repaint();

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
	//	if(e.getKeyCode()==KeyEvent.VK_1) {}
		//	screen = Screen.MENU;
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
			timer.start();
		if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE)
			timer.stop();
		if('w' == e.getKeyChar())
			paddle1MovingUp=true;
		if('s' == e.getKeyChar())
			paddle1MovingDown=true;
		if(e.getKeyCode()== KeyEvent.VK_2)
			firing=true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if('w' == e.getKeyChar())
			paddle1MovingUp=false;
		if('s' == e.getKeyChar())
			paddle1MovingDown=false;
		if(e.getKeyCode()== KeyEvent.VK_2)
			firing=false;
	} 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Pong");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.green);
		PongPanel mainPanel = new PongPanel();
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setVisible(true);
	}
}
