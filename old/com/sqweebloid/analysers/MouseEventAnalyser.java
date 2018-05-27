package com.sqweebloid.analysers;

import java.util.Iterator;
import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.sqweebloid.utils.Updater;

public class MouseEventAnalyser extends AbstractAnalyser {

	public MouseEventAnalyser(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canRun(ClassNode node) {
		ListIterator<String> fnIt = node.interfaces.listIterator();
		int mouse = 0;
		while (fnIt.hasNext()) {
			String inode = fnIt.next();
			if (inode.equals("java/awt/event/MouseListener")||inode.equals("java/awt/event/MouseMotionListener")||inode.equals("java/awt/event/FocusListener")) {
				mouse++;
			}
		}
		return (mouse == 3);
	}

	@Override
	public void analyse(ClassNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Mouse";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return null;
	}

}
