package mcjty.hologui.gui;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.WeakHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mcjty.hologui.HoloGui;
import mcjty.hologui.api.CloseStrategy;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IGuiTile;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IIntegerSlider;
import mcjty.hologui.api.components.ITextEntry;
import mcjty.hologui.gui.components.HoloWindowPanel;
import mcjty.lib.varia.SafeClientTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class HoloGuiEntity extends Entity implements IHoloGuiEntity {

    private static final Map<MinecraftServer, UUID> PIN_SESSIONS = new WeakHashMap<>();

    private static final EntityDataAccessor<Optional<BlockPos>> GUITILE = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<String> TAG = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> GUIID = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> CLOSESTRATEGY = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> PINNED = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(HoloGuiEntity.class, EntityDataSerializers.STRING);

    private AABB playerDetectionBox = null;

    private int timeout;
    private int maxTimeout;
    private int ticks;  // For syncing TE to client
    private IGuiComponent<?> panel;
    private UUID textEditor;
    private String textComponent;
    private long textDeadline;
    private UUID sliderEditor;
    private String sliderComponent;
    private long sliderDeadline;
    private long lastSliderUpdate = -1;

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
    public float getViewYRot(float partialTicks) {
        return Mth.rotLerp(partialTicks, yRotO, getYRot());
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

    @Nullable
    public UUID getOwnerUUID() {
        String owner = entityData.get(OWNER);
        return owner.isEmpty() ? null : UUID.fromString(owner);
    }

    public void setOwnerUUID(UUID owner) {
        entityData.set(OWNER, owner == null ? "" : owner.toString());
    }

    public void positionFor(Player player, double distance) {
        Vec3 look = player.getLookAngle();
        look = new Vec3(look.x, 0, look.z).normalize();
        snapTo(player.getX() + look.x * distance, player.getY() + player.getEyeHeight() - .5,
                player.getZ() + look.z * distance, player.getYRot(), 0);
        playerDetectionBox = null;
        timeout = maxTimeout;
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
        textEditor = null;
        sliderEditor = null;
        panel = null;
    }

    @Override
    public void tick() {
        super.tick();
        // @todo 1.14?
        setScale(getScale());

        if (level().isClientSide()) {
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

    @Override
    public boolean canClose() {
        return !isPassenger();
    }

    @Override
    public boolean canPin() {
        return canClose() && getGuiTile() != null;
    }

    @Override
    public boolean isPinned() {
        return entityData.get(PINNED);
    }

    @Override
    public void setPinned(boolean pinned) {
        entityData.set(PINNED, pinned && canPin());
        timeout = maxTimeout;
    }

    private UUID pinSession() {
        return PIN_SESSIONS.computeIfAbsent(level().getServer(), server -> UUID.randomUUID());
    }

    @Override
    public void close() {
        if (canClose() && !level().isClientSide()) {
            closeGui();
        }
    }

    private void closeGui(@Nullable Player player) {
        level().playSound(player, getX(), getY(), getZ(), HoloGuiSounds.GUIOPEN.get(), SoundSource.PLAYERS, 0.2f, 1.0f); // @todo configure
        discard();
    }

    public void beginTextEntry(Player player, String component) {
        textEditor = player.getUUID();
        textComponent = component;
        textDeadline = level().getGameTime() + 1200;
    }

    public void renewTextEntry(Player player, String component) {
        if (player.getUUID().equals(textEditor) && component.equals(textComponent) && level().getGameTime() <= textDeadline) {
            textDeadline = level().getGameTime() + 1200;
        }
    }

    public void commitTextEntry(Player player, String component, String text) {
        if (player.getUUID().equals(textEditor) && component.equals(textComponent) && level().getGameTime() <= textDeadline) {
            findComponent(component).filter(ITextEntry.class::isInstance).map(ITextEntry.class::cast)
                    .ifPresent(field -> field.commit(player, this, text));
            textEditor = null;
            textComponent = null;
            timeout = maxTimeout;
        }
    }

    public void beginSlider(Player player, String component) {
        sliderEditor = player.getUUID();
        sliderComponent = component;
        sliderDeadline = level().getGameTime() + 40;
        lastSliderUpdate = -1;
    }

    public void dragSlider(Player player, String component, boolean finish, Vec3 look) {
        long now = level().getGameTime();
        if (!player.getUUID().equals(sliderEditor) || !component.equals(sliderComponent) || now > sliderDeadline) {
            return;
        }
        if (finish || lastSliderUpdate < 0 || now - lastSliderUpdate >= 2) {
            Vec3 normal = getLookAngle();
            double denominator = look.dot(normal);
            if (Math.abs(denominator) < .0001) {
                return;
            }
            Vec3 eye = player.getEyePosition();
            double distance = position().subtract(eye).dot(normal) / denominator;
            if (distance <= 0 || distance > 8) {
                return;
            }
            Vec2 point = get2DProjection(position(), normal, eye.add(look.scale(distance)));
            float scale = getScale();
            float factor = 1f - (1f - scale) * (.4f / .25f);
            float offset = .8f + (1f - scale) * (3.3f / .25f);
            double x = point.x * 10 / factor - offset;
            double y = point.y * 10 / factor - offset;
            findComponent(component).filter(IIntegerSlider.class::isInstance).map(IIntegerSlider.class::cast)
                    .filter(slider -> Double.isFinite(x) && Double.isFinite(y) && Math.abs(y - slider.getY()) <= 2)
                    .ifPresent(slider -> slider.drag(player, this, x, y));
            lastSliderUpdate = now;
            sliderDeadline = now + 40;
            timeout = maxTimeout;
        }
        if (finish) {
            sliderEditor = null;
            sliderComponent = null;
        }
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

                IGuiTile guiTile = HoloGui.guiHandler.getGuiTile(level(), tile);
                if (guiTile == null) {
                    // Block connected to GUI has been destroyed.
                    closeGui();

                    return;
                } else {
                    guiTile.syncToClient();
                }
            }
        }

        if (!isPinned() && hasCloseStrategy(CloseStrategy.TIMEOUT) && !(textEditor != null && level().getGameTime() <= textDeadline)) {
            timeout--;
            if (timeout <= 0) {
                closeGui();
            }
            if (hasCloseStrategy(CloseStrategy.TIMEOUT_RESET)) {
                if (level().getEntitiesOfClass(Player.class, playerDetectionBox)
                        .stream()
                        .anyMatch(this::playerLooksAtMe)) {
                    timeout = maxTimeout;
                }
            }
        }
    }

    private boolean playerLooksAtMe(Player player) {
        Vec3 normal = getLookAngle();
        double direction = player.getViewVector(1).dot(normal);
        if (Math.abs(direction) < .0001 || position().subtract(player.getEyePosition()).dot(normal) / direction <= 0) {
            return false;
        }
        Vec2 vec2d = intersect(player);

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
        updateCursor(SafeClientTools.getClientPlayer(), 1.0f);
    }

    public void updateCursor(Player player, float partialTicks) {
        Vec3 origin = getPosition(partialTicks);
        Vec3 lookVec = getViewVector(partialTicks);
        Vec3 v = getIntersect3D(player, origin, lookVec, partialTicks);
        Vec2 vec2d = get2DProjection(origin, lookVec, v);

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
        textEditor = null;
        sliderEditor = null;
        panel = null;
    }

    @Nonnull
    @Override
    public Optional<IGuiComponent<?>> findComponent(String name) {
        if (panel == null) {
            return Optional.empty();
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
                    IGuiTile guiTile = HoloGui.guiHandler.getGuiTile(level(), tile);
                    if (guiTile != null) {
                        panel = guiTile.createGui(getTag(), HoloGui.guiHandler.getComponentRegistry());
                    }
                }
            }
            if (panel != null) {
                panel = new HoloWindowPanel(panel, this);
            }
        }
        return panel;
    }

    @Override
    public boolean canBeCollidedWith(Entity other) {
        // Ray targeting uses isPickable; an overlapping GUI must not lower the player's pose.
        return false;
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
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        // The small holo is used as a child on the flux levitator. It doesn't close like this
        if (canClose() && hasCloseStrategy(CloseStrategy.RIGHTCLICK)) {
            closeGui(player);
        }
        return InteractionResult.PASS;
    }

    private Vec2 intersect(Player player) {
        Vec3 lookVec = getLookAngle();
        Vec3 origin = position();
        Vec3 v = getIntersect3D(player, origin, lookVec, 1.0f);
        return get2DProjection(origin, lookVec, v);
    }

    public boolean canRiderInteract() {
        return true;
    }

    private Vec2 get2DProjection(Vec3 origin, Vec3 lookVec, Vec3 v) {
        double x = v.x;
        double y = v.y;
        double z = v.z;
        // Origin on plane is getPosX(), getPosY(), getPosZ()
        // Point on plane in 2D x direction: cross( (0,1,0), (xn,yn,zn) )
        // Point on plane in 2D y direction: getPosX(), getPosY()-1, getPosZ()
        Vec3 vx = lookVec.cross(new Vec3(0, 1, 0));    // @todo optimize
        Vec3 vy = new Vec3(0, -1, 0);
//        Vector3d vy = vx.crossProduct(new Vector3d(1, 0, 0));
        x -= origin.x;
        y -= origin.y;
        z -= origin.z;
        double x2d = vx.x * x + vx.y * y + vx.z * z + .5;
        double y2d = vy.x * x + vy.y * y + vy.z * z + 1;

        return new Vec2((float)x2d, (float)y2d);
    }

    private Vec3 getIntersect3D(Player player, Vec3 origin, Vec3 lookVec, float partialTicks) {
        // Center point of plane: getPosX(), getPosY(), getPosZ()
        // Perpendicular to the plane: getLookVec()
        double xn = lookVec.x;
        double yn = lookVec.y;
        double zn = lookVec.z;

        // Plane: Ax + By + Cz + D = 0
        double D = -lookVec.dot(origin);
        double A = xn;
        double B = yn;
        double C = zn;

        // Line (from player): (x = x1 + at, y = y1 + bt, z = z1 + ct)
        Vec3 eye = player.getEyePosition(partialTicks);
        double x1 = eye.x;
        double y1 = eye.y;
        double z1 = eye.z;
        Vec3 playerLookVec = player.getViewVector(partialTicks);
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

                if (!level().isClientSide()) {
                    gui.hit(player, this, x, y);
                } else {
                    gui.hitClient(player, this, x, y);
                }
            }
        }
        return true;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(GUITILE, Optional.empty());
        builder.define(TAG, "");
        builder.define(GUIID, "");
        builder.define(SCALE, 1.0f);
        builder.define(CLOSESTRATEGY, CloseStrategy.TIMEOUT + CloseStrategy.TIMEOUT_RESET + CloseStrategy.RIGHTCLICK);
        builder.define(PINNED, false);
        builder.define(OWNER, "");
    }

    @Override
    public void readAdditionalSaveData(ValueInput compound) {
        // An entity can reload with its chunk during play; a new server session clears pins.
        entityData.set(PINNED, false);
        setOwnerUUID(compound.read("owner", UUIDUtil.CODEC).orElse(null));
        this.timeout = compound.getIntOr("timeout", 0);
        this.maxTimeout = compound.getIntOr("maxTimeout", 0);
        if (compound.getInt("guix").isPresent()) {
            int x = compound.getIntOr("guix", 0);
            int y = compound.getIntOr("guiy", 0);
            int z = compound.getIntOr("guiz", 0);
            setGuiTile(new BlockPos(x, y, z));
        }
        setTag(compound.getStringOr("tag", ""));
        setGuiId(compound.getStringOr("guiId", ""));
        if (compound.getFloatOr("scale", -1) >= 0) {
            setScale(compound.getFloatOr("scale", 0));
        }
        if (compound.getInt("closeStrategy").isPresent()) {
            setCloseStrategy(compound.getIntOr("closeStrategy", 0));
        }
        if (!level().isClientSide() && compound.read("pinSession", UUIDUtil.CODEC).filter(pinSession()::equals).isPresent()) {
            setPinned(true);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput compound) {
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
        if (getOwnerUUID() != null) {
            compound.store("owner", UUIDUtil.CODEC, getOwnerUUID());
        }
        if (isPinned() && !level().isClientSide()) {
            compound.store("pinSession", UUIDUtil.CODEC, pinSession());
        }
    }
}
