package superheroShowdownV2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class SuperheroShowdownV2 extends Canvas implements Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Thread thread;
	private static final int WIDTH = 640;
	private static final int HEIGHT = WIDTH/4 *3;
	
	public enum STATE
	{
		Menu, //Main Menu
		Help, //Help Menu
		CharacterSelection, //Character Selection Screen
		StageSelection, //Stage Selection Screen
		Game, //Playing the game
		Pause, //Pausing the game (cheating)
		GameOver //The Game Is Over Screen (regardless of who wins), Show Stats
	}
	private static STATE state=STATE.Menu;
	private static Window window;
	private static Handler handler = new Handler();
	private static KeyboardControl control = new KeyboardControl();
	private static MouseControl mControl = new MouseControl();
	private boolean running = false;

	public SuperheroShowdownV2()
	{
		window=new Window(WIDTH,HEIGHT);
		window.getFrame().add(this);
		String file="C:\\Users\\Shannon\\Documents\\Workspace\\Projects\\src\\superheroShowdownV2\\Ironman Moving Right.png";
		window.setVisible(true);
		this.start();
		drawSuperhero(file);
	}
	public static void main(String[] args)
	{
		new SuperheroShowdownV2();
	}

	public static void drawSuperhero(String file)
	{
		ImageIcon img=new ImageIcon(file);
		Image superHero= img.getImage();
		JFrame frame=window.getFrame();
		Graphics g=frame.getGraphics();
		g.drawImage(superHero, 0, 0, null);
	}

	public void start()
	{
		thread = new Thread(this,"SuperheroShowdownV2");
		thread.start();
		running = true;
	}

	public void stop()
	{
		if(!running) return;
		running = false;
	}

	@Override
	public void run()
	{
		double target=60.0;
		double nsPerTick = 1000000000.0 /target;
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double unprocessed = 0.0;
		int fps = 0;
		int tps = 0;
		boolean canRender=false;
		
		while(running)
		{
			long now = System.nanoTime();
			unprocessed += (now-lastTime)/nsPerTick;
			lastTime=now;
			
			if(unprocessed >= 1.0)
			{
				tick();
				unprocessed--;
				tps++;
				canRender=true;
			}
			else
				canRender=false;
			try
			{
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			if(canRender)
			{
				render();
				fps++;
			}
			if(System.currentTimeMillis()-1000 > timer)
			{
				timer+=1000;
				fps=0;
				tps=0;
			}
		}
		System.exit(0);
	}

	private void tick()
	{
		handler.tick();	
	}

	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		//handler.render(g);

		g.dispose();
		bs.show();
	}

	public static STATE getState()
	{
		return state;
	}
	public static void setState(String s)
	{
		if(s.equals("Menu"))
			state=STATE.Menu;
	}
	public static Handler getHandler() {
		return handler;
	}

	public static Window getWindow(){
		return window;
	}
}