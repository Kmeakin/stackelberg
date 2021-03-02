import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import comp34120.ex2.PlayerImpl;
import comp34120.ex2.PlayerType;
import comp34120.ex2.Record;

public class Leader extends PlayerImpl {
    static final int N_DAYS = 60;
    Record[] historic_data = new Record[N_DAYS];

    protected Leader() throws RemoteException, NotBoundException {
        super(PlayerType.LEADER, "Leader");
    }

    public static void main(String[] argv) throws RemoteException, NotBoundException {
        new Leader();
    }

    @Override
    public void startSimulation(int steps) throws RemoteException {
        for (int i = 0; i < N_DAYS; i++) {
            Record record = this.m_platformStub.query(PlayerType.LEADER, i + 1);
            System.out.printf("%d: %s\n", i, record.toString());
            this.historic_data[i] = record;
        }
    }

    @Override
    public void proceedNewDay(int date) throws RemoteException {
        this.m_platformStub.publishPrice(this.m_type, 0.0f);
    }

    @Override
    public void goodbye() {
        System.exit(0);
    }
}
