package pl.pwpw.playground.service;

public class FileException extends RuntimeException {

	public FileException(String message) {
		super(message);
	}

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}

	public static FileException causedBy(final String message, final Throwable throwable) {

		return new FileException(message, throwable);
	}
}
