package ca.utoronto.cs;

public enum SystemUser {
	ANONYMOUS("anonymous");

	private final String value;

	SystemUser(final String newValue) {
		value = newValue;
	}

	public String getValue() { return value; }
}
