package mcjty.hologui.gui;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.CloseStrategy;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IGuiTile;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.lib.varia.SafeClientTools;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class HoloGuiEntity extends Entity implements IHoloGuiEntity {

    private static final EntityDataAccessor<Optional<BlockPos>> GUITILE = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<String> TAG = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> GUIID = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> CLOSESTRATEGY = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.INT);

    private AABB playerDetectionBox = null;

    private int timeout;
    private int maxTimeout;
    private int ticks;  // For syncing TE to client
    private IGuiComponent<?> panel;

    // Client side only
    private double cursorX;
    private double cursorY;
    private Vec3 hit;
    private String lastGuiId = null;    // Last guiId that was rendered on the client. If it changes then we have to redo the gui
    private String lastTag = null;      // Last tag
    public int tooltipTimeout = 10;
    public IGuiComponent<?> tooltipComponent = null;

    public HoloGuiEntity(EntityType<? extends HoloGuiEntity> type, Level worldIn) {
        super(type, worldIn);
        maxTimeout = 20 * 4;
        timeout = maxTimeout;
        ticks = 5;
        // @todo 1.14
        setScale(1);
//        setSize(1f, 1f);
    }


    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    public double getCursorX() {
        return cursorX;
    }

    public double getCursorY() {
        return cursorY;
    }

    @Override
    public void setMaxTimeout(int maxTimeout) {
        this.maxTimeout = maxTimeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Vec3 getHit() {
        return hit;
    }

    private static final AABB HOLO_ZERO_AABB = new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

    @Override
    public float getScale() {
        return this.entityData.get(SCALE);
    }

    @Override
    public void setScale(float scale) {
        this.entityData.set(SCALE, scale);
    }
    public void setGuiTile(BlockPos guiTile) {
        this.entityData.set(GUITILE, Optional.ofNullable(guiTile));
    }

    public BlockPos getGuiTile() {
        return (BlockPos) ((Optional) this.entityData.get(GUITILE)).orElse(null);
    }

    @Override
    public String getGuiId() {
        return this.entityData.get(GUIID);
    }

    public void setGuiId(String guiId) {
        this.entityData.set(GUIID, guiId);
    }

    public void setTag(String tag) {
        this.entityData.set(TAG, tag);
    }

    @Override
    public String getTag() {
        return this.entityData.get(TAG);
    }

    @Override
    public void switchTag(String tag) {
        if (tag.equals(getTag())) {
            return;
        }
        setTag(tag);
        panel = null;
    }

    @Override
    public void tick() {
        super.tick();
        // @todo 1.14?
        setScale(getScale());

        if (level.isClientSide) {
            String id = getGuiId();
            if (id != null && !id.isEmpty()) {
                if (!id.equals(lastGuiId)) {
                    panel = null;
                    lastGuiId = id;
                }
            }
            String tag = getTag();
            if (tag != null && !tag.isEmpty()) {
                if (!tag.equals(lastTag)) {
                    panel = null;
                    lastTag = tag;
                }
            }
            onUpdateClient();
        } else {
            onUpdateServer();
        }
    }

    private void closeGui() {
        closeGui((Player)null);
    }

    private void closeGui(@Nullable Player player) {
        level.playSound(player, getX(), getY(), getZ(), HoloGuiSounds.GUIOPEN.get(), SoundSource.PLAYERS, 0.2f, 1.0f); // @todo configure
        discard();
    }

    private void onUpdateServer() {
        if (playerDetectionBox == null) {
            playerDetectionBox = new AABB(getX() - 10, getY() - 10, getZ() - 10, getX() + 10, getY() + 10, getZ() + 10);
        }

        ticks--;
        if (ticks < 0) {
            ticks = 5;
            String id = getGuiId();
            if (id != null && !id.isEmpty()) {
                // @todo do we have to do anything here?
            } else {
                BlockPos tile = getGuiTile();
                if (tile == null) {
                    // Block connected to GUI has been destroyed.
                    closeGui();

                    return;
                }

                IGuiTile guiTile = HoloGui.guiHandler.getGuiTile(level, tile);
                if (guiTile == null) {
                    // Block connected to GUI has been destroyed.
                    closeGui();

                    return;
                } else {
                    guiTile.syncToClient();
                }
            }
        }

        if (hasCloseStrategy(CloseStrategy.TIMEOUT)) {
            timeout--;
            if (timeout <= 0) {
                closeGui();
            }
            if (hasCloseStrategy(CloseStrategy.TIMEOUT_RESET)) {
                if (level.getEntitiesOfClass(Player.class, playerDetectionBox)
                        .stream()
                        .anyMatch(this::playerLooksAtMe)) {
                    timeout = maxTimeout;
                }
            }
        }
    }


    private boolean playerLooksAtMe(Player player) {
        Vec3 lookVec = getLookAngle();
        Vec3 v = getIntersect3D(player, lookVec);
        Vec2 vec2d = get2DProjection(lookVec, v);

        double cx;
        double cy;
        float scale = getScale();

        float factor = 1f - (1f - scale) * (.4f / .25f);
        float offset = .8f + (1f - scale) * (3.3f / .25f);

        cx = vec2d.x * 10 / factor - offset;
        cy = vec2d.y * 10 / factor - offset;

        return cx >= -1 && cx <= 11 && cy >= -1 && cy <= 11;
    }

    private void onUpdateClient() {
        Player player = SafeClientTools.getClientPlayer();
        Vec3 lookVec = getLookAngle();
        Vec3 v = getIntersect3D(player, lookVec);
        Vec2 vec2d = get2DProjection(lookVec, v);

        float scale = getScale();

        float factor = 1f - (1f - scale) * (.4f / .25f);
        float offset = .8f + (1f - scale) * (3.3f / .25f);

        cursorX = vec2d.x * 10 / factor - offset;
        cursorY = vec2d.y * 10 / factor - offset;

        hit = v;
    }

    @Override
    public void switchGui(String guiId) {
        setGuiId(guiId);
        panel = null;
    }

    @Nonnull
    @Override
    public java.util.Optional<IGuiComponent<?>> findComponent(String name) {
        if (panel == null) {
            return java.util.Optional.empty();
        }

        return panel.findChild(name);
    }

    public IGuiComponent<?> getGui(Player player) {
        if (panel == null) {
            String id = getGuiId();
            if (id != null && !id.isEmpty()) {
                panel = HoloGui.guiHandler.getGuiRegistry().createGui(id, player);
            } else {
                BlockPos tile = getGuiTile();
                if (tile != null) {
                    IGuiTile guiTile = HoloGui.guiHandler.getGuiTile(level, tile);
                    if (guiTile != null) {
                        panel = guiTile.createGui(getTag(), HoloGui.guiHandler.getComponentRegistry());
                    }
                }
            }
        }
        return panel;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void setCloseStrategy(int strategy) {
        this.entityData.set(CLOSESTRATEGY, strategy);
    }

    @Override
    public int getCloseStrategy() {
        return this.entityData.get(CLOSESTRATEGY);
    }

    @Override
    public boolean hasCloseStrategy(int s) {
        return (getCloseStrategy() & s) != 0;
    }

    @Nonnull
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        // The small holo is used as a child on the flux levitator. It doesn't close like this
        if (hasCloseStrategy(CloseStrategy.RIGHTCLICK)) {
            closeGui(player);
        }
        return InteractionResult.PASS;
    }

    private Vec2 intersect(Player player) {
        Vec3 lookVec = getLookAngle();
        Vec3 v = getIntersect3D(player, lookVec);
        return get2DProjection(lookVec, v);
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    private Vec2 get2DProjection(Vec3 lookVec, Vec3 v) {
        double x = v.x;
        double y = v.y;
        double z = v.z;
        // Origin on plane is getPosX(), getPosY(), getPosZ()
        // Point on plane in 2D x direction: cross( (0,1,0), (xn,yn,zn) )
        // Point on plane in 2D y direction: getPosX(), getPosY()-1, getPosZ()
        Vec3 vx = lookVec.cross(new Vec3(0, 1, 0));    // @todo optimize
        Vec3 vy = new Vec3(0, -1, 0);
//        Vector3d vy = vx.crossProduct(new Vector3d(1, 0, 0));
        x -= getX();
        y -= getY();
        z -= getZ();
        double x2d = vx.x * x + vx.y * y + vx.z * z + .5;
        double y2d = vy.x * x + vy.y * y + vy.z * z + 1;

        return new Vec2((float)x2d, (float)y2d);
    }

    private Vec3 getIntersect3D(Player player, Vec3 lookVec) {
        // Center point of plane: getPosX(), getPosY(), getPosZ()
        // Perpendicular to the plane: getLookVec()
        double xn = lookVec.x;
        double yn = lookVec.y;
        double zn = lookVec.z;

        // Plane: Ax + By + Cz + D = 0
        double D = -(xn * getX() + yn * getY() + zn * getZ());
        double A = xn;
        double B = yn;
        double C = zn;

        // Line (from player): (x = x1 + at, y = y1 + bt, z = z1 + ct)
        double x1 = player.getX();
        double y1 = player.getY() + player.getEyeHeight();
        double z1 = player.getZ();
        Vec3 playerLookVec = player.getLookAngle();
        double a = playerLookVec.x;
        double b = playerLookVec.y;
        double c = playerLookVec.z;

        // Intersection:
        double factor = (A * x1 + B * y1 + C * z1 + D) / (A * a + B * b + C * c);
        double x = x1 - a * factor;
        double y = y1 - b * factor;
        double z = z1 - c * factor;
        return new Vec3(x, y, z);
    }

    @Override
    public boolean skipAttackInteraction(Entity entityIn) {
        if (entityIn instanceof Player player) {
            Vec2 vec2d = intersect(player);
            IGuiComponent gui = getGui(player);
            if (gui != null) {
                double x;
                double y;
                float scale = getScale();

                float factor = 1f - (1f - scale) * (.4f / .25f);
                float offset = .8f + (1f - scale) * (3.3f / .25f);

                x = vec2d.x * 10 / factor - offset;
                y = vec2d.y * 10 / factor - offset;

                if (!level.isClientSide) {
                    gui.hit(player, this, x, y);
                } else {
                    gui.hitClient(player, this, x, y);
                }
            }
        }
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

// @todo 1.14: check EntityRenderer.isMultipass
//    @Override
//    public boolean shouldRenderInPass(int pass) {
//        return pass == 1;
//    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(GUITILE, Optional.empty());
        this.entityData.define(TAG, "");
        this.entityData.define(GUIID, "");
        this.entityData.define(SCALE, 1.0f);
        this.entityData.define(CLOSESTRATEGY, CloseStrategy.TIMEOUT + CloseStrategy.TIMEOUT_RESET + CloseStrategy.RIGHTCLICK);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.timeout = compound.getInt("timeout");
        this.maxTimeout = compound.getInt("maxTimeout");
        if (compound.contains("guix")) {
            int x = compound.getInt("guix");
            int y = compound.getInt("guiy");
            int z = compound.getInt("guiz");
            setGuiTile(new BlockPos(x, y, z));
        }
        setTag(compound.getString("tag"));
        setGuiId(compound.getString("guiId"));
        if (compound.contains("scale")) {
            setScale(compound.getFloat("scale"));
        }
        if (compound.contains("closeStrategy")) {
            setCloseStrategy(compound.getInt("closeStrategy"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("timeout", timeout);
        compound.putInt("maxTimeout", maxTimeout);
        BlockPos tile = getGuiTile();
        if (tile != null) {
            compound.putInt("guix", tile.getX());
            compound.putInt("guiy", tile.getY());
            compound.putInt("guiz", tile.getZ());
        }
        String tag = getTag();
        if (tag != null) {
            compound.putString("tag", tag);
        }
        String guiid = getGuiId();
        if (guiid != null) {
            compound.putString("guiId", guiid);
        }
        compound.putFloat("scale", getScale());
        compound.putInt("closeStrategy", getCloseStrategy());
    }
}
