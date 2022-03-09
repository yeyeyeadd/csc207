package ca.utoronto.cs.exception;


public class GenericErrorException extends ResponseExceptionBase {
	public GenericErrorException(String msg) {
		super(msg);
	}
}
