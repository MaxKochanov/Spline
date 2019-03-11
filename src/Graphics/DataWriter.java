package Graphics;

import spline.Spline;

import java.io.FileWriter;

public class DataWriter {

    public void points(double[] x, double[] y) {
        try {
            FileWriter file = new FileWriter("E:\\Java_projects\\Spline\\src\\graphics\\dataPoints");
            for (int i = 0; i < Spline.n + 1; i++) {
                file.write(x[i] + "                           " + y[i] + "\n");
                file.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spline(double[] x, double[] y) {
        try {
            FileWriter file = new FileWriter("E:\\Java_projects\\Spline\\src\\graphics\\dataSpline");
            for (int i = 0; i < Spline.pointsAmount; i++) {
                file.write(x[i] + "                           " + y[i] + "\n");
                file.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void errorSpline(double[] x, double[] y) {
        try {
            FileWriter file = new FileWriter("E:\\Java_projects\\Spline\\src\\graphics\\dataError");
            for (int i = 0; i < Spline.pointsAmount; i++) {
                file.write(x[i] + "                           " + 20 * Math.abs((Spline.testFunction(x[i]) - y[i])) + "\n");
                file.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
