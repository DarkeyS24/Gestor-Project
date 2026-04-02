package br.com.drky.gestor.exception;

public class RegisteredClientException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegisteredClientException(String msg) {
		super(msg);
	}
}