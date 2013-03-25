package com.kingx.dungeons.server;

import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.kingx.artemis.World;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.dynamic.GravityComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.system.WorldRotateSystem;
import com.kingx.dungeons.engine.system.client.CollisionSystem;
import com.kingx.dungeons.engine.system.client.GravitySystem;
import com.kingx.dungeons.engine.system.client.MovementSystem;
import com.kingx.dungeons.input.InputConstants;
import com.kingx.dungeons.input.Touch;

public class OfflineServer extends AbstractServer {

    public OfflineServer(World world) {
        super(world);
        world.setSystem(new GravitySystem());
        world.setSystem(new MovementSystem());
        // world.setSystem(new ZombieAI());
        world.setSystem(new WorldRotateSystem());
        world.setSystem(new CollisionSystem());
    }

    @Override
    public void process(ClientCommand c) {
        processInput(c);
    }

    @Override
    public void updateInternal(float delta) {
        world.setDelta(delta);
        world.process();
    }

    private void processInput(ClientCommand c) {
        if (c.getAction() < 256) {
            processKey(c);
        } else {
            processTouch(c);
        }

    }

    private void processKey(ClientCommand c) {
        MoveComponent move = App.getPlayer().getMoveComponent();
        GravityComponent gravity = App.getPlayer().getGravity();
        int mapped = getKey(c.getAction(), move.mapping);
        switch (mapped) {
            case Keys.A:
                move.vector.x += c.getValue() == 0 ? 1 : -1;
                break;
            case Keys.D:
                move.vector.x += c.getValue() == 0 ? -1 : 1;
                break;
            case Keys.SPACE:
                if (!gravity.isFalling() && c.getValue() == 1) {
                    move.vector.y = 1.5f;
                }
                break;
        }
    }

    private int getKey(int keycode, Map<Integer, Integer> mapping) {
        Integer mapped = mapping.get(keycode);
        return mapped == null ? keycode : mapped;
    }

    Touch touch = new Touch();

    private void processTouch(ClientCommand c) {
        switch (c.getAction()) {
            case InputConstants.TOUCH_X_DOWN:
                touch.setX(c.getValue());
                touch.press();
                break;
            case InputConstants.TOUCH_Y_DOWN:
                touch.setY(c.getValue());
                touch.press();
                break;
            case InputConstants.TOUCH_X_UP:
                touch.setX(c.getValue());
                touch.release();
                break;
            case InputConstants.TOUCH_Y_UP:
                touch.setY(c.getValue());
                touch.release();
                break;
        }
    }
}
