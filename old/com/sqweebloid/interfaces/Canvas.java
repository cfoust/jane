package com.sqweebloid.interfaces;

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.accessibility.Accessible;

import com.sqweebloid.base.Initializer;

public class Canvas extends java.awt.Canvas implements MouseMotionListener,
  MouseListener, Accessible {
	static final long serialVersionUID = 1L;
	private static final BufferedImage image = new BufferedImage(764, 503,
	   BufferedImage.TYPE_INT_ARGB);
	private static Canvas instance = null;
	private static int x = 0;
	private static int y = 0;
	public static boolean inputMouse = true;
	public static boolean inputKeyboard = true;
	public static HashMap<String,String> data = new HashMap<>();
	public static Initializer caller = null;
	static boolean callerSet = false;
	public static void setCaller(Initializer is) {
		caller = is;
		callerSet = true;
	}
	public Canvas() {
	  instance = this;
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
	  // TODO Auto-generated method stub
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	  // TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	  // TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
	  // TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
	  // TODO Auto-generated method stub
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
	  // TODO Auto-generated method stub
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
	@Override
	public Graphics getGraphics() {
	  Graphics g = image.getGraphics();
	  if (callerSet) {
		  caller.draw(g);
	  }
	  super.getGraphics().drawImage(image, 0, 0, null);
	  return g;
	}
	
	// sending AWT events
	public void sendEvent(AWTEvent awtevent) {
	  if (awtevent instanceof MouseEvent) {
	   if (awtevent.getID() == 505) {
	        x = -1;
	        y = -1;
	   } else {
	        x = ((MouseEvent) awtevent).getX();
	        y = ((MouseEvent) awtevent).getY();
	   }
	  }
	  super.processEvent(awtevent);
	}
	@Override
	public void processEvent(AWTEvent awtevent) {
	  if (awtevent instanceof MouseEvent) {
	   if (awtevent.getID() == 505) {
	        x = -1;
	        y = -1;
	   } else {
	        x = ((MouseEvent) awtevent).getX();
	        y = ((MouseEvent) awtevent).getY();
	   }
	   if(!inputMouse) {
	        return;
	   }
	  } else if(awtevent instanceof KeyEvent) {
	   if(!inputKeyboard) {
	        return;
	   }
	  }
	  super.processEvent(awtevent);
	}
	public static int getMouseX() {
	  return x;
	}
	public static int getMouseY() {
	  return y;
	}
	
	public static Canvas get() {
	  return instance;
	}
}