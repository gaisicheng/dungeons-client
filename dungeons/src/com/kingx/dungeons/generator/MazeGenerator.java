package com.kingx.dungeons.generator;

import java.util.ArrayList;

import com.kingx.dungeons.App;
import com.kingx.dungeons.BlockPair;
import com.kingx.dungeons.geom.Point;

public class MazeGenerator extends AbstractGenerator {

    private final Point.Int[] ways = new Point.Int[4];
    private final boolean[] flags = new boolean[4];
    private BlockPair[][] maze;

    @Override
    public BlockPair[][] build(int width, int height) {
        maze = new BlockPair[width][height];
        current = new Point.Int(App.rand.nextInt(width), App.rand.nextInt(height));
        Point.Int temp;
        while (true) {
            while ((temp = getWay()) == null) {
                if (!goBack()) {
                    return maze;
                }
            }
            mark(temp);
        }
    }

    private final ArrayList<Point.Int> trail = new ArrayList<Point.Int>();
    private Point.Int current;

    private void mark(Point.Int temp) {
        trail.add(new Point.Int(temp));
        maze[temp.x][temp.y] = null;
        current.set(temp);
    }

    private boolean goBack() {
        if (trail.size() > 1) {
            trail.remove(trail.size() - 1);
            current.set(trail.get(trail.size() - 1));
            return true;
        }
        return false;
    }

    private Point.Int getWay() {
        int added = 0;
        fillAdajcentBlocks(ways, current);
        for (int i = 0; i < ways.length; i++) {

            if (isInRange(ways[i]) && !isRoad(ways[i])) {
                if (!penetrateWall(ways[i])) {
                    ways[added++] = ways[i];
                    continue;
                }
            }
            flags[i] = false;
        }

        if (added == 0) {
            return null;
        } else {
            return ways[App.rand.nextInt(added)];
        }
    }

    private final Point.Int[] tempPoints = new Point.Int[4];

    private boolean penetrateWall(Point.Int p) {
        fillAdajcentBlocks(tempPoints, p);
        for (int i = 0; i < tempPoints.length; i++) {

            if (tempPoints[i].equals(current)) {
                continue;
            }

            if (!isInRange(tempPoints[i]) || isRoad(tempPoints[i])) {
                return true;
            }

        }

        return false;
    }

    private boolean isInRange(Point.Int p) {
        if (p.x >= 0 && p.x < maze.length) {
            if (p.y >= 0 && p.y < maze[0].length) {
                return true;
            }
        }
        return false;

    }

    private boolean isRoad(Point.Int point) {
        return maze[point.x][point.y] == null;
    }

    private void fillAdajcentBlocks(Point.Int[] blocks, Point.Int p) {
        // Right
        blocks[0] = new Point.Int(p.x + 1, p.y);
        // Up
        blocks[1] = new Point.Int(p.x, p.y + 1);
        // Left
        blocks[2] = new Point.Int(p.x - 1, p.y);
        // Down
        blocks[3] = new Point.Int(p.x, p.y - 1);
    }

}
