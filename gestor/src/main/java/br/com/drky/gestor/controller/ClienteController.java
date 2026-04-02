package br.com.drky.gestor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.drky.gestor.dto.CreateRequestClienteDTO;
import br.com.drky.gestor.dto.ResponseClienteDTO;
import br.com.drky.gestor.dto.UpdateRequestClienteDTO;
import br.com.drky.gestor.model.Cliente;
import br.com.drky.gestor.service.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("clientes")
public class ClienteController {

	@Autowired
	private ClienteService service;

	@GetMapping
	@Cacheable(value = "todosOsClientes")
	public ResponseEntity<List<ResponseClienteDTO>> BuscaTodosOsClientes() {
		return ResponseEntity.ok(ResponseClienteDTO.toDtoList(service.BuscaTodosOsClientes()));
	}

	@GetMapping("/findById/{id}")
	@Cacheable(value = "todosOsClientes")
	public ResponseEntity<ResponseClienteDTO> buscaClientePorId(@PathVariable("id") Integer id) {
		Cliente cliente = service.buscaClientePorId(id);
		return ResponseEntity.ok(ResponseClienteDTO.toDto(cliente));
	}

	@PostMapping
	@Transactional
	@CacheEvict(value = "todosOsClientes")
	public ResponseEntity<?> inserirCliente(@RequestBody @Valid CreateRequestClienteDTO dto) {
		service.inserirCliente(dto);
		return ResponseEntity.ok("Cliente criado com sucesso!!");
	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "todosOsClientes")
	public ResponseEntity<?> atualizarCliente(@PathVariable("id") Integer id,
			@RequestBody UpdateRequestClienteDTO dto) {
		service.atualizarCliente(id, dto);
		return ResponseEntity.ok("Cliente atualizado com sucesso!!");
	}

	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "todosOsClientes")
	public ResponseEntity<?> excluirClientePorId(@PathVariable("id") Integer id) {
		service.excluirClientePorId(id);
		return ResponseEntity.ok("Cliente excluido com sucesso!!");
	}
}