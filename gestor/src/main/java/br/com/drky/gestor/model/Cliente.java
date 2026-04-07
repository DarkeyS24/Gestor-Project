package br.com.drky.gestor.model;

import br.com.drky.gestor.model.enums.TipoCliente;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TBCLIENTE")
public class Cliente {

	@Id
	private Integer codigo;
	private String nome;
	private TipoCliente tipo;
	private String cpfCnpj;
	private String telefone;
	private Boolean sincronizado;
	private Boolean excluido;

	@Nullable
	private String email;

	public Cliente(String nome, TipoCliente tipo, String cpf_cnpj, String telefone, String email) {
		this.nome = nome;
		this.tipo = tipo;
		this.cpfCnpj = cpf_cnpj;
		this.telefone = telefone;
		this.email = email;
		this.sincronizado = false;
		this.excluido = false;
	}

	public Cliente() {
	}

	public Boolean getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(Boolean sincronizado) {
		this.sincronizado = sincronizado;
	}

	public Boolean getExcluido() {
		return excluido;
	}

	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoCliente getTipo() {
		return tipo;
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}