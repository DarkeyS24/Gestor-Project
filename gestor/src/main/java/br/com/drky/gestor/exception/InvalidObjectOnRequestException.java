package br.com.drky.gestor.exception;

public class InvalidObjectOnRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidObjectOnRequestException(String msg) {
		super(msg);
	}
}