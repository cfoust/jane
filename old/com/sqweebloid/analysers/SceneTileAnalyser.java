package com.sqweebloid.analysers;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.sqweebloid.utils.Updater;

public class SceneTileAnalyser extends AbstractAnalyser {

	public SceneTileAnalyser(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canRun(ClassNode node) {
		int own = 0, bools = 0, ssr = 0;
		if (node.superName.equals(instance.getClassName("Node"))) {
			ListIterator<FieldNode> fnIt = node.fields.listIterator();
			while (fnIt.hasNext()) {
				FieldNode fn = fnIt.next();
				if (fn.desc.equals("L" + node.name + ";")) {
					own++;
				} else if (fn.desc.equals("Z")&&fn.access!=Opcodes.ACC_STATIC) {
					bools++;
				} else if (fn.desc.equals("[L" + instance.getClassName("SceneSpawnRequest") + ";")&&fn.access==0) {
					ssr++;
				}
			}
		}
		return (own == 1 && bools == 3 && ssr == 1);
	}

	@Override
	public void analyse(ClassNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SceneTile";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return "SceneSpawnRequest";
	}

}
