package com.sqweebloid.analysers;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.sqweebloid.utils.Updater;

public class SceneSpawnRequestAnalyser extends AbstractAnalyser {

	public SceneSpawnRequestAnalyser(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canRun(ClassNode node) {
		ListIterator<FieldNode> fnIt = node.fields.listIterator();
		int animable = 0, ints = 0;
		while (fnIt.hasNext()) {
			FieldNode fn = fnIt.next();
			if (fn.desc.equals("L" + instance.getClassName("Animable") + ";")&&fn.access!=Opcodes.ACC_STATIC) {
				animable++;
			} else if (fn.desc.equals("I")&&fn.access==0) {
				ints++;
			}
		}
		return (animable == 1&&ints==12);
	}

	@Override
	public void analyse(ClassNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SceneSpawnRequest";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return "Animable";
	}

}
