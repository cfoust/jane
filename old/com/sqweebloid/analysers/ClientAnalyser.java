package com.sqweebloid.analysers;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import com.sqweebloid.utils.Updater;

public class ClientAnalyser extends AbstractAnalyser {

	public ClientAnalyser(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canRun(ClassNode node) {
		if (node.name.equals("client")) {
            return true;
        }
		return false;
	}

	@Override
	public void analyse(ClassNode node) {
		ListIterator<FieldNode> fnIt = node.fields.listIterator();
		node.interfaces.add("com/sqweebloid/interfaces/Client");
		while (fnIt.hasNext()) {
			FieldNode fn = fnIt.next();
			if (fn.desc.equals("[L" + instance.getClassName("Player") + ";")) {
				MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getPlayers", "()[Lcom/sqweebloid/interfaces/Player;", null, null);
				getter.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, node.name, fn.name, fn.desc));
		        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
		        int size = getter.instructions.size();
		        getter.visitMaxs(size, size);
		        getter.visitEnd();
		        node.methods.add(getter);
		        instance.logInject(node.name, "getPlayers", fn.desc, node.name + "." + fn.name);
			}
			if (fn.desc.equals("[L" + instance.getClassName("NPC") + ";")) {
				MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getNPCs", "()[Lcom/sqweebloid/interfaces/NPC;", null, null);
				getter.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, node.name, fn.name, fn.desc));
		        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
		        int size = getter.instructions.size();
		        getter.visitMaxs(size, size);
		        getter.visitEnd();
		        node.methods.add(getter);
		        instance.logInject(node.name, "getNPCs", fn.desc, node.name + "." + fn.name);
			}
		}
		for (ClassNode inode : instance.CLASSES.values()) {
			ListIterator<FieldNode> fnInode = inode.fields.listIterator();
			while (fnInode.hasNext()) {
				FieldNode fnN = fnInode.next();
				if ((fnN.access == Opcodes.ACC_STATIC) && (fnN.desc.equals("L" + instance.getClassName("Player") + ";"))) {
					MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getLocalPlayer", "()Lcom/sqweebloid/interfaces/Player;", null, null);
					getter.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, inode.name, fnN.name, fnN.desc));
			        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
			        int size = getter.instructions.size();
			        getter.visitMaxs(size, size);
			        getter.visitEnd();
			        node.methods.add(getter);
			        instance.logInject(node.name, "getLocalPlayer", fnN.desc, inode.name + "." + fnN.name);
				}
				if ((fnN.access == Opcodes.ACC_STATIC) && (fnN.desc.equals("[[[I"))) {
					System.out.println("field " + inode.name + "." + fnN.name + " is a 3D int array");
				}
				if ((fnN.access == Opcodes.ACC_STATIC) && (fnN.desc.equals("Lcn;"))) {
					System.out.println("field " + inode.name + "." + fnN.name + " is scene");
				}
			}
		}
		ListIterator<FieldNode> fnInode = instance.CLASSES.get(instance.getClassName("Mouse")).fields.listIterator();
		while (fnInode.hasNext()) {
			FieldNode fnN = fnInode.next();
			if ((fnN.access == 9) && (fnN.desc.equals("L" + instance.getClassName("Mouse") + ";"))) {
				MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getListener", "()Ljava/util/EventListener;", null, null);
				getter.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, instance.getClassName("Mouse"), fnN.name, fnN.desc));
		        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
		        int size = getter.instructions.size();
		        getter.visitMaxs(size, size);
		        getter.visitEnd();
		        node.methods.add(getter);
		        instance.logInject(node.name, "getListener", fnN.desc, instance.getClassName("Mouse") + "." + fnN.name);
			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Client";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return null;
	}

}
