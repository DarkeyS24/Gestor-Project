package br.com.drky.gestor.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import br.com.drky.gestor.dto.CreateRequestClienteDTO;
import br.com.drky.gestor.dto.UpdateRequestClienteDTO;
import br.com.drky.gestor.exception.ClientNotFoundException;
import br.com.drky.gestor.exception.RegisteredClientException;
import br.com.drky.gestor.model.Cliente;
import br.com.drky.gestor.model.enums.TipoCliente;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class ClienteServiceTest {

	@Autowired
	private ClienteService service;

	@Test
	public void DeveriaRetornarTrueSeOCpfForValido() {
		Assertions.assertTrue(service.verificarCPF("712.296.412.46"));
	}

	@Test
	public void DeveriaRetornarFalseSeOCpfForInvalido() {
		Assertions.assertFalse(service.verificarCPF("99999999999"));
		Assertions.assertFalse(service.verificarCPF("999.999.999-99"));
	}

	@Test
	public void DeveriaRetornarTrueSeOCnpjForValido() {
		Assertions.assertTrue(service.verificarCNPJ("00.394.460/0058-87"));
	}

	@Test
	public void DeveriaRetornarFalseSeOCnpjForInvalido() {
		Assertions.assertFalse(service.verificarCNPJ("00.000.000/0000-0"));
		Assertions.assertFalse(service.verificarCNPJ("0000000000000"));
	}

	@Test
	public void DeveriaLancarUmaExceptionQuandoCpfJaEstiverCadastrado() {
		Cliente cliente = new Cliente("Angel Perez", TipoCliente.FISICO, "712.296.412-46", "(99)99999-9999",
				"angel@gmail.com");
		Cliente novoCliente = new Cliente("Jose Perez", TipoCliente.FISICO, "712.296.412-46", "(88)88888-8888",
				"jose@gmail.com");

		cliente.setCodigo(1);
		novoCliente.setCodigo(2);
		service.inserirCliente(CreateRequestClienteDTO.toDto(cliente));

		RegisteredClientException assertThrows = Assertions.assertThrows(RegisteredClientException.class, () -> {
			service.inserirCliente(CreateRequestClienteDTO.toDto(novoCliente));
		});

		Assertions.assertEquals(assertThrows.getMessage(), "CPF já cadastrado");
	}

	@Test
	public void DeveriaLancarUmaExceptionQuandoCnpjJaEstiverCadastrado() {

		service.deleteAll();

		Cliente cliente = new Cliente("Angel Perez", TipoCliente.JURIDICO, "00.394.460/0058-87", "(99)99999-9999",
				"angel@gmail.com");
		Cliente novoCliente = new Cliente("Jose Perez", TipoCliente.JURIDICO, "00.394.460/0058-87", "(88)88888-8888",
				"jose@gmail.com");

		cliente.setCodigo(3);
		novoCliente.setCodigo(4);
		service.inserirCliente(CreateRequestClienteDTO.toDto(cliente));

		RegisteredClientException assertThrows = Assertions.assertThrows(RegisteredClientException.class, () -> {
			service.inserirCliente(CreateRequestClienteDTO.toDto(novoCliente));
		});

		Assertions.assertEquals(assertThrows.getMessage(), "CNPJ já cadastrado");
	}

	@Test
	public void DeveriaLancarUmaExceptionQuandoClienteNãoCadastrado() {
		Cliente cliente = new Cliente("Candelaria Henriquez", TipoCliente.FISICO, "000.000.000-00", "(99)99999-9999",
				"nena@gmail.com");

		Integer id = 5;

		ClientNotFoundException assertThrows = Assertions.assertThrows(ClientNotFoundException.class, () -> {
			service.atualizarCliente(id, new UpdateRequestClienteDTO(cliente.getTelefone(), cliente.getEmail()));
		});

		Assertions.assertEquals(assertThrows.getMessage(), "Cliente não encontrado");
	}

	@Test
	public void DeveriaLancarUmaExceptionQuandoClientePesquisadoPorIdNãoCadastrado() {

		service.deleteAll();

		Cliente cliente = new Cliente("Luis Enrique", TipoCliente.FISICO, "712.296.092-70", "(99)99999-9999",
				"luis@gmail.com");

		cliente.setCodigo(5);
		service.inserirCliente(CreateRequestClienteDTO.toDto(cliente));

		Integer id = 6;

		ClientNotFoundException assertThrows = Assertions.assertThrows(ClientNotFoundException.class, () -> {
			service.buscaClientePorId(id);
		});

		Assertions.assertEquals(assertThrows.getMessage(), "Cliente não encontrado");
	}

	@Test
	public void DeveriaRetornarOClientePesquisadoPorIdQuandoCadastrado() {

		service.deleteAll();

		Cliente cliente = new Cliente("Luis Enrique", TipoCliente.FISICO, "712.296.092-70", "(99)99999-9999",
				"luis@gmail.com");

		Integer id = 5;
		cliente.setCodigo(id);
		service.inserirCliente(CreateRequestClienteDTO.toDto(cliente));

		Assertions.assertEquals(cliente.getCodigo(), service.buscaClientePorId(id).getCodigo());
	}

	@Test
	public void DeveriaRetornarUmaListaClientes() {

		service.deleteAll();

		Cliente cliente = new Cliente("Angel Perez", TipoCliente.JURIDICO, "00.394.460/0058-87", "(99)99999-9999",
				"angel@gmail.com");
		Cliente novoCliente = new Cliente("Jose Perez", TipoCliente.FISICO, "712.296.412-46", "(88)88888-8888",
				"jose@gmail.com");

		cliente.setCodigo(1);
		novoCliente.setCodigo(2);

		service.inserirCliente(CreateRequestClienteDTO.toDto(cliente));
		service.inserirCliente(CreateRequestClienteDTO.toDto(novoCliente));

		List<Cliente> clientes = service.BuscaTodosOsClientes();
		if (clientes.size() > 0) {
			for (int i = 0; i < clientes.size(); i++) {
				System.out.println(clientes.get(i).getNome());
				Assertions.assertEquals(i + 1, clientes.get(i).getCodigo());
			}
		}
	}

	@Test
	public void DeveriaLancarUmaExceptionQuandoClienteExcluidoPorIdNãoCadastrado() {

		Integer id = 100;

		ClientNotFoundException assertThrows = Assertions.assertThrows(ClientNotFoundException.class, () -> {
			service.excluirClientePorId(id);
		});

		Assertions.assertEquals(assertThrows.getMessage(), "Cliente não encontrado");
	}
}