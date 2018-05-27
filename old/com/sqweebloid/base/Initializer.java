package com.sqweebloid.base;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.jar.JarFile;

import javax.annotation.processing.Processor;
import javax.swing.JButton;
import javax.swing.JFrame;
import com.sqweebloid.interfaces.*;
import com.sqweebloid.utils.Mouse;
import com.sqweebloid.utils.Updater;

public class Initializer extends JFrame implements AppletStub, KeyListener {

    public static final String PAGE_ADDRESS = "http://oldschool1.runescape.com/";
    private static final long serialVersionUID = -3450697819043722786L;
    private static final HashMap<String, String> parameters = new HashMap<String, String>();
    private static URL pageAddressUrl;
    static Applet game;
    public static void log(String s) { System.out.println(s); }
    
    
    public static void main(String[] args) throws Exception {
    	
	    System.out.println("Jane Initializing...");
		pageAddressUrl = new URL(PAGE_ADDRESS);
		String pageSource = readPageSource(PAGE_ADDRESS);
		parseParameters(pageSource);
		String gamepackFileName = findGamepackFileName(pageSource);
		
		long startms = new Date().getTime();
		log("Downloading client jar..");
		URL website = new URL(PAGE_ADDRESS + gamepackFileName);
	    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
	    FileOutputStream fos = new FileOutputStream("client-dl.jar");
	    fos.getChannel().transferFrom(rbc, 0, 1 << 24);
	    long dltime = new Date().getTime()-startms;
	    log("Successfully downloaded gamepack.jar in " + dltime + "ms");
	    
	    log("Starting updater.");
	    
	    JarFile cljar = new JarFile(new File("client-dl.jar"));
	    Updater clUpdate = new Updater(cljar);
	    
//	    log("Starting client.");
//		URLClassLoader classLoader = new URLClassLoader(new URL[] { new URL("file:cl-injected.jar") });
//	    //URLClassLoader classLoader = new URLClassLoader(new URL[] { new URL(PAGE_ADDRESS + gamepackFileName) });
//		game = (Applet) classLoader.loadClass("client").newInstance();
//		
//		final Initializer rs2Loader = new Initializer();
//		rs2Loader.setLayout(null);
//		game.setStub(rs2Loader);
//		game.init();
//		game.start();
//		game.setLocation(0, 0);
//		game.setSize(773, 500);
//		rs2Loader.add(game);
//		rs2Loader.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		rs2Loader.setSize(773, 580);
//		rs2Loader.setTitle("Old School RuneScape Client Loader");
//		rs2Loader.setResizable(false);
//		rs2Loader.setVisible(true);
//		
//		Canvas.setCaller(rs2Loader);
//		
//		
//		
//		JButton button = new JButton("Dump");
//		button.addActionListener(new ActionListener() {
//		      public void actionPerformed(ActionEvent evt) {
//		    	  MouseEvent simulPress = new MouseEvent(Mouse.FAKE_SOURCE, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 495, 317, 1, false,1);
//		    	  MouseListener listener = (MouseListener) rs2Loader.getClient().getListener();
//		    	  listener.mouseClicked(simulPress);
//		      }
//	     });
//		button.setSize(100,50);
//		button.setLocation(0, 500);
//		button.setFont(new Font("Serif", Font.PLAIN, 20));
//		rs2Loader.add(button);
		
    }
    public final synchronized void processEvent(AWTEvent e) {
        if (e instanceof MouseEvent) {
            MouseEvent event = (MouseEvent) e;
            if (event.getSource().equals(Mouse.FAKE_SOURCE))
                event.setSource(this);
        }
        super.processEvent(e);
    }
    private Client getClient() {
    	return ((com.sqweebloid.interfaces.Client) game);
    }
    private static String readPageSource(String pageAddress) throws IOException {
		String source = "";
		Scanner scanner = new Scanner(new URL(pageAddress).openStream());
		while (scanner.hasNextLine()) {
		    source += scanner.nextLine() + "\n";
		}
		scanner.close();
		return source;
    }
    Player[] players;
    public void updatePlayers() {
    	players = getClient().getPlayers();
    	int num = 0;
    	for (Player player : players) {if (player != null) num++;}
    	Player[] tempPlayers = new Player[num];
    	int pit = 0;
    	for (Player player : players) {if (player != null){tempPlayers[pit] = player; pit++;}}
    	players = new Player[num];
    	System.arraycopy(tempPlayers, 0, players, 0, num);
    }
    long time = System.currentTimeMillis();
    public void draw(Graphics g) {
    	time = System.currentTimeMillis() - time;
    	if (time > 5000) {
    		updatePlayers();
    	}
    	for (int i = 0; i<players.length; i++) {
    		if (players[i] != null)
    			g.drawString(players[i].getName(), 50, 100+(20*i));
    	}
    }

    private static void parseParameters(String pageSource) {
		String[] lines = pageSource.split("\n");
		String paramNameBeginning = "param name=";
		String valueBeginning = "value=";
		for (String line : lines) {
		    if (!line.contains(paramNameBeginning)) {
			continue;
		    }
		    int start = line.indexOf(paramNameBeginning)
			    + paramNameBeginning.length() + 1;
		    int end = line.indexOf('"', start);
		    String name = line.substring(start, end);
		    start = line.indexOf(valueBeginning) + valueBeginning.length() + 1;
		    end = line.indexOf('"', start);
		    String value = line.substring(start, end);
		    parameters.put(name, value);
		}
    }

    private static String findGamepackFileName(String pageSource) {
		String gamepackFileNameStart = "archive=";
		int gamepackFileNameStartIndex = pageSource
			.indexOf(gamepackFileNameStart);
		return pageSource.substring(gamepackFileNameStartIndex
			+ gamepackFileNameStart.length(),
			pageSource.indexOf('\'', gamepackFileNameStartIndex));
    }

    public URL getDocumentBase() {
    	return pageAddressUrl;
    }

    public URL getCodeBase() {
    	return pageAddressUrl;
    }

    public String getParameter(String name) {
    	return parameters.get(name);
    }

    public AppletContext getAppletContext() {
    	return null;
    }

    public void appletResize(int width, int height) {

    }
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_F1) {
			System.out.println("F1 Pressed");
		}
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}