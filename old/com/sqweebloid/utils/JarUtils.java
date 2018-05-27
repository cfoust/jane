package com.sqweebloid.utils;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;


public class JarUtils {
	public static HashMap<String,ClassNode> parseJar(JarFile jar) {
		HashMap<String,ClassNode> classes = new HashMap<>();
		try {
			Enumeration<?> enumeration = jar.entries();
			while (enumeration.hasMoreElements()) {
				JarEntry entry = (JarEntry) enumeration.nextElement();
				if (entry.getName().endsWith(".class")) {
					ClassReader classReader = new ClassReader(jar.getInputStream(entry));
					ClassNode classNode = new ClassNode();
					classReader.accept(classNode,ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
					classes.put(classNode.name, classNode);
				}
			}
			return classes;
		} catch (Exception e) {
			return null;
		}
	}
	public static void updateJar(HashMap<String,ClassNode> classes, JarFile jar) {
		try {
			Enumeration<?> enumeration = jar.entries();
			JarOutputStream newJ = new JarOutputStream(new FileOutputStream(new File("cl-injected.jar")),jar.getManifest());
			while (enumeration.hasMoreElements()) {
				JarEntry entry = (JarEntry) enumeration.nextElement();
				if (entry.getName().endsWith(".class")) {
						String className = entry.getName().substring(0, entry.getName().length()-6);
						if (classes.containsKey(className)) {
							newJ.putNextEntry(new JarEntry(entry.getName()));
							ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
							ClassNode node = classes.get(className);
							node.accept(cw);
							try {
								ByteArrayInputStream byteAr = new ByteArrayInputStream(cw.toByteArray());
								byte[] buffer = new byte[1024];
							    while (true)
							    {
							      int count = byteAr.read(buffer);
							      if (count == -1)
							        break;
							      newJ.write(buffer, 0, count);
							    }
							    newJ.closeEntry();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
				}
			}
			newJ.close();
			jar.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
