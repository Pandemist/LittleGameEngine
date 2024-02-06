package lang;

public enum Languages {
	GERMAN("DE"),
	FRENCH("FR"),
	ENGLISH_US("EN"),
	JAPANESE("JP");

	public final String label;

	private Languages(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return this.label;
	}

	public static Languages getLangByValue(String s) {
		for (Languages l : Languages.values()) {
			if (l.label == s) {
				return l;
			}
		}
		return GERMAN;
	}
}
