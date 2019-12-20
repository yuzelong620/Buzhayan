package com.kratos.game.herphone.box.until;

public  class RandomItem<E> {
	public RandomItem(int odds, E value) {
		this.odds = odds;
		this.value = value;
	}

	int odds;
	E value;

	public int getOdds() {
		return odds;
	}

	public void setOdds(int odds) {
		this.odds = odds;
	}

	public E getValue() {
		return value;
	}

	public void setValue(E value) {
		this.value = value;
	}
}
