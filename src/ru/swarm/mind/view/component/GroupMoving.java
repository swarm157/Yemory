package ru.swarm.mind.view.component;

import ru.swarm.mind.model.Memory;

import java.awt.geom.Point2D;

public class GroupMoving {
    public static final class Constants {
        private static final float MIN_SPEED = 0.01f;
        private static final float MAX_SPEED = 15f;
        private static final float SPEED_FAILING_PERCENT = 3.5f;
    }
    public static float xCam = 0,
            yCam = 0,
            zoom = 0.4f;

    private static float getPercentOf(float value) {
        return value/100;
    }

    private static float getRelation() {
        return (float) (Math.abs(speedVector.getX())/Math.abs(speedVector.getY()));
    }

    private static float getAbsSpeed() {
        return (float) (Math.abs(speedVector.getX())+Math.abs(speedVector.getY()));
    }

    public static Point2D speedVector = new Point2D.Float(0, 0);
    public static void compute() {
        float absSpeed = getAbsSpeed();
        float relation = getRelation();
        //if (relation==0) relation=1;

        float percent = getPercentOf(absSpeed);
        if (absSpeed> Constants.MAX_SPEED) percent*= Constants.MAX_SPEED/absSpeed/zoom;
        float xS = (float) speedVector.getX();
        float yS = (float) speedVector.getY();
        //if (absSpeed<Constants.MIN_SPEED) absSpeed=0;
        //xS -= (absSpeed - percent * Constants.SPEED_FAILING_PERCENT);
        //yS -= (absSpeed - percent * Constants.SPEED_FAILING_PERCENT);
        xS = toZero(xS, percent * Constants.SPEED_FAILING_PERCENT, 0.5f);
        yS = toZero(yS, percent * Constants.SPEED_FAILING_PERCENT, 0.5f);
        speedVector.setLocation(new Point2D.Float(xS, yS));
        for (Memory memory : DrawPanel.Interact.groupSelection) {
            memory.setPoint(new Point2D.Float((float) (memory.getPoint().getX()+speedVector.getX()), (float) (memory.getPoint().getY()+speedVector.getY())));
        }
        //xCam+= speedVector.getX();
        //yCam+= speedVector.getY();
    }

    public static float toZero(float value, float take, float min) {
        var result = (value>0)?value-take:value+take;
        if ((result>0&&result<min)||(result<0&&result>(min*-1)))
            return 0;
        else return result;
    }
}
