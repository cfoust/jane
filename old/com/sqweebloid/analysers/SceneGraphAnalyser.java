package com.sqweebloid.analysers;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.sqweebloid.utils.Updater;

public class SceneGraphAnalyser extends AbstractAnalyser {

	public SceneGraphAnalyser(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canRun(ClassNode node) {
		ListIterator<FieldNode> fnIt = node.fields.listIterator();
		boolean foundTileArray = false, foundSpawnRequest = false;
		while (fnIt.hasNext()) {
			FieldNode fn = fnIt.next();
			if (fn.desc.equals("[[[L" + instance.getClassName("SceneTile") + ";")&&fn.access==0) {
				foundTileArray = true;
			} else if (fn.desc.equals("[L" + instance.getClassName("SceneSpawnRequest") + ";")&&fn.access==0) {
				foundSpawnRequest = true;
			}
		}
		return (foundTileArray && foundSpawnRequest);
	}

	@Override
	public void analyse(ClassNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SceneGraph";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return "SceneTile";
	}

}
