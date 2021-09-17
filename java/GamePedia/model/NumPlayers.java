package GamePedia.model;

public class NumPlayers {
	private int numPlayersId;
	private NumberName numberName;
	
	public enum NumberName {
		MultiPlayer, SinglePlayer
	}

	public NumPlayers(int numPlayersId, NumberName numberName) {
		super();
		this.numPlayersId = numPlayersId;
		this.numberName = numberName;
	}

	public NumPlayers(int numPlayersId) {
		super();
		this.numPlayersId = numPlayersId;
	}

	public int getNumPlayersId() {
		return numPlayersId;
	}

	public void setNumPlayersId(int numPlayersId) {
		this.numPlayersId = numPlayersId;
	}

	public NumberName getNumberName() {
		return numberName;
	}

	public void setNumberName(NumberName numberName) {
		this.numberName = numberName;
	}
}
