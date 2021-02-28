import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Leader extends PlayerImpl {
    protected Leader() throws RemoteException, NotBoundException {
        super(PlayerType.LEADER, "Leader");
    }

    public static void main(String[] argv) throws RemoteException, NotBoundException {
        new Leader();
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
