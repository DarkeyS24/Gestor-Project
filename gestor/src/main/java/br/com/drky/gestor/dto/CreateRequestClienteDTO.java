package br.com.drky.gestor.dto;

import br.com.drky.gestor.model.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateRequestClienteDTO(@NotBlank String codigo, @NotBlank String nome,
		@NotBlank @Pattern(regexp = "FISICO|JURIDICO|fisico|juridico", message = "Tipo de cliente invalido") String tipo,
		@NotBlank String cpfCnpj, @NotBlank String telefone, String email, String sincronizado) {

	public static CreateRequestClienteDTO toDto(Cliente cliente) {
		CreateRequestClienteDTO dto = new CreateRequestClienteDTO(cliente.getCodigo().toString(), cliente.getNome(),
				cliente.getTipo().toString(), cliente.getCpfCnpj(), cliente.getTelefone(),
				(cliente.getEmail() == null ? null : cliente.getEmail()), cliente.getSincronizado().toString());

		return dto;
	}
}