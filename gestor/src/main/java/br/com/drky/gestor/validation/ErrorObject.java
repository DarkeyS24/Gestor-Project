package br.com.drky.gestor.validation;

public class ErrorObject {

	String field;
	String message;

	public ErrorObject(String field, String message) {
		this.field = field;
		this.message = message;
	}

	public ErrorObject() {
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}