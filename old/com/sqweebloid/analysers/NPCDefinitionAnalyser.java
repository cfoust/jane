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

public class NPCDefinitionAnalyser extends AbstractAnalyser {

	public NPCDefinitionAnalyser(Updater i) {
		super(i);
	}

	@Override
	public boolean canRun(ClassNode node) {
		if (node.superName.equals(instance.getClassName("SubNode"))) {
			int strCnt = 0, shrtaCnt = 0, boolCnt = 0;
            for (FieldNode fn : (List<FieldNode>) node.fields) {
            	if (fn.access != Opcodes.ACC_STATIC) {
	                if (fn.desc.contains("Ljava/lang/String;")) {
	                    strCnt++;
	                } else if (fn.desc.equals("[S")) {
	                	shrtaCnt++;
	                } else if (fn.desc.equals("Z")) {
	                	boolCnt++;
	                }
            	}
            }
            return (strCnt == 2)&&(shrtaCnt == 4)&&(boolCnt == 4);
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
				node.interfaces.add("com/sqweebloid/interfaces/NPCDefinition");
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
			        //instance.log("\tINJECT: method " + node.name + ".getName()" + fn.desc);
				}
			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "NPCDefinition";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return "SubNode";
	}

}
