package main;

import Graphics.*;
import spline.Spline;


import static spline.Spline.*;

public class Main {

    public static void main(String[] args) {
        Spline spline = new Spline();
        DataWriter dataWriter = new DataWriter();
        dataWriter.points(x, y);

        double[] z = new double[pointsAmount];
        for (int i = 0; i < pointsAmount; i++) {
            z[i] = i /(double)35;
        }
        double[] value = new double[pointsAmount];
        double leftCondition = spline.getLeftCondition(x[0], x[1], x[2]);
        double rightCondition = spline.getRightCondition(x[n-3], x[n-2], x[n-1], x[n], x[n]);

        double[] gamma = spline.findParameters(-testFunction(x[0]), -testFunction(x[n]));
        for (int i = 0; i < pointsAmount; i++) {
            for (int j = 0; j < n; j++) {
                if (z[i] >= x[j] && z[i] <= x[j + 1]) {
                    value[i] = spline.value(j, z[i], gamma);
                    break;
                }
            }
        }
        dataWriter.spline(z, value);
        dataWriter.errorSpline(z, value);

    }
}
