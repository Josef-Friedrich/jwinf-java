package rocks.friedrich.jwinf.platform.gui.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import ea.Vector;
import ea.actor.Image;
import ea.animation.Interpolator;
import ea.animation.ValueAnimator;
import ea.animation.interpolation.SinusFloat;
import rocks.friedrich.jwinf.platform.data.model.ItemData;
import rocks.friedrich.jwinf.platform.gui.State;
import rocks.friedrich.jwinf.platform.logic.Compass;
import rocks.friedrich.jwinf.platform.logic.map.Point;
import rocks.friedrich.jwinf.platform.logic.robot.Movement;
import rocks.friedrich.jwinf.platform.logic.robot.Robot;
import rocks.friedrich.jwinf.platform.logic.robot.VirtualRobot;

public class ImageRobot extends Image implements Robot
{
    private VirtualRobot virtual;

    /**
     * Damit keine neue Bewegung gestartet werden kann, bevor nicht die alte
     * fertig abgelaufen ist.
     */
    private boolean inMotion = false;

    protected float speed = 1f;

    /**
     * Behälter in dem Objekte eingesammelt (withdraw) werden können.
     *
     * https://github.com/France-ioi/bebras-modules/blob/ec1baf055c7f1c383ce8dfa5d27998463ef5be59/pemFioi/blocklyRobot_lib-1.1.js#L2458-L2478
     */
    public List<ItemData> bag = new ArrayList<>();

    public ImageRobot(String filepath, VirtualRobot virtual)
    {
        super(filepath, 1, 1);
        this.virtual = virtual;
    }

    public int getRow()
    {
        return virtual.getRow();
    }

    public int getCol()
    {
        return virtual.getCol();
    }

    public Point getPoint()
    {
        return virtual.getPoint();
    }

    public String[] reportRoute()
    {
        return virtual.reportRoute();
    }

    public void printRoute()
    {
        virtual.printRoute();
    }

    public boolean isOnExit()
    {
        var tile = virtual.map.get(getRow(), getCol());
        return tile != null && tile.isExit;
    }

    public boolean obstacleInFront()
    {
        return virtual.obstacleInFront();
    }

    protected void go(double meter)
    {
        if (inMotion)
        {
            return;
        }
        inMotion = true;
        Vector move = Vector.ofAngle(getRotation()).multiply((float) meter);
        Vector initial = getCenter();
        float duration = (float) meter / speed / 2;
        animate(duration, progress -> {
            setCenter(initial.add(move.multiply(progress)));
        });
        inMotion = false;
    }

    /**
     * Gehe einen Pixelmeter in Richtung der aktuellen Rotation.
     */
    public Movement forward()
    {
        var movement = virtual.forward();
        if (movement.relocated)
        {
            go(virtual.dir);
        }
        else
        {
            wiggle();
        }
        return movement;
    }

    public void go(Compass direction)
    {
        float degree = 90;
        switch (direction)
        {
        case EAST:
            degree = 0;
            break;

        case NORTH:
            degree = 90;
            break;

        case WEST:
            degree = 180;
            break;

        case SOUTH:
            degree = 270;
            break;

        default:
            System.out.println("Not supported direction");
        }
        rotateAnimated(degree);
        go(1);
    }

    public Movement east()
    {
        var movement = virtual.east();
        go(movement.to.dir);
        return movement;
    }

    public Movement south()
    {
        var movement = virtual.south();
        go(movement.to.dir);
        return movement;
    }

    public Movement west()
    {
        var movement = virtual.west();
        go(movement.to.dir);
        return movement;
    }

    public Movement north()
    {
        var movement = virtual.north();
        go(movement.to.dir);
        return movement;
    }

    protected void wait(double seconds)
    {
        try
        {
            Thread.sleep((long) (1000 * seconds));
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    public void wiggle()
    {
        this.wiggle(0.3f);
    }

    public void wiggle(float duration)
    {
        if (inMotion)
        {
            return;
        }
        inMotion = true;
        float rotation = getRotation();
        Vector center = getCenter();
        wait(0.1);
        animate(duration, progress -> {
            setRotation(rotation + progress);
            setCenter(center);
        }, new SinusFloat(0, 45));
        wait(0.1);
        inMotion = false;
    }

    public void rotateByAnimated(double degree)
    {
        if (inMotion)
        {
            return;
        }
        inMotion = true;
        Vector center = getCenter();
        // To avoid high rotation numbers
        float start = getRotation() % 360;
        setRotation(start);
        float duration = 1 / speed / 4;
        animate(duration, progress -> {
            setRotation(start + progress * (float) degree);
            setCenter(center);
        });
        inMotion = false;
    }

    public void rotateAnimated(double dest)
    {
        float src = getRotation();
        Vector center = getCenter();
        float diff = (float) dest - src;
        diff %= 360;
        if (diff == 0)
        {
            return;
        }
        // for example: dest 0 src 270
        if (diff < -180)
        {
            diff += 360;
        }
        if (diff > 180)
        {
            diff -= 360;
        }
        rotateByAnimated(diff);
        setCenter(center);
    }

    public ItemData dropObject(int itemNum)
    {
        ItemData item = virtual.dropObject(itemNum);
        return item;
    }

    /**
     * Drehe um 90 Grad nach links.
     */
    public Movement turnLeft()
    {
        var movement = virtual.turnLeft();
        rotateByAnimated(90);
        return movement;
    }

    /**
     * Drehe um 90 Grad nach rechts.
     */
    public Movement turnRight()
    {
        var movement = virtual.turnRight();
        rotateByAnimated(-90);
        return movement;
    }

    public Movement turnAround()
    {
        var movement = virtual.turnAround();
        rotateByAnimated(180);
        return movement;
    }

    protected void animate(float duration, Consumer<Float> setter,
            Interpolator<Float> interpolator)
    {
        CompletableFuture<Void> future = new CompletableFuture<>();
        ValueAnimator<Float> animator = new ValueAnimator<>(duration, setter,
                interpolator, this);
        animator.addCompletionListener(value -> {
            setter.accept(value);
            future.complete(null);
        });
        addFrameUpdateListener(animator);
        try
        {
            future.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected void animate(float duration, Consumer<Float> setter)
    {
        animate(duration, setter, State.interpolator);
    }

    public void placeInMap(int x, int y)
    {
        if (y > 0)
        {
            y = y * -1;
        }
        setCenter(x, y);
    }
}