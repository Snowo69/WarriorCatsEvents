package net.snowteb.warriorcats_events.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.effect.ModEffects;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticleFootprint extends TextureSheetParticle {

    private float initialAlpha;
    private final BlockPos position;
    private final float angle;

    private final String entityName;

    private final int entityID;

    protected ParticleFootprint(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprite, EntityBasedParticleType type) {
        super(level, x, y, z, vx, vy, vz);

        this.position = new BlockPos(Mth.floor(this.x), Mth.floor(this.y - 0.1f), Mth.floor(this.z));

        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        this.entityName = type.entity.getType().getDescription().getString();
        this.entityID = type.entity.getId();

        this.angle = (float) -Mth.atan2(vx, -vz);

        this.lifetime = 45 * 20;

        if (type.entity.isBaby()) {
            this.quadSize = 0.07f;
        } else {
            this.quadSize = 0.15f;
        }

        this.setAlpha(1f);

        this.pickSprite(sprite);
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        this.initialAlpha = alpha;
    }

    @Override
    public void tick() {

        if (this.level.isRaining() && this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, position).getY() <= this.y) {
            if (this.age < this.lifetime) this.age += 2;
        } else {
            if (this.age < this.lifetime) this.age++;
        }

        if (this.age >= this.lifetime || this.level.isEmptyBlock(position)) this.remove();

        if (this.age > this.lifetime / 2f) {
            this.alpha = this.initialAlpha - (this.initialAlpha * (this.age - this.lifetime / 2f) / (this.lifetime / 2f));
        }

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {

        if (Minecraft.getInstance().player != null) {
            if (!Minecraft.getInstance().player.hasEffect(ModEffects.SHARP_SCENT.get())) {
                return;
            }
        }

        Vec3 cameraPos = pRenderInfo.getPosition();

        float px = (float)(Mth.lerp(pPartialTicks, this.xo, this.x) - cameraPos.x);
        float py = (float)(Mth.lerp(pPartialTicks, this.yo, this.y) - cameraPos.y);
        float pz = (float)(Mth.lerp(pPartialTicks, this.zo, this.z) - cameraPos.z);

        float halfAngle = this.angle * 0.5f;
        float sin = Mth.sin(halfAngle);
        float cos = Mth.cos(halfAngle);

        Quaternionf rotation = new Quaternionf(0.0f, sin, 0.0f, cos);

        float size = this.getQuadSize(pPartialTicks);

        Vector3f vector0 = new Vector3f(-1f, 0f, -1f);
        Vector3f vector1 = new Vector3f(-1f, 0f,  1f);
        Vector3f vector2 = new Vector3f( 1f, 0f,  1f);
        Vector3f vector3 = new Vector3f( 1f, 0f, -1f);

        if (Minecraft.getInstance().player != null) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            HitResult hit = Minecraft.getInstance().hitResult;
            if (hit instanceof BlockHitResult blockHitResult) {
                Vec3 hitPos = blockHitResult.getLocation();
                AABB box = new AABB(this.position).inflate(0.6);
                if (box.contains(hitPos) && localPlayer.distanceToSqr(this.position.getCenter()) < 1.0*1.0 && this.entityID != localPlayer.getId()) {
                    if (!WCEClient.lookingAtParticle.equals(this.entityName)) {
                        WCEClient.lookingAtParticle = this.entityName;
                    }
                    WCEClient.newParticleTime = 5;
                }
            }
        }

        vector0.rotate(rotation).mul(size).add(px, py, pz);
        vector1.rotate(rotation).mul(size).add(px, py, pz);
        vector2.rotate(rotation).mul(size).add(px, py, pz);
        vector3.rotate(rotation).mul(size).add(px, py, pz);

        float u0 = this.sprite.getU0();
        float u1 = this.sprite.getU1();
        float v0 = this.sprite.getV0();
        float v1 = this.sprite.getV1();

        int light = this.getLightColor(pPartialTicks);

        pBuffer.vertex(vector0.x, vector0.y, vector0.z).uv(u1, v0)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(light).endVertex();

        pBuffer.vertex(vector1.x, vector1.y, vector1.z).uv(u1, v1)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(light).endVertex();

        pBuffer.vertex(vector2.x, vector2.y, vector2.z).uv(u0, v1)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(light).endVertex();

        pBuffer.vertex(vector3.x, vector3.y, vector3.z).uv(u0, v0)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(light).endVertex();

    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ) {
            return new ParticleFootprint(level, x, y, z, speedX, speedY, speedZ, this.spriteProvider, (EntityBasedParticleType) type);
        }
    }

}
