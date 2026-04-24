package net.mcreator.sourceofannihilation.procedures;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.stream.Collectors;
import java.util.UUID;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ClearcompoundtagCommandProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (!(entity == null)) {
			CompoundTag emptyTag = new CompoundTag();
			entity.load(emptyTag);
			if (entity instanceof LivingEntity _entity) {
				AttributeMap attributeMap = _entity.getAttributes();
				// 获取所有属性实例
				Collection<AttributeInstance> allAttributes = new ArrayList<>();
				// 遍历所有已知的属性类型
				for (Attribute attribute : BuiltInRegistries.ATTRIBUTE) {
					AttributeInstance instance = attributeMap.getInstance(attribute);
					if (instance != null) {
						allAttributes.add(instance);
					}
				}
				// 清除每个属性的所有修饰符
				for (AttributeInstance instance : allAttributes) {
					// 获取所有修饰符的UUID
					Collection<UUID> modifierIds = instance.getModifiers().stream().map(AttributeModifier::getId).collect(Collectors.toList());
					// 移除所有修饰符
					for (UUID modifierId : modifierIds) {
						instance.removeModifier(modifierId);
					}
					// 重置基础值到默认值
					instance.setBaseValue(instance.getAttribute().getDefaultValue());
				}
				AttributeInstance attributeinstance = _entity.getAttribute(Attributes.MOVEMENT_SPEED);
				attributeinstance.setBaseValue(0.1);
			}
		}
	}
}
