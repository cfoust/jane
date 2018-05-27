package com.sqweebloid.analysers;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.sqweebloid.utils.Updater;

public class CanvasAnalyser extends AbstractAnalyser {

	public CanvasAnalyser(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canRun(ClassNode node) {
		if (!node.superName.equals("java/awt/Canvas"))
			  return false;
		return true;
	}

	@Override
	public void analyse(ClassNode node) {
		node.superName = "com/sqweebloid/interfaces/Canvas";
		ListIterator<?> mli = node.methods.listIterator();
		while (mli.hasNext())
		{
		  MethodNode mn = (MethodNode) mli.next();
		  if (mn.name.equals("<init>"))
		  {
		    ListIterator<?> ili = mn.instructions.iterator();
		    while (ili.hasNext())
		    {
		      AbstractInsnNode ain = (AbstractInsnNode) ili.next();
		      if (ain.getOpcode() == Opcodes.INVOKESPECIAL)
		      {
		        MethodInsnNode min = (MethodInsnNode) ain;
		        min.owner = "com/sqweebloid/interfaces/Canvas";
		        break;
		      }
		    }
		  }
		}
		instance.log("-->Superclassed canvas to com/sqweebloid/interfaces/Canvas");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Canvas";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return null;
	}

}
