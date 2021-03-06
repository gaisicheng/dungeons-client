package com.kingx.dungeons.engine.concrete;

import com.badlogic.gdx.math.Vector2;
import com.kingx.artemis.World;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.component.AnimationComponent;
import com.kingx.dungeons.engine.component.CollisionComponent;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.HealthComponent;
import com.kingx.dungeons.engine.component.MiningComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.ShadowComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.WorldRotateComponent;
import com.kingx.dungeons.engine.component.dynamic.GravityComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.RotationComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.graphics.Shader;

public class Wanderer extends ConcreteEntity {

    private final PositionComponent positionComponent;
    private final MoveComponent moveComponent;
    private final ShaderComponent shader;
    private final TextureComponent texture;
    private final HealthComponent health;
    private final SightComponent sight;
    private final GravityComponent gravity;
    private final WorldRotateComponent worldRotate;
    private final ShadowComponent shadow;
    private final CollisionComponent collision;
    private final MiningComponent mining;
    private final AnimationComponent animationComponent;

    public Wanderer(World world, Vector2 p, float size, float speed, FollowCameraComponent camera) {
        super(world);

        positionComponent = new PositionComponent(p.x, p.y, App.PLAYER_OFFSET);
        moveComponent = new MoveComponent(0, 0);
        shader = new ShaderComponent(Shader.getShader("normal"));
        texture = new TextureComponent(Assets.getTexture("miner.walk.middle", 0));
        health = new HealthComponent(100);
        sight = new SightComponent(3f);
        gravity = new GravityComponent(3f, moveComponent);
        SizeComponent sizeComponent = new SizeComponent(size);
        worldRotate = new WorldRotateComponent(positionComponent, moveComponent, sizeComponent);
        shadow = new ShadowComponent();
        collision = new CollisionComponent();
        mining = new MiningComponent(false);

        animationComponent = new AnimationComponent("miner", texture, moveComponent);
        animationComponent.addAnimation("jump.left");
        animationComponent.addAnimation("jump.middle");
        animationComponent.addAnimation("jump.right");

        animationComponent.addAnimation("climb.left");
        animationComponent.addAnimation("climb.middle");
        animationComponent.addAnimation("climb.right");

        animationComponent.addAnimation("walk.left");
        animationComponent.addAnimation("walk.middle");
        animationComponent.addAnimation("walk.right");

        animationComponent.addAnimation("dig.left");
        animationComponent.addAnimation("dig.middle");
        animationComponent.addAnimation("dig.right");

        bag.add(positionComponent);
        bag.add(new RotationComponent(0, 1, 0));
        bag.add(new SpeedComponent(speed));
        bag.add(moveComponent);
        bag.add(sizeComponent);
        bag.add(shader);
        bag.add(texture);
        bag.add(health);
        bag.add(sight);
        bag.add(gravity);
        bag.add(worldRotate);
        bag.add(shadow);
        bag.add(collision);
        bag.add(mining);
        bag.add(animationComponent);

        if (camera != null) {
            bag.add(camera);
        }
    }

    public PositionComponent getPositionComponent() {
        return positionComponent;
    }

    public MoveComponent getMoveComponent() {
        return moveComponent;
    }

    public ShaderComponent getShader() {
        return shader;
    }

    public TextureComponent getTextures() {
        return texture;
    }

    public HealthComponent getHealth() {
        return health;
    }

    public SightComponent getSight() {
        return sight;
    }

    public GravityComponent getGravity() {
        return gravity;
    }

    public WorldRotateComponent getWorldRotate() {
        return worldRotate;
    }

    public ShadowComponent getShadow() {
        return shadow;
    }

    public MiningComponent getMining() {
        return mining;
    }

    public CollisionComponent getCollision() {
        return collision;
    }

}
