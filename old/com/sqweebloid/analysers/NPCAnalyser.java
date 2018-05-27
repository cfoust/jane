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

public class NPCAnalyser extends AbstractAnalyser {

	public NPCAnalyser(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canRun(ClassNode node) {
        if (node.superName.equals(instance.getClassName("Entity"))) {
            for (FieldNode fn : (List<FieldNode>) node.fields) {
                if ((fn.desc.equals("L" + instance.getClassName("NPCDefinition") + ";")) && (fn.access != Opcodes.ACC_STATIC)) {
                    return true;
                }
            }
        }
		return false;
	}

	@Override
	public void analyse(ClassNode node) {
		ListIterator<FieldNode> fnIt = node.fields.listIterator();
		int num = 0;
		while (fnIt.hasNext()) {
			FieldNode fn = fnIt.next();
			if (fn.desc.equals("L" + instance.getClassName("NPCDefinition") + ";")) {
				node.interfaces.add("com/sqweebloid/interfaces/NPC");
				{
					MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getDefinition", "()Lcom/sqweebloid/interfaces/NPCDefinition;", null, null);
					getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
					getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, fn.name, fn.desc));
			        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
			        int size = getter.instructions.size();
			        getter.visitMaxs(size, size);
			        getter.visitEnd();
			        node.methods.add(getter);
			        instance.logInject(node.name, "getDefinition", fn.desc, node.name + "." + fn.name);
				}
			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "NPC";
	}

	@Override
	public String requires() {
		// TODO Auto-generated method stub
		return "Entity";
	}

}
