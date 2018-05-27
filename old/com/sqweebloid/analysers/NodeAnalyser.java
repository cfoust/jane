package com.sqweebloid.analysers;
import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.sqweebloid.utils.Updater;


public class NodeAnalyser extends AbstractAnalyser {

	public NodeAnalyser(Updater i) {
		super(i);
	}

	@Override
	public boolean canRun(ClassNode node) {
		int ownType = 0, longType = 0;
		ListIterator<FieldNode> fnIt = node.fields.listIterator();
		while (fnIt.hasNext()) {
			FieldNode fn = fnIt.next();
			if ((fn.access & Opcodes.ACC_STATIC) == 0) {
				if (fn.desc.equals(String.format("L%s;",node.name)))
					ownType++;
				if (fn.desc.equals("J"))
					longType++;
			}
		}
		return ownType == 2 && longType == 1;
	}

	@Override
	public void analyse(ClassNode node) {
		node.interfaces.add("com/sqweebloid/interfaces/Node");
		ListIterator<FieldNode> fnIt = node.fields.listIterator();
		while (fnIt.hasNext()) {
			FieldNode fn = fnIt.next();
			if (fn.desc.equals("J")&&(fn.access == Opcodes.ACC_PUBLIC)) {
				MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getID", "()J", null, null);
				getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
				getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, fn.name, fn.desc));
		        getter.instructions.add(new InsnNode(Opcodes.LRETURN));
		        int size = getter.instructions.size();
		        getter.visitMaxs(size, size);
		        getter.visitEnd();
		        node.methods.add(getter);
		        instance.logInject(node.name, "getID", fn.desc, node.name + "." + fn.name);
			}
			if (fn.desc.equals("L" + node.name + ";")&&(fn.access == Opcodes.ACC_PUBLIC)) {
				MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getPrevious", "()Lcom/sqweebloid/interfaces/Node;", null, null);
				getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
				getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, fn.name, fn.desc));
		        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
		        int size = getter.instructions.size();
		        getter.visitMaxs(size, size);
		        getter.visitEnd();
		        node.methods.add(getter);
		        instance.logInject(node.name, "getPrevious", fn.desc, node.name + "." + fn.name);
			}
			if (fn.desc.equals("L" + node.name + ";")&&(fn.access != Opcodes.ACC_PUBLIC)) {
				MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getNext", "()Lcom/sqweebloid/interfaces/Node;", null, null);
				getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
				getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, fn.name, fn.desc));
		        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
		        int size = getter.instructions.size();
		        getter.visitMaxs(size, size);
		        getter.visitEnd();
		        node.methods.add(getter);
		        instance.logInject(node.name, "getNext", fn.desc, node.name + "." + fn.name);
			}
		}
	}

	@Override
	public String getName() {
		return "Node";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return null;
	}

}
