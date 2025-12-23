package io.github.akashiikun.mavapi.impl;

import com.google.gson.Gson;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class MixinConfigPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}


	// TODO mappings
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if (mixinClassName.contains("AxolotlMixin") && !mixinClassName.contains("$")) {
			{
				MethodNode readAdditionalSaveData = targetClass.methods.stream().filter(a -> a.name.equals("readAdditionalSaveData")).findFirst().orElseThrow();
				InsnList oldList = readAdditionalSaveData.instructions;


				LdcInsnNode node = null;
				for (AbstractInsnNode instruction : readAdditionalSaveData.instructions) {
					if (instruction instanceof LdcInsnNode insnNode && "Variant".equals(insnNode.cst)) {
						node = insnNode;
						break;
					}
				}
				AbstractInsnNode from = IntStream.iterate(readAdditionalSaveData.instructions.indexOf(node), i -> i > 0, i -> i - 1).filter(i -> readAdditionalSaveData.instructions.get(i) instanceof LabelNode).mapToObj(i -> (LabelNode) readAdditionalSaveData.instructions.get(i)).findFirst().orElse(null);//readAdditionalSaveData.instructions.get(readAdditionalSaveData.instructions.indexOf(node) - 1);
				AbstractInsnNode to = null;
				for (AbstractInsnNode instruction : readAdditionalSaveData.instructions) {
					if (instruction instanceof MethodInsnNode insnNode && insnNode.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) instruction).name.equals("setVariant")) {
						System.out.println(insnNode.name + " " + insnNode.owner + " " + insnNode.desc);
						to = insnNode;
						break;
					}
				}
				InsnList list = new InsnList();

				Textifier printer = new Textifier();
				TraceMethodVisitor traceMV = new TraceMethodVisitor(printer);
				boolean adding = true;
				for (AbstractInsnNode abstractInsnNode : oldList) {
					if (abstractInsnNode == from) adding = false;
					if (adding) {
						abstractInsnNode.accept(traceMV);
						list.add(abstractInsnNode);
					}
					if (abstractInsnNode == to) adding = true;
				}
				readAdditionalSaveData.instructions = list;
				System.out.println("Printing: " + printer.text);
				printer.print(new PrintWriter(System.out));
			}

			{
				MethodNode getBreedOffspring = targetClass.methods.stream().filter(a -> a.name.equals("getBreedOffspring")).findFirst().orElseThrow();
				InsnList oldList = getBreedOffspring.instructions;


				FieldInsnNode node = null;
				for (AbstractInsnNode instruction : getBreedOffspring.instructions) {
					if (instruction instanceof FieldInsnNode insnNode && insnNode.desc.contains("RandomSource")) {
						node = insnNode;
						break;
					}
				}
				AbstractInsnNode from = IntStream.iterate(getBreedOffspring.instructions.indexOf(node), i -> i > 0, i -> i - 1).filter(i -> getBreedOffspring.instructions.get(i) instanceof LabelNode).mapToObj(i -> (LabelNode) getBreedOffspring.instructions.get(i)).findFirst().orElse(null);//readAdditionalSaveData.instructions.get(readAdditionalSaveData.instructions.indexOf(node) - 1);
				AbstractInsnNode to = null;
				for (AbstractInsnNode instruction : getBreedOffspring.instructions) {
					if (instruction instanceof MethodInsnNode insnNode && insnNode.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) instruction).name.equals("setVariant")) {
						System.out.println(insnNode.name + " " + insnNode.owner + " " + insnNode.desc);
						to = insnNode;
						break;
					}
				}
				InsnList list = new InsnList();

				Textifier printer = new Textifier();
				TraceMethodVisitor traceMV = new TraceMethodVisitor(printer);
				boolean adding = true;
				for (AbstractInsnNode abstractInsnNode : oldList) {
					if (abstractInsnNode == from) adding = false;
					if (adding) {
						abstractInsnNode.accept(traceMV);
						list.add(abstractInsnNode);
					}
					if (abstractInsnNode == to) adding = true;
				}
				getBreedOffspring.instructions = list;
				System.out.println("Printing: " + printer.text);
				printer.print(new PrintWriter(System.out));
			}

//			while (true) {
//				AbstractInsnNode insnNode = readAdditionalSaveData.instructions.get(i);
//				System.out.println("removed " + insnNode);
//				readAdditionalSaveData.instructions.remove(insnNode);
//				if (insnNode == to) break;
//			}
//
//			for (AbstractInsnNode instruction : readAdditionalSaveData.instructions) {
//				System.out.println(instruction);
//			}
//			readAdditionalSaveData.localVariables.removeIf(lv -> lv.start == from || lv.end == from);
//			readAdditionalSaveData.instructions.resetLabels();
//			readAdditionalSaveData.maxStack = -1;
//			readAdditionalSaveData.maxLocals = -1;
		}
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
