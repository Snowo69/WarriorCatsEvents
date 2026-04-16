package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.snowteb.warriorcats_events.entity.custom.MossBallEntity;

public class MossBallRenderer extends ThrownItemRenderer<MossBallEntity> {
    public MossBallRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, 0.75f, false);
    }
}
