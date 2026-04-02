package br.com.drky.gestor.exception;

public class InvalidClientDocumentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidClientDocumentException(String msg) {
		super(msg);
	}
}