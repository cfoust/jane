package com.sqweebloid.analysers;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import com.sqweebloid.utils.*;

public abstract class AbstractAnalyser {
	public Updater instance;
	
	public AbstractAnalyser(Updater i) {
        instance = i;
    }
	
	public abstract boolean canRun(ClassNode node);
	public abstract void analyse(ClassNode node);
	public abstract String getName();
	public abstract String requires();
}
