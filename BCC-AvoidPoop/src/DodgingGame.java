import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DodgingGame extends JPanel implements ActionListener {
	private int playerX = 200;
	private int playerY = 400;
	private List<Point> drops = new ArrayList<>();
	//private int dropY = 0;
	private int score = 0;
	private boolean gameOver = false;
	private Timer timer;
	private int dropSpeed = 10;
	private int dropCount = 1;
	private Timer dropTimer;
	private Image poop;
	private Image peo;
	 
	public DodgingGame() {
		setFocusable(true);
		setPreferredSize(new Dimension(400, 500));
        addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 0) {
                    playerX -= 10;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < 380) {
                    playerX += 10;
                }
        	 }
        });
        try {
            poop = Toolkit.getDefaultToolkit().getImage(new File("poop.png").getAbsolutePath());
            peo = Toolkit.getDefaultToolkit().getImage(new File("player.png").getAbsolutePath());
        } catch (Exception e) {
            System.err.println("이미지를 로드할 수 없습니다: " + e.getMessage());
        }
        
        timer = new Timer(100,this);
        timer.start();
        
        dropTimer = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				generateDrop();
			}
		});
        dropTimer.start();
        
        generateDrop();
    }
	 private void generateDrop() {
	        Random rand = new Random();
	        for (int i = 0; i < dropCount; i++) {
	            drops.add(new Point(rand.nextInt(380), 0)); // 새로운 똥 생성
	        }
	        if (score > 0 && score % 5 == 0) { // 점수가 5의 배수일 때 속도 증가 및 똥 개수 증가
	            dropSpeed++;
	            dropCount++; // 떨어지는 똥의 개수 증가
	        }
	    }
	 protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(peo, playerX, playerY, 80,80, this); // 플레이어
	        for (Point drop : drops) {
	        	g.drawImage(poop, drop.x, drop.y, 20, 20, this); // 떨어지는 똥
	        }
	        g.setColor(Color.BLACK);
	        g.drawString("Score: " + score, 10, 20);

	        if (gameOver) {
	            g.setColor(Color.BLACK);
	            g.drawString("Game Over! Score: " + score, 150, 250);
	        }
	    }
	@Override
	public void actionPerformed(ActionEvent e) {
		  if (!gameOver) {
			  for (int i = 0; i < drops.size(); i++) {
	                Point drop = drops.get(i);
	            drop.y += dropSpeed;

	            if (drop.y + 20 >= playerY && drop.y <= playerY + 80 && 
	            	drop.x + 20 >= playerX && drop.x <= playerX + 80) {
	                System.out.println("Collision detected!");
	                System.out.println("Drop: (" + drop.x + ", " + drop.y + ") Player: (" + playerX + ", " + playerY + ")");
	                gameOver = true;
	            }


	            if (drop.y > 500) {
                    score++;
                    drops.remove(i); // 화면 밖으로 나간 똥은 제거
                    i--; // 인덱스 조정
                }
            }

            repaint();
        }
    }
	public static void main(String[] args) {
        JFrame frame = new JFrame("Dodging Game");
        DodgingGame game = new DodgingGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
	
}