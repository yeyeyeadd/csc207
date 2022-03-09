package ca.utoronto.cs.exception;

public class ObjectNotFoundException extends InternalExceptionBase {
	public ObjectNotFoundException(String name) {
		super(name);
	}
}
