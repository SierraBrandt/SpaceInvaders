package spaceinvaders;

import java.io. * ;
import java.awt. * ;
import java.awt.geom. * ;
import java.awt.event. * ;
import javax.swing. * ;

public class SpaceInvaders extends JFrame implements Runnable {
	static final int WINDOW_WIDTH = 500;
	static final int WINDOW_HEIGHT = 700;
	final int XBORDER = 20;
	final int YBORDER = 30;
	final int YTITLE = 25;
	boolean animateFirstTime = true;
	int xsize = -1;
	int ysize = -1;
	Image image;
	Graphics2D g;

	int cannonXPos;
	int cannonYPos;
	int ballY;
        int cannonBallNum=25;
        
	int cannonBallXPos[] = new int[cannonBallNum];
	int cannonBallYPos[] = new int[cannonBallNum];
        boolean cannonBallActive[] = new boolean[cannonBallNum];
        
        int invaderNum=10;
	int invaderXPos[] = new int[invaderNum];
	int invaderYPos[] = new int[invaderNum];
	boolean invaderActive[]= new boolean[invaderNum];
        boolean gameOver;
	int currentCannonBallIndex;
        
        int scoreCount;
        int highScore;
        
        boolean gameWin;

	static SpaceInvaders frame;
	public static void main(String[] args) {
		frame = new SpaceInvaders();
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public SpaceInvaders() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.BUTTON1 == e.getButton()) {
					//left button

					// location of the cursor.
					int xpos = e.getX();
					int ypos = e.getY();
					cannonBallActive[currentCannonBallIndex] = true;
					cannonBallXPos[currentCannonBallIndex] = cannonXPos - XBORDER;
					cannonBallYPos[currentCannonBallIndex] = cannonYPos;

					currentCannonBallIndex++;

					if (currentCannonBallIndex >= cannonBallXPos.length) {
						currentCannonBallIndex = 0;
					}

				}
				if (e.BUTTON3 == e.getButton()) {
					//right button
					reset();
				}
				repaint();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				repaint();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {

				cannonXPos = e.getX() - XBORDER;

				repaint();
			}
		});

		addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.VK_UP == e.getKeyCode()) {} else if (e.VK_DOWN == e.getKeyCode()) {} else if (e.VK_LEFT == e.getKeyCode()) {} else if (e.VK_RIGHT == e.getKeyCode()) {}
				repaint();
			}
		});
		init();
		start();
	}
	Thread relaxer;
	////////////////////////////////////////////////////////////////////////////
	public void init() {
		requestFocus();
	}
	////////////////////////////////////////////////////////////////////////////
	public void destroy() {}

	////////////////////////////////////////////////////////////////////////////
	public void paint(Graphics gOld) {
		if (image == null || xsize != getSize().width || ysize != getSize().height) {
			xsize = getSize().width;
			ysize = getSize().height;
			image = createImage(xsize, ysize);
			g = (Graphics2D) image.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		//fill background
		g.setColor(Color.cyan);
		g.fillRect(0, 0, xsize, ysize);

		int x[] = {getX(0),getX(getWidth2()),getX(getWidth2()),getX(0),getX(0)
		};
		int y[] = {getY(0),getY(0),getY(getHeight2()),getY(getHeight2()),getY(0)
		};
		//fill border
		g.setColor(Color.white);
		g.fillPolygon(x, y, 4);
		// draw border
		g.setColor(Color.red);
		g.drawPolyline(x, y, 5);

		if (animateFirstTime) {
			gOld.drawImage(image, 0, 0, null);
			return;
		}

		Image img1 = Toolkit.getDefaultToolkit().getImage("cdn4.iconfinder.com/data/icons/space-32/541/space_41-512.png");
		g.drawImage(img1, 10, 10, this);
		g.finalize();
                
		for (int i = 0; i < cannonBallXPos.length; i++) {
			if (cannonBallActive[i]) {
				drawCannonBall(getX(cannonBallXPos[i]), getYNormal(cannonBallYPos[i]), 0, 0.5, 0.5);
                        
			
		}
        }
                
		drawCannon(getX(cannonXPos), getYNormal(cannonYPos), 0, 1, 1);

		g.setColor(Color.BLACK);
		for (int i = 0; i < invaderXPos.length; i++) {
                    if (invaderActive[i]){
			drawInvader(getX(invaderXPos[i]), getYNormal(invaderYPos[i]), 0, 0.5, 0.5);
                    }
                        
		}
                g.setFont(new Font("Gadugi", Font.PLAIN, 15));
                g.drawString("Your score: "+scoreCount, 50, 50);
                
                 g.setFont(new Font("Gadugi", Font.PLAIN, 15));
                g.drawString("High Score: " + highScore, 300, 50);
                
                if(gameOver){
                 g.setFont(new Font("Century Gothic", Font.PLAIN, 50));
                 g.drawString("Game Over", 150, 350);
                 
                 
                }
                
                if(gameWin){
                 g.setFont(new Font("Century Gothic", Font.PLAIN, 50));
                 g.drawString("You Win!", 150, 350);
                 
                }
       

                    
                
                
		gOld.drawImage(image, 0, 0, null);

	}

	////////////////////////////////////////////////////////////////////////////
	public void drawCannon(int xpos, int ypos, double rot, double xscale, double yscale) {
		g.translate(xpos, ypos);
		g.rotate(rot * Math.PI / 180.0);
		g.scale(xscale, yscale);
		Color starYellow = new Color(235, 207, 70);
		g.setColor(starYellow);
		int xval[] = {0,10,40,10,0,-10,	-40,-10,0
		};
		int yval[] = { - 40,-10,0,10,40,10,0,-10,-40
		};
		g.fillPolygon(xval, yval, xval.length);

		g.scale(1.0 / xscale, 1.0 / yscale);
		g.rotate( - rot * Math.PI / 180.0);
		g.translate( - xpos, -ypos);
	}
	public void drawCannonBall(int xpos, int ypos, double rot, double xscale, double yscale) {
		g.translate(xpos, ypos);
		g.rotate(rot * Math.PI / 180.0);
		g.scale(xscale, yscale);

		Color aliengreen = new Color(40, 128, 19);
		g.setColor(aliengreen);
		g.fillOval(10, 10, 50, 30);
		Color alienwhite = new Color(235, 239, 240);
		g.setColor(alienwhite);
		g.fillOval(8, 8, 13, 13);
		g.fillOval(48, 8, 13, 13);
		Color alienblack = new Color(21, 25, 26);
		g.setColor(alienblack);
		g.drawArc(30, 20, 10, 10, -180, 180);
		g.fillOval(10, 10, 8, 8);
		g.fillOval(51, 10, 8, 8);
		g.setColor(alienwhite);
		g.fillOval(9, 10, 5, 5);
		g.fillOval(50, 10, 5, 5);

		g.scale(1.0 / xscale, 1.0 / yscale);
		g.rotate( - rot * Math.PI / 180.0);
		g.translate( - xpos, -ypos);
	}
	public void drawInvader(int xpos, int ypos, double rot, double xscale, double yscale) {
		g.translate(xpos, ypos);
		g.rotate(rot * Math.PI / 180.0);
		g.scale(xscale, yscale);

		g.fillOval( - 10, -38, 20, 20);

		int xval[] = {10,20,25,10,10,15,10,0,-10,-15,-10,-10,-25,-20,-10,-10
		};
		int yval[] = { - 20,-30,-25,-10,20,35,40,20,40,35,20,-10,-25,-30,-20,-20
		};
		g.fillPolygon(xval, yval, xval.length);

		g.scale(1.0 / xscale, 1.0 / yscale);
		g.rotate( - rot * Math.PI / 180.0);
		g.translate( - xpos, -ypos);
	}

	////////////////////////////////////////////////////////////////////////////
	// needed for     implement runnable
	public void run() {
		while (true) {
			animate();
			repaint();
			double seconds = 0.02; //time that 1 frame takes.
			int miliseconds = (int)(1000.0 * seconds);
			try {
				Thread.sleep(miliseconds);
			} catch(InterruptedException e) {}
		}
	}
	/////////////////////////////////////////////////////////////////////////
	public void reset() {
		cannonXPos = getWidth2() / 2;
		cannonYPos = 0;
                gameOver = false;
		for (int i = 0; i < cannonBallXPos.length; i++) {
			cannonBallActive[i] = false;
		}

		for (int i = 0; i < invaderXPos.length; i++) {
			invaderXPos[i] = (int)(Math.random() * getWidth2());
			invaderYPos[i] = (int)(Math.random() * getHeight2() / 2 + getHeight() / 2);
                        invaderActive[i]=true;
		}
		currentCannonBallIndex = 0;
                scoreCount=0;
                gameWin = false;
	}
	/////////////////////////////////////////////////////////////////////////
	public void animate() {
		if (animateFirstTime) {
			animateFirstTime = false;
			if (xsize != getSize().width || ysize != getSize().height) {
				xsize = getSize().width;
				ysize = getSize().height;
			}

			reset();
		}
                
                if(gameOver){
                    return;
                }
                
                for (int i = 0; i < cannonBallXPos.length; i++) {
                    
                    for (int j = 0; j < invaderXPos.length; j++) {
                       
                    if(cannonBallActive[i]){
                        
                        if(invaderActive[j]){
                            
                            if(cannonBallXPos[i] < invaderXPos[j]+ 7 && cannonBallXPos[i] >  invaderXPos[j]-30 && cannonBallYPos[i] < invaderYPos[j]+20 && cannonBallYPos[i] > invaderYPos[j]-20){
                            invaderActive[j]=false;
                            scoreCount++;
                                if(scoreCount >= highScore){
                                    highScore++;
                                }
                            }

                        }
                    }  
                }
                    
            }
		for (int i = 0; i < cannonBallXPos.length; i++) {
			if (cannonBallActive[i]) {
			cannonBallYPos[i] += 15;

			}
		}
                
                for (int j = 0; j < invaderXPos.length; j++) {
                    
                    invaderYPos[j]+=-0.02;
                    if(invaderYPos[j]<5){
                       gameOver=true;
                    }
                   
        
                }
                
                if(scoreCount>=invaderXPos.length){
                    gameWin=true;
                }
	}

	////////////////////////////////////////////////////////////////////////////
	public void start() {
		if (relaxer == null) {
			relaxer = new Thread(this);
			relaxer.start();
		}
	}
	////////////////////////////////////////////////////////////////////////////
	public void stop() {
		if (relaxer.isAlive()) {
			relaxer.stop();
		}
		relaxer = null;
	}
	/////////////////////////////////////////////////////////////////////////
	public int getX(int x) {
		return (x + XBORDER);
	}

	public int getY(int y) {
		return (y + YBORDER + YTITLE);
	}

	public int getYNormal(int y) {
		return ( - y + YBORDER + YTITLE + getHeight2());
	}

	public int getWidth2() {
		return (xsize - getX(0) - XBORDER);
	}

	public int getHeight2() {
		return (ysize - getY(0) - YBORDER);
	}
}