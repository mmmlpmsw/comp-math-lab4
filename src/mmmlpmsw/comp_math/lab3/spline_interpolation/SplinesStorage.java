package mmmlpmsw.comp_math.lab3.spline_interpolation;

import mmmlpmsw.comp_math.lab3.Function;

public class SplinesStorage {
    private Spline[] splines;

    public SplinesStorage(double[] x, double[] y) {
        initSplines(x, y);
    }

    public void initSplines(double[] x, double[] y) {
        int n = x.length;
        splines = new Spline[n];
        for (int i = 0; i < n; ++i) {
            splines[i] = new Spline();
        }
        for (int i = 0; i < n; ++i) {
            splines[i].setX(x[i]);
            splines[i].setA(y[i]);
        }
        splines[0].setC(0d);
        solveByTridiagonalMatrixAlgorithm(x, y, n);
    }

    private void solveByTridiagonalMatrixAlgorithm(double[] x, double[] y, int n) {
        double[] alpha = new double[n - 1];
        double[] beta = new double[n - 1];
        alpha[0] = 0;
        beta[0] = 0;

        double hi, hi_inc, A = 0, B, C = 0, F = 0;

        for (int i = 1; i < n - 1; ++i) {
            hi = x[i] - x[i - 1];
            A = hi;
            hi_inc = x[i + 1] - x[i];
            B = hi_inc;
            C = (hi_inc + hi) * 2;
            F = 6 * ((y[i + 1] - y[i])/hi_inc - (y[i] - y[i - 1])/hi);
            double t = (A * alpha[i - 1] + C);

            alpha[i] = -B / t;
            beta[i] = (F - A * beta[i - 1]) / t;
        }
        splines[n - 1].setC((F - A * beta[n - 2])/(C + A * alpha[n - 2]));

        for (int i = n - 2; i > 0; --i)
            splines[i].setC(alpha[i] * splines[i + 1].getC() + beta[i]);

        for (int i = n - 1; i > 0; --i) {
            hi = x[i] - x[i -1];
            splines[i].setD((splines[i].getC() - splines[i - 1].getC()) / hi);
            splines[i].setB((hi * (2 * splines[i].getC() + splines[i - 1].getC()) / 6) + (y[i] - y[i - 1]) / hi);
        }
    }

    public Function interpolate () {
        return new Function() {
            @Override
            public double getValue(double arg) {
                return getInterpolatedY(arg);
            }
        };
    }

    private double getInterpolatedY(double x) {
        Spline spline;
        if (x >= splines[splines.length - 1].getX())
            spline = splines[splines.length - 1];
        else if (x <= splines[0].getX())
            spline = splines[0];
        else {
            int k, left = 0, right = splines.length - 1;
            while (right > left + 1) {
                k = left + (right - left) / 2;
                if (x <= splines[k].getX())
                    right = k;
                else
                    left = k;
            }
            spline = splines[right];
        }
        double dx = x - spline.getX();
        double value = spline.getA() + spline.getB() * dx
                + spline.getC() * dx * dx/2 + spline.getD()* dx * dx * dx/6 ;
        return value;
    }


    private class Spline {
        private double a, b, c, d, x;

        public double getA() {
            return a;
        }

        public void setA(double a) {
            this.a = a;
        }

        public double getB() {
            return b;
        }

        public void setB(double b) {
            this.b = b;
        }

        public double getC() {
            return c;
        }

        public void setC(double c) {
            this.c = c;
        }

        public double getD() {
            return d;
        }

        public void setD(double d) {
            this.d = d;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }
    }
}
