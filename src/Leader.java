import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import comp34120.ex2.PlayerImpl;
import comp34120.ex2.PlayerType;
import comp34120.ex2.Record;

class Vec2 {
    float x;
    float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("[%f %f]", this.x, this.y);
    }

    public boolean equals(Vec2 other) {
        return this.x == other.x && this.y == other.y;
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    public Vec2 add(float other) {
        return new Vec2(this.x + other, this.y + other);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    public Vec2 sub(float other) {
        return new Vec2(this.x - other, this.y - other);
    }

    public Vec2 mul(float other) {
        return new Vec2(this.x * other, this.y * other);
    }

    public Vec2 div(float other) {
        return new Vec2(this.x / other, this.y / other);
    }
}

class Regression {
    float p;
    float lambda;
    Vec2 theta;

    public Regression(float[] xs, float[] ys, float lambda) {
        assert (xs.length == ys.length);
        this.lambda = lambda;
        /* Calculate initial params */
        int T0 = xs.length;

        this.p = 0.0f;
        for (int t = 0; t < T0; t++) {
            this.p += Math.pow(this.lambda, T0 - t - 1) * (1 + Math.pow(xs[t], 2));
        }

        this.theta = new Vec2(0.0f, 0.0f);
        this.theta.x = (float) Math.pow(this.p, -1.0f);
        this.theta.y = (float) Math.pow(this.p, -1.0f);
        for (int t = 0; t < T0; t++) {
            // phi(t) = [1, xt]
            double l = Math.pow(this.lambda, T0 - t - 1);
            this.theta.x += l * ys[t];
            this.theta.y += l * xs[t] * ys[t];
        }
        double pt0_inv = Math.pow(this.p, -1);
        this.theta.x *= pt0_inv;
        this.theta.y *= pt0_inv;
    }

    private float next_denominator(float next_x) {
        return this.lambda + (this.p + this.p * next_x * next_x);
    }

    private Vec2 next_l(float new_x) {
        // [1, next_x]
        Vec2 phi = new Vec2(1, new_x);
        return phi.mul(this.p).div(this.next_denominator(new_x));
    }

    private float next_p(float new_x) {
        float top, bottom;
        top = this.p * (1 + new_x * new_x) * this.p;
        bottom = this.next_denominator(new_x);
        return (1 / this.lambda) * (top / bottom);
    }

    public void update(float new_x, float new_y) {
        float error = new_y - (this.theta.x + this.theta.y * new_x);
        // System.out.printf("[update] error = %f\n", error);
        // System.out.printf("[update] theta = %s\n", this.theta);
        // System.out.printf("[update] new_l = %s\n", this.next_l(new_x));
        this.theta = this.theta.add(this.next_l(new_x).mul(error));
        // System.out.printf("[update] theta = %s\n", this.theta);
        this.p = this.next_p(new_x);
    }

    public float pick_leader_price() {
        float top, bottom;
        // System.out.printf("[pick_leader_price] theta = %s\n", this.theta);

        top = 3 * (this.theta.x - this.theta.y + 10);
        // System.out.printf("[pick_leader_price] top = %f\n", top);

        bottom = 2 * (3 * this.theta.y - 10);
        // System.out.printf("[pick_leader_price] bottom = %f\n", bottom);
        return -(top / bottom);
    }

    public float predict_follower_price(float leader_price) {
        return this.theta.x + this.theta.y * leader_price;
    }
}

class LeaderTest {
    private static void test_simple_model_from_hw() {
        float[] xs = new float[] { 1.0f, 2.0f, 3.0f };
        float[] ys = new float[] { 1.0f, 3.0f, 4.0f };

        Regression r = new Regression(xs, ys, 0.99f);
        System.out.println(r.theta);
    }

    public static void main(String[] argv) {
        test_simple_model_from_hw();
    }
}

public class Leader extends PlayerImpl {
    private String record_to_string(Record record) {
        return String.format("{date = %d, leader_price = %f, follower_price = %f}", record.m_date, record.m_leaderPrice,
                record.m_followerPrice);
    }

    static final int N_DAYS = 60;
    static float LAMBDA = 0.95f;
    Regression r;

    protected Leader() throws RemoteException, NotBoundException {
        super(PlayerType.LEADER, "Leader");
    }

    public static void main(String[] argv) throws RemoteException, NotBoundException {
        new Leader();
    }

    @Override
    public void startSimulation(int steps) throws RemoteException {
        float[] xs = new float[N_DAYS];
        float[] ys = new float[N_DAYS];

        for (int t = 0; t < N_DAYS - 0; t++) {
            Record record = this.m_platformStub.query(PlayerType.LEADER, t + 1);
            xs[t] = record.m_leaderPrice;
            ys[t] = record.m_followerPrice;
        }

        this.r = new Regression(xs, ys, LAMBDA);
    }

    @Override
    public void endSimulation() throws RemoteException {
        Record record;
        int i = 1;
        while ((record = this.m_platformStub.query(PlayerType.LEADER, i++)) != null) {
            System.out.printf("%s\n", record_to_string(record));
        }
    }

    @Override
    public void proceedNewDay(int date) throws RemoteException {
        Record previous_day = this.m_platformStub.query(PlayerType.LEADER, date - 1);
        this.r.update(previous_day.m_leaderPrice, previous_day.m_followerPrice);
        float our_price = this.r.pick_leader_price();
        this.m_platformStub.publishPrice(this.m_type, our_price);
    }

    @Override
    public void goodbye() {
        System.exit(0);
    }
}
