package com.sqweebloid.analysers;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.sqweebloid.utils.Updater;

public class EntityAnalyser extends AbstractAnalyser {

	public EntityAnalyser(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canRun(ClassNode node) {
		int intType = 0;
		ListIterator<FieldNode> fnIt = node.fields.listIterator();
		while (fnIt.hasNext()) {
			FieldNode fn = fnIt.next();
			if (fn.desc.equals("I"))
				intType++;
		}
		return intType == 53;
	}

	@Override
	public void analyse(ClassNode node) {
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Entity";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return null;
	}

}
