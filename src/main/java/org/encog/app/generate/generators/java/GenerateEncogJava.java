package org.encog.app.generate.generators.java;

import org.encog.app.generate.GenerationError;
import org.encog.app.generate.generators.AbstractGenerator;
import org.encog.app.generate.program.EncogArgType;
import org.encog.app.generate.program.EncogProgram;
import org.encog.app.generate.program.EncogProgramNode;
import org.encog.app.generate.program.EncogTreeNode;

public class GenerateEncogJava extends AbstractGenerator {
	
	private void generateComment(EncogProgramNode commentNode) {
		this.addLine("// " + commentNode.getName());
	}
	
	private void generateClass(EncogProgramNode node) {
		addBreak();
		indentLine("public class " + node.getName() + " {");
		generateForChildren(node);
		unIndentLine("}");
	}
	
	private void generateMainFunction(EncogProgramNode node) {
		addBreak();
		indentLine("public static void main(String[] args) {");
		generateForChildren(node);
		unIndentLine("}");
	}
	
	private void generateConst(EncogProgramNode node) {
		boolean quote = false;
		StringBuilder line = new StringBuilder();
		String t = node.getArgs().get(1).getValue();
		
		line.append("public static final ");
		
		if(EncogArgType.String.name().equals(t) ) {
			line.append("String");
			quote = true;
		} else if(EncogArgType.Int.name().equals(t) ) {
			line.append("int");
		} else if(EncogArgType.Float.name().equals(t) ) {
			line.append("double");
		} else {
			throw new GenerationError("Unknown type: " + t);
		}
		
		line.append(' ');
		line.append(node.getName());
		
		line.append(" = ");
		
		if( quote ) {
			line.append("\"");
		}
		
		line.append(node.getArgs().get(0).getValue());
		
		if( quote ) {
			line.append("\"");
		}
		
		line.append(';');
		
		addLine(line.toString());		
	}
	
	private void generateNode(EncogProgramNode node) {
		switch(node.getType()) {
			case Comment:
				generateComment(node);
				break;
			case Class:
				generateClass(node);
				break;
			case MainFunction:
				generateMainFunction(node);
				break;
			case Const:
				generateConst(node);
				break;
		}
	}
	
	private void generateForChildren(EncogTreeNode parent) {
		for(EncogProgramNode node : parent.getChildren()) {
			generateNode(node);
		}
	}
	
	public void generate(EncogProgram program) {
		generateForChildren(program);		
	}
}
