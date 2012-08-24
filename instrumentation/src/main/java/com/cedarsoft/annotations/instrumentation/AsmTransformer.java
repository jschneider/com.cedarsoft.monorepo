package com.cedarsoft.annotations.instrumentation;

//import org.kohsuke.asm4.ClassReader;
//import org.kohsuke.asm4.ClassWriter;
//import org.kohsuke.asm4.Opcodes;
//import org.kohsuke.asm4.tree.AbstractInsnNode;
//import org.kohsuke.asm4.tree.AnnotationNode;
//import org.kohsuke.asm4.tree.ClassNode;
//import org.kohsuke.asm4.tree.InsnList;
//import org.kohsuke.asm4.tree.LdcInsnNode;
//import org.kohsuke.asm4.tree.MethodInsnNode;
//import org.kohsuke.asm4.tree.MethodNode;

import javax.annotation.Nonnull;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Iterator;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class AsmTransformer implements ClassFileTransformer {
  @Override
  public byte[] transform( ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer ) throws IllegalClassFormatException {
    return classfileBuffer;
//    ClassReader cr = new ClassReader( classfileBuffer );
//    ClassNode classNode = new ClassNode();
//
//    cr.accept( classNode, 0 );
//
//    //Let's move through all the methods
//    for ( MethodNode methodNode : classNode.methods ) {
//      System.out.println( methodNode.name + "  " + methodNode.desc );
//      boolean hasAnnotation = isAnnotated( methodNode );
//      if ( hasAnnotation ) {
//        //Lets insert the begin logger
//        InsnList beginList = new InsnList();
//        beginList.add( new LdcInsnNode( methodNode.name ) );
//        beginList.add( new MethodInsnNode( Opcodes.INVOKESTATIC, "com/geekyarticles/asm/Logger", "logMethodStart", "(Ljava/lang/String;)V" ) );
//
//        beginList.add( new  );
//
//        Iterator<AbstractInsnNode> insnNodes = methodNode.instructions.iterator();
//        while ( insnNodes.hasNext() ) {
//          System.out.println( insnNodes.next().getOpcode() );
//        }
//
//        methodNode.instructions.insert( beginList );
//        System.out.println( methodNode.instructions );
//
//        //A method can have multiple places for return
//        //All of them must be handled.
//        insnNodes = methodNode.instructions.iterator();
//        while ( insnNodes.hasNext() ) {
//          AbstractInsnNode insn = insnNodes.next();
//          System.out.println( insn.getOpcode() );
//
//          if ( insn.getOpcode() == Opcodes.IRETURN
//            || insn.getOpcode() == Opcodes.RETURN
//            || insn.getOpcode() == Opcodes.ARETURN
//            || insn.getOpcode() == Opcodes.LRETURN
//            || insn.getOpcode() == Opcodes.DRETURN ) {
//            InsnList endList = new InsnList();
//            endList.add( new LdcInsnNode( methodNode.name ) );
//            endList.add( new MethodInsnNode( Opcodes.INVOKESTATIC, "com/geekyarticles/asm/Logger", "logMethodReturn", "(Ljava/lang/String;)V" ) );
//            methodNode.instructions.insertBefore( insn, endList );
//          }
//
//        }
//
//      }
//    }
//
//    //We are done now. so dump the class
//    ClassWriter cw = new ClassWriter( ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES );
//    classNode.accept( cw );
//
//    return cw.toByteArray();
  }
}
