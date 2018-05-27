package com.sqweebloid.analysers;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.sqweebloid.utils.Updater;

public class PlayerAnalyser extends AbstractAnalyser {

	public PlayerAnalyser(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canRun(ClassNode node) {
		if(node.superName.equals(instance.getClassName("Entity"))){
			int intType = 0;
			ListIterator<FieldNode> fnIt = node.fields.listIterator();
			while (fnIt.hasNext()) {
				FieldNode fn = fnIt.next();
				if (fn.desc.equals("I"))
					intType++;
			}
			return intType == 20;
        }
		return false;
	}

	@Override
	public void analyse(ClassNode node) {
		
		ListIterator<FieldNode> fnIt = node.fields.listIterator();
		int num = 0;
		while (fnIt.hasNext()) {
			FieldNode fn = fnIt.next();
			if (fn.desc.equals("Ljava/lang/String;")) {
				node.interfaces.add("com/sqweebloid/interfaces/Player");
				{
					MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getName", "()Ljava/lang/String;", null, null);
					getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
					getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, fn.name, fn.desc));
			        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
			        int size = getter.instructions.size();
			        getter.visitMaxs(size, size);
			        getter.visitEnd();
			        node.methods.add(getter);
			        instance.logInject(node.name, "getName", fn.desc, node.name + "." + fn.name);
				}
			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Player";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return "Entity";
	}

}
