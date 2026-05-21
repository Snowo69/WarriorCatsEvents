package net.snowteb.warriorcats_events.diseases;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class DiseaseType<T extends Disease<T>> {

    private final Component name;
    private final Component description;
    private final int maxTimeSeconds;
    private final int maxLevel;
    private final int maxHealLevel;
    private final String id;
    private boolean infinite = false;
    private final ResourceLocation icon;
    private boolean contagious = false;

    private final BiConsumer<Diseaseable<?>, T> tickAction;

    private final Supplier<T> factory;

    public DiseaseType(ResourceLocation id, int maxTimeSeconds, int maxLevel, int maxHealLevel, BiConsumer<Diseaseable<?>, T> tickAction, Supplier<T> factory) {
        this.name = Component.translatable("disease." + id.getNamespace() + "." + id.getPath());
        this.description = Component.translatable("disease." + id.getNamespace() + "." + id.getPath() + "." + "description");
        this.maxTimeSeconds = maxTimeSeconds;
        this.maxLevel = maxLevel;
        this.maxHealLevel = maxHealLevel;
        this.tickAction = tickAction;
        this.factory = factory;
        this.id = id.toString();
        this.icon = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/diseases/" + id.getPath() + ".png");
    }

    public DiseaseType<T> infinite() {
        this.infinite = true;
        return this;
    }

    public DiseaseType<T> contagious() {
        this.contagious = true;
        return this;
    }



    public T create() {
        T disease = factory.get();
        disease.setType(this);
        return disease;
    }

    public int getMaxTimeSeconds() {
        return maxTimeSeconds;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getMaxHealLevel() {
        return maxHealLevel;
    }

    public String getID() {
        return id;
    }

    public BiConsumer<Diseaseable<?>, T> getTickAction() {
        return tickAction;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public Component getDescription() {
        return description;
    }

    public Component getName() {
        return name;
    }

    public boolean isContagious() {
        return contagious;
    }

    public boolean is(DiseaseType<?> type) {
        return type.equals(this);
    }

}
