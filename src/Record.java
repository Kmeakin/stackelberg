package comp34120.ex2;

import java.io.Serializable;

/**
 * Date structure for one record in the data file
 * 
 * @author Xin
 */
public final class Record implements Serializable {
	/* The date of the record */
	public final int m_date;
	/* The price of the leader */
	public final float m_leaderPrice;
	/* The price of the follower */
	public final float m_followerPrice;
	/* The cost of the follower */
	public final float m_cost;

	Record(final int p_date, final float p_leaderPrice, final float p_followerPrice, final float p_cost) {
		this.m_date = p_date;
		this.m_leaderPrice = p_leaderPrice;
		this.m_followerPrice = p_followerPrice;
		this.m_cost = p_cost;
	}

	public String toString() {
		return String.format("{date: %d, leader_price: %f, follower_price: %f, cost: %f}", this.m_date,
				this.m_leaderPrice, this.m_followerPrice, this.m_cost);
	}
}
