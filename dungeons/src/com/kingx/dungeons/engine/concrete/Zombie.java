package com.kingx.dungeons.engine.concrete;

import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.HealthComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.ZombieAIComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.RotationComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Zombie extends ConcreteEntity {

    public Zombie(World world, Vector3 position, float size, float speed) {
        super(world);

        PositionComponent zombiePosition = new PositionComponent(position);
        PositionComponent playerPosition = App.getPlayer().getEntity().getComponent(PositionComponent.class);
        SpeedComponent zombieSpeed = new SpeedComponent(speed);
        MoveComponent zombieMove = new MoveComponent(0, 0);
        ShaderComponent shader = new ShaderComponent(Shader.getShader("normal"));
        TextureComponent textures = new TextureComponent("skeleton", "skeleton", Colors.ZOMBIE_NORMAL);
        HealthComponent health = new HealthComponent(100);
        SightComponent sight = new SightComponent(4f);

        bag.add(zombiePosition);
        bag.add(new RotationComponent(1, 0, 0));
        bag.add(zombieSpeed);
        bag.add(zombieMove);
        bag.add(new SizeComponent(size));
        bag.add(shader);
        bag.add(textures);
        bag.add(health);
        bag.add(new ZombieAIComponent(zombiePosition, playerPosition, zombieSpeed, zombieMove, shader, textures, Colors.ZOMBIE_NORMAL,
                Colors.ZOMBIE_ALARM, sight));
    }
}
