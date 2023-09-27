package ru.swarm.mind.view.component;

import java.awt.*;
import java.awt.geom.Point2D;

import static ru.swarm.mind.view.component.Camera.Constants.MAX_SPEED;

public class Camera {


    public static final class Constants {
        public static final float MIN_SPEED = 0.01f;
        public static final float MAX_SPEED = 15f;
        public static final float SPEED_FAILING_PERCENT = 3.5f;
    }
    public static float xCam = 0,
            yCam = 0,
            zoom = 0.4f;
    public static Point2D speedVector = new Point2D.Float(0, 0);

    public static float getAngle(Point2D center, Point2D point) {
        float angle = (float) Math.toDegrees(Math.atan2(point.getY() - center.getY(), point.getX() - center.getX()));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }

    public static float toZero(float value, float take, float min) {
        var result = (value>0)?value-take:value+take;
        if ((result>0&&result<min)||(result<0&&result>(min*-1)))
            return 0;
        else return result;
    }
    public static Point2D calculatePointOnCircle(double centerX, double centerY, double radius, double degrees) {
        // Расчет координат точки
        double radians = Math.toRadians(degrees);
        double x = centerX + radius * Math.cos(radians);
        double y = centerY + radius * Math.sin(radians);

        // Создание объекта Point и возвращение его
        return new Point2D.Float((float) x, (float) y);
    }

    public static Point2D slowDownVector(Point2D vector, double slowdown) {
        // Расчет замедленного вектора
        float slowedX = (float) (vector.getX() * slowdown);
        float slowedY = (float) (vector.getY() * slowdown);

        // Создание объекта Point и возвращение его
        return new Point2D.Float(slowedX, slowedY);
    }
    public static Point2D createVector(Point2D start, Point2D end) {
        // Расчет вектора
        float vectorX = (float) (end.getX() - start.getX());
        float vectorY = (float) (end.getX() - start.getX());

        // Создание объекта Point и возвращение его
        return new Point2D.Float(vectorX, vectorY);
    }
    public static float slowGrowthAddition(float a, float b, float slowdown) {
        float result = a + b;
        if (result > a) {
            result = a + (result - a) * slowdown;
        }
        return result;
    }
    public static float calculateDirection(Point2D vector) {
        double x = vector.getX();
        double y = vector.getY();
        double angleRad = Math.atan2(y, x);
        float angleDeg = (float) Math.toDegrees(angleRad);
        return angleDeg;
    }
    public static float slowIncrease(float value, float max, float growthValue) {
        float range = max - value;
        float growthFactor = growthValue / (range + 1);
        float increment = (float) Math.ceil(growthFactor * range);
        return value + increment;
    }
    public static void compute() {
        float absSpeed = getAbsSpeed();
        float relation = getRelation();
        //if (relation==0) relation=1;

        float percent = getPercentOf(absSpeed);
        if (absSpeed> MAX_SPEED) percent*= MAX_SPEED/absSpeed/zoom;
        speedVector = slowDownVector(speedVector, 0.2759999f);
        float xS = (float) speedVector.getX();
        float yS = (float) speedVector.getY();
        //if (absSpeed<Constants.MIN_SPEED) absSpeed=0;
        //xS -= (absSpeed - percent * Constants.SPEED_FAILING_PERCENT);
        //yS -= (absSpeed - percent * Constants.SPEED_FAILING_PERCENT);

        //xS = toZero(xS, percent * Constants.SPEED_FAILING_PERCENT, 0.5f);

        //yS = yS+((xS<0)?-temp2:temp2);
        //yS = toZero(yS, percent * Constants.SPEED_FAILING_PERCENT, 0.5f);
        //yS = toZero(yS, percent * Constants.SPEED_FAILING_PERCENT, 0.5f);
        speedVector.setLocation(new Point2D.Float(xS, yS));
        xCam+= speedVector.getX();
        yCam+= speedVector.getY();
        //System.out.println(xS +" "+ yS);
    }

    private static float getPercentOf(float value) {
        return value/100;
    }

    private static float getRelation() {
        return (float) (Math.abs(speedVector.getX())/Math.abs(speedVector.getY()));
    }

    private static float getAbsSpeed() {
        return (float) (Math.abs(speedVector.getX())+Math.abs(speedVector.getY()));
    }

    @Deprecated
    public static void oldCompute() {
        if (speedVector.getX()<0.25f& speedVector.getX()>-0.25f) {
            speedVector.setLocation(0f, 0f);
        } else {
            if (speedVector.getX()<0) speedVector.setLocation(speedVector.getX()/1.15, speedVector.getY());
            if (speedVector.getX()>0) speedVector.setLocation(speedVector.getX()/1.15, speedVector.getY());
            if (speedVector.getY()<0) speedVector.setLocation(speedVector.getX(), speedVector.getY()/1.15);
            if (speedVector.getY()>0) speedVector.setLocation(speedVector.getX(), speedVector.getY()/1.15);
        }
        if (speedVector.getX()>10) speedVector.setLocation(9, speedVector.getY());
        if (speedVector.getX()<-10) speedVector.setLocation(-9, speedVector.getY());
        if (speedVector.getY()>10) speedVector.setLocation(speedVector.getX(), 9);
        if (speedVector.getY()<-10) speedVector.setLocation(speedVector.getX(), -9);
        xCam+= speedVector.getX();
        yCam+= speedVector.getY();
    }
}
