package com.kingx.dungeons.engine.system.client;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.ai.controller.ParentTaskController;
import com.kingx.dungeons.engine.ai.task.LeafTask;
import com.kingx.dungeons.engine.ai.task.Selector;
import com.kingx.dungeons.engine.ai.task.UpdateFilter;
import com.kingx.dungeons.engine.component.ZombieAIComponent;
import com.kingx.dungeons.geom.Collision;

public class ZombieAI extends EntityProcessingSystem {
    @Mapper
    static ComponentMapper<ZombieAIComponent> dataMapper;
    private final UpdateFilter planner;

    public ZombieAI() {
        super(Aspect.getAspectForAll(ZombieAIComponent.class));

        Selector selector = new Selector();
        ((ParentTaskController) selector.getControl()).add(new Search(App.getMaze().getVerts()));
        ((ParentTaskController) selector.getControl()).add(new Idle());
        planner = new UpdateFilter(selector, 1);
    }

    @Override
    protected void process(Entity e) {
        planner.start(e);
        planner.doAction(e);
    }

    private static class Search extends LeafTask {

        private ZombieAIComponent data;
        private final float[] verts;

        private Search(float[] verts) {
            this.verts = verts;
        }

        @Override
        public boolean checkConditions(Entity entity) {
            this.data = dataMapper.get(entity);

            Ray ray = getRay(data.playerPosition.vector, data.entityPosition.vector);

            if (Collision.distance(data.playerPosition.vector, data.entityPosition.vector) <= data.alertRadius) {
                return !Collision.intersectRayTrianglesBetweenPoints(ray, verts, data.playerPosition.vector, data.entityPosition.vector);
            }

            return false;
        }

        @Override
        public boolean doAction(Entity entity) {
            data.target = data.playerPosition.vector.cpy();
            data.entityMove.vector.set(data.target.x - data.entityPosition.getX(), data.target.y - data.entityPosition.getY()).nor();
            data.entitySpeed.setCurrent(data.entitySpeed.turbo);
            data.shader.setTexture(getRightTexture(data.entityMove.vector));
            data.shader.setColor(data.alertColor);
            return true;
        }

        private Ray getRay(Vector3 a, Vector3 b) {
            return new Ray(a, b.cpy().sub(a));
        }

    }

    private static class Idle extends LeafTask {

        private ZombieAIComponent data;
        private int counter;

        @Override
        public boolean checkConditions(Entity entity) {
            this.data = dataMapper.get(entity);
            return true;
        }

        @Override
        public boolean doAction(Entity e) {
            counter++;
            if (counter > 40) {
                counter = 0;
                data.entityMove.vector = getNewDirection();
                data.shader.setTexture(getRightTexture(data.entityMove.vector));
            }
            data.entitySpeed.setCurrent(data.entitySpeed.normal);
            data.shader.setColor(data.normalColor);
            return true;
        }

        private Vector2 getNewDirection() {
            return new Vector2(App.rand.nextFloat() - 0.5f, App.rand.nextFloat() - 0.5f).nor();
        }
    }

    public static TextureRegion getRightTexture(Vector2 vector) {
        int ang = (int) Math.toDegrees(Math.atan2(vector.y, vector.x)) + 90 + 22;
        if (ang < 0) {
            ang += 360;
        } else if (ang > 360) {
            ang -= 360;
        }
        ang = (int) (ang / 360f * 8);
        return Assets.getTexture("zombie", ang);
    }
}