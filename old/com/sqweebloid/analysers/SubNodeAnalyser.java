package com.sqweebloid.analysers;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.sqweebloid.utils.Updater;

public class SubNodeAnalyser extends AbstractAnalyser {

	public SubNodeAnalyser(Updater i) {
		super(i);
	}

	@Override
	public boolean canRun(ClassNode node) {
		if(!node.superName.equals("java/lang/Object")){
			int ownType = 0;
			ListIterator<FieldNode> fnIt = node.fields.listIterator();
			while (fnIt.hasNext()) {
				FieldNode fn = fnIt.next();
				if ((fn.access & Opcodes.ACC_STATIC) == 0) {
					if (fn.desc.equals(String.format("L%s;",node.name)))
						ownType++;
				}
			}
			return ownType == 2;
        }
		return false;
	}

	@Override
	public void analyse(ClassNode node) {
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SubNode";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return null;
	}

}
