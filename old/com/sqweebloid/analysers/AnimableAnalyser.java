package com.sqweebloid.analysers;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.sqweebloid.utils.Updater;

public class AnimableAnalyser extends AbstractAnalyser {

	public AnimableAnalyser(Updater i) {
		super(i);
	}

	@Override
	public boolean canRun(ClassNode node) {
		if (node.superName.equals(instance.getClassName("SubNode"))) {
		    int numInts = 0, numBytes = 0;
	        for (FieldNode fn : (List<FieldNode>) node.fields) {
	            if (fn.desc.equals("I")) {
	                numInts++;
	            } else if (fn.desc.equals("B")) {
	            	numBytes++;
	            }
	        }
	        return numInts == 2 && numBytes == 0;
		}
		return false;
	}

	@Override
	public void analyse(ClassNode node) {
	}

	@Override
	public String getName() {
		return "Animable";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return "SubNode";
	}

}
