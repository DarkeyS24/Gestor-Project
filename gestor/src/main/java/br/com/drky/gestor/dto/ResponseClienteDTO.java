package br.com.drky.gestor.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.drky.gestor.model.Cliente;

public record ResponseClienteDTO(String codigo, String nome, String tipo, String cpfCnpj, String telefone,
		String email) {

	public static ResponseClienteDTO toDto(Cliente cliente) {
		ResponseClienteDTO dto = new ResponseClienteDTO(cliente.getCodigo().toString(), cliente.getNome(),
				cliente.getTipo().toString(), cliente.getCpfCnpj(), cliente.getTelefone(), cliente.getEmail());

		return dto;
	}

	public static List<ResponseClienteDTO> toDtoList(List<Cliente> clientes) {
		List<ResponseClienteDTO> list = new ArrayList<>();

		for (Cliente c : clientes) {
			ResponseClienteDTO dto = new ResponseClienteDTO(c.getCodigo().toString(), c.getNome(),
					c.getTipo().toString(), c.getCpfCnpj(), c.getTelefone(), c.getEmail());
			list.add(dto);
		}

		return list;
	}
}