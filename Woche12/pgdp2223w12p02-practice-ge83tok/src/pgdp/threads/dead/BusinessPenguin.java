package pgdp.threads.dead;

public class BusinessPenguin {
	private String name;
	private int balance;
	private BusinessPenguin partner;

	public BusinessPenguin(String name) {
		this.name = name;
		this.balance = 0;
	}

	public void setPartner(BusinessPenguin partner) {
		this.partner = partner;
	}

	public String getName() {
		return name;
	}

	public int getBalance() {
		return balance;
	}

	public synchronized void sellFish(int price) {
		balance += price / 2;
		partner.addMoney(price / 2);
	}

	public synchronized void addMoney(int amount) {
		balance += amount;
	}
}