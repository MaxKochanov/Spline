package spline;

import java.util.Random;

public class Spline {

    public static int n = 60; // Количество узлов
    public static int pointsAmount = 350; // Количество точек для графика
    public static double[] x = new double[n + 1];
    public static double[] y = new double[n + 1];

    public static double testFunction(double x) {
        return Math.sin(x);
    }

    private void initNodes() {
        Random random = new Random();
        for (int i = 1; i < n; i++) {
            double eps = random.nextInt(10);
            x[i] = i / (double) 6 + eps / 25;
            y[i] = testFunction(x[i]);
        }
        x[0] = 0;
        x[n] = 10;
        y[0] = testFunction(x[0]);
        y[n] = testFunction(x[n]);
    }

    public Spline() {
        initNodes();
    }

    private double[][] createMatrix() {
        double[] h = new double[n];
        for (int i = 0; i < n - 1; i++) {
            h[i] = x[i + 1] - x[i];
        }
        double[][] matrix = new double[n - 1][n - 1];
        for (int i = 0; i < n - 1; i++) {
            matrix[i] = new double[n - 1];
        }
        matrix[0][0] = 1 / (double) 3 * (h[0] + h[1]);
        matrix[0][1] = 1 / (double) 6 * h[1];
        for (int i = 1; i < n - 2; i++) {
            for (int j = 1; j < n - 2; j++) {
                matrix[i][i] = 1 / (double) 3 * (h[i] + h[i + 1]);
                matrix[i][i - 1] = 1 / (double) 6 * h[i];
                matrix[i][i + 1] = 1 / (double) 6 * h[i + 1];
            }
        }
        matrix[n - 2][n - 2] = 1 / (double) 3 * (h[n - 2] + h[n - 1]);
        matrix[n - 2][n - 3] = 1 / (double) 6 * h[n - 2];
        return matrix;
    }

    private double[] createConstants(double gamma0, double gammaN) {
        double[] constantsTmp = new double[n];
        constantsTmp[0] = 0; // костыль чтобы за границу массива не уходило
        for (int i = 1; i < n; i++) {
            constantsTmp[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]) - (y[i] - y[i - 1]) / (x[i] - x[i - 1]);
            if (i == 1) constantsTmp[i] -= gamma0 * (x[i] - x[i - 1]) / 6;
            if (i == n - 1) constantsTmp[i] -= gammaN * (x[i + 1] - x[i]) / 6;
        }
        double[] constants = new double[n - 1];
        for (int i = 0; i < n - 1; i++) {
            constants[i] = constantsTmp[i + 1];
        }
        return constants;
    }

    private double[] Thomas(double[][] matrix, double[] constants) { // works when matrix is three-diagonal, Метод прогонки
        int size = n - 1;
        double[] csi = new double[size + 1];
        double[] eta = new double[size + 1];
        double[] x = new double[size + 1];
        double[] a = new double[size];
        double[] b = new double[size];
        double[] c = new double[size];

        for (int i = 1; i < size; i++) {
            a[i] = matrix[i][i - 1];
        }

        for (int i = 0; i < size; i++) {
            b[i] = matrix[i][i];
        }

        for (int i = 0; i < size - 1; i++) {
            c[i] = matrix[i][i + 1];
        }

        for (int i = 1; i < size + 1; i++) {
            csi[i] = -c[i - 1] / (a[i - 1] * csi[i - 1] + b[i - 1]);
        }
        for (int i = 1; i < size + 1; i++) {
            eta[i] = (constants[i - 1] - a[i - 1] * eta[i - 1]) / (a[i - 1] * csi[i - 1] + b[i - 1]);
        }
        for (int i = size - 1; i >= 0; i--) {
            x[i] = csi[i + 1] * x[i + 1] + eta[i + 1];
        }

        double[] y = new double[n - 1];
        for (int i = 0; i < n - 1; i++) {
            y[i] = x[i];
        }
        return y;
    }

    public double[] findParameters(double gamma0, double gammaN) {
        double[][] matrix = createMatrix();
        double[] constants = createConstants(gamma0, gammaN); // Вообще говоря, должны быть вторые производные
        double[] gammaTmp = Thomas(matrix, constants);
        double[] gamma = new double[n + 1];
        gamma[0] = gamma0;
        gamma[n] = gammaN;
        for (int j = 1; j < n; j++) {
            gamma[j] = gammaTmp[j - 1];
        }
        return gamma;
    }


    public double value(int i, double z, double[] gamma) { // i = 0, 1, 2, ..., n-1
        return y[i] * (x[i + 1] - z) / (x[i + 1] - x[i]) + y[i + 1] * (z - x[i]) / (x[i + 1] - x[i])
                + gamma[i] * (Math.pow((x[i + 1] - z), 3) - Math.pow((x[i + 1] - x[i]), 2) * (x[i + 1] - z)) /
                (6 * (x[i + 1] - x[i])) + gamma[i + 1] * (Math.pow((z - x[i]), 3) - Math.pow((x[i + 1] - x[i]), 2) * (z - x[i])) /
                (6 * (x[i + 1] - x[i]));
    }

}
