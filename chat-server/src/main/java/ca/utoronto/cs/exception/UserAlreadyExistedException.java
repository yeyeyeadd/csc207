package ca.utoronto.cs.exception;

public class UserAlreadyExistedException extends InternalExceptionBase {
	public UserAlreadyExistedException(String username) {
		super(username);
	}
}
