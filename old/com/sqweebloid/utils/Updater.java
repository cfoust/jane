package com.sqweebloid.utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.jar.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import com.sqweebloid.analysers.*;


public class Updater implements Opcodes {
	public HashMap<String, ClassNode> CLASSES = new HashMap<>();
	private HashMap<String, ClassNode> processed = new HashMap<>();
	private ArrayList<AbstractAnalyser> analysers = new ArrayList<AbstractAnalyser>();
	private ArrayList<AbstractAnalyser> reQueued = new ArrayList<AbstractAnalyser>();
	public void log(String s) {System.out.println(s);}
	int fields;
	public void logInject(String inClass, String funcName, String returnType, String returnsVal){System.out.println("-->" + inClass + "." + funcName + "() returns " + returnsVal + " (" + returnType + ")"); fields++;}
	public Updater(JarFile jar) throws IOException {
		CLASSES = JarUtils.parseJar(jar);
		System.out.println("Analysing " + CLASSES.size() + " classes.\n");
		this.loadAnalysers();
		this.findClassNodes();
		this.findClassNodes();
		this.runAnalyzers();
		log("INFO: " + processed.size() + "/" + analysers.size() + " classes found.");
		JarUtils.updateJar(CLASSES, jar);
	}
	private void loadAnalysers() {
		this.analysers.add(new AnimableAnalyser(this));
		this.analysers.add(new NodeAnalyser(this));
		this.analysers.add(new SubNodeAnalyser(this));
		this.analysers.add(new ClientAnalyser(this));
		this.analysers.add(new NPCDefinitionAnalyser(this));
		this.analysers.add(new NPCAnalyser(this));
		this.analysers.add(new EntityAnalyser(this));
		this.analysers.add(new PlayerAnalyser(this));
		this.analysers.add(new CanvasAnalyser(this));
		this.analysers.add(new MouseEventAnalyser(this));
		this.analysers.add(new SceneTileAnalyser(this));
		this.analysers.add(new SceneSpawnRequestAnalyser(this));
		this.analysers.add(new SceneGraphAnalyser(this));
	}
	private void findClassNodes() {
		for (ClassNode node: CLASSES.values()) {
			for (AbstractAnalyser analyser: this.analysers) {
				if (((this.processed.containsKey(analyser.requires()))||(analyser.requires() == null))
						&& (analyser.canRun(node))
						&& (!processed.containsValue(node))){
							processed.put(analyser.getName(), node);
				}
			}
		}
	}
	private void runAnalyzers() {
		fields = 0;
		for (AbstractAnalyser analyser: this.analysers) {
			if (this.processed.containsKey(analyser.getName())) {
				ClassNode node = this.processed.get(analyser.getName());
				log("^ " + analyser.getName() + " found as class " + node.name);
				analyser.analyse(node);
			}
			log("\n");
		}
		log(fields + " methods injected.");
	}
	public void createGetter(ClassNode node, FieldNode fn, String name) {
		MethodNode getter = new MethodNode(ACC_PUBLIC, name, "()" + fn.desc, null, null);
        getter.instructions.add(new VarInsnNode(ALOAD, 0));
        getter.instructions.add(new FieldInsnNode(GETFIELD, node.name, fn.name, fn.desc));
        getter.instructions.add(new InsnNode(IRETURN));
        int size = getter.instructions.size();
        getter.visitMaxs(size, size);
        getter.visitEnd();
        node.methods.add(getter);
        System.out.println("--" + name + "() created for field " + node.name + "." + fn.name);
	}
	public void createGetterWhichReturnsCustom(ClassNode node, FieldNode fn, String name, String customReturn,boolean shit) {
		MethodNode getter = new MethodNode(ACC_PUBLIC, name, "()" + customReturn, null, null);
        //getter.instructions.add(new VarInsnNode(ALOAD, 0));
        //getter.instructions.add(new FieldInsnNode(GETFIELD, node.name, fn.name, fn.desc));
		getter.instructions.add(new FieldInsnNode(GETSTATIC, node.name, fn.name, fn.desc));
        getter.instructions.add(new InsnNode(ARETURN));
        int size = getter.instructions.size();
        getter.visitMaxs(size, size);
        getter.visitEnd();
        node.methods.add(getter);
        System.out.println("--" + name + "() created for field " + node.name + "." + fn.name);
	}
	public String getClassName(String input) {
        if (processed.containsKey(input)) {
            String pro = processed.get(input).name;
            return pro;
        }
        return "null";
    }
}
