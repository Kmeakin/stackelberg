import comp34120.ex2.PlayerImpl;
import comp34120.ex2.PlayerType;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * A pseudo leader. The members m_platformStub and m_type are declared in the
 * PlayerImpl, and feel free to use them. You may want to check the
 * implementation of the PlayerImpl. You will use m_platformStub to access the
 * platform by calling the remote method provided by it.
 * 
 * @author Xin
 */
final class PseudoLeader extends PlayerImpl {
	PseudoLeader() throws RemoteException, NotBoundException {
		super(PlayerType.LEADER, "Pseudo Leader");
	}

	@Override
	public void startSimulation(int steps) throws RemoteException {
		super.startSimulation(steps);

	}

	@Override
	public void endSimulation() throws RemoteException {
		super.endSimulation();
	}

	@Override
	public void proceedNewDay(int date) throws RemoteException {
		/*
		 * Check for new price Record l_newRecord = m_platformStub.query(m_type, date);
		 *
		 * Your own math model to compute the price here ... float l_newPrice = ....
		 *
		 * Submit your new price, and end your phase m_platformStub.publishPrice(m_type,
		 * l_newPrice);
		 */
	}
}
