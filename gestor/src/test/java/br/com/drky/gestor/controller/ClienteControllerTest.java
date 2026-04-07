package br.com.drky.gestor.controller;

import java.net.URI;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.drky.gestor.dto.CreateRequestClienteDTO;
import br.com.drky.gestor.dto.ResponseClienteDTO;
import br.com.drky.gestor.dto.UpdateRequestClienteDTO;
import br.com.drky.gestor.model.Cliente;
import br.com.drky.gestor.model.enums.TipoCliente;
import br.com.drky.gestor.service.ClienteService;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClienteControllerTest {

	@Autowired
	private MockMvc mock;

	@Autowired
	private ClienteService service;

	@Autowired
	private ObjectMapper om;

	@Test
	@Order(1)
	public void DeveRetornarStatus200QuandoChamadaAListaDeClientes() throws Exception {

		URI uri = new URI("/clientes");

		mock.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().is(200));
	}

	@Test
	@Order(2)
	public void DeveRetornarStatus200QuandoPesquisadoUmClientePorId() throws Exception {

		Cliente cliente = new Cliente("Angel Perez", TipoCliente.FISICO, "712.296.412-46", "(99)99999-9999",
				"angel@gmail.com");

		cliente.setCodigo(1);
		service.inserirCliente(CreateRequestClienteDTO.toDto(cliente));

		URI uri = new URI("/clientes/findById/1");

		String json = mock.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn().getResponse().getContentAsString();
		ResponseClienteDTO dto = om.readValue(json, ResponseClienteDTO.class);

		Assertions.assertEquals(dto.nome(), cliente.getNome());
	}

	@Test
	@Order(3)
	public void DeveRetornarStatus400QuandoDadosDoIdDoClienteEstiveremInvalidos() throws Exception {

		//		Cliente cliente = new Cliente("Angel Perez", TipoPessoa.FISICA, "712.296.412-46", "(99)99999-9999",
		//				"angel@gmail.com");

		URI uri = new URI("/clientes/findById/r");

		mock.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	@Order(4)
	public void DeveRetornarStatus404QuandoPesquisadoUmClientePorIdNãoEstiverCadastrado() throws Exception {

		//		Cliente cliente = new Cliente("Angel Perez", TipoPessoa.FISICA, "712.296.412-46", "(99)99999-9999",
		//				"angel@gmail.com");

		URI uri = new URI("/clientes/findById/400");

		mock.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().is(404));
	}

	@Test
	@Order(5)
	public void DeveRetornarStatus200QuandoClienteCadastradoComSucesso() throws Exception {

		Cliente cliente = new Cliente("Jose Perez", TipoCliente.FISICO, "712.296.092-70", "(99)99999-9999",
				"jose@gmail.com");
		cliente.setCodigo(2);
		URI uri = new URI("/clientes");

		String json = om.writeValueAsString(CreateRequestClienteDTO.toDto(cliente));

		String msg = mock
				.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200)).andReturn().getResponse().getContentAsString();

		Assertions.assertEquals(msg, "Cliente criado com sucesso!!");
	}

	@Test
	@Order(6)
	public void DeveRetornarStatus400QuandoClienteNãoValidoParaCadastro() throws Exception {

		//		Cliente cliente = new Cliente("Jose Perez", TipoPessoa.FISICA, "712.296.092-70", "(99)99999-9999",
		//				"jose@gmail.com");

		URI uri = new URI("/clientes");

		String json = "valor qualquer";

		mock.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	@Order(7)
	public void DeveRetornarStatus400QuandoCPFJaCadastrado() throws Exception {

		Cliente cliente = new Cliente("Jose Perez", TipoCliente.FISICO, "712.296.092-70", "(99)99999-9999",
				"jose@gmail.com");

		cliente.setCodigo(2);
		URI uri = new URI("/clientes");

		String json = om.writeValueAsString(CreateRequestClienteDTO.toDto(cliente));

		mock.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	@Order(8)
	public void DeveRetornarStatus400QuandoCPFInvalido() throws Exception {

		Cliente cliente = new Cliente("Esteban Perez", TipoCliente.FISICO, "000.000.000-00", "(99)99999-9999",
				"esteban@gmail.com");
		cliente.setCodigo(3);
		cliente.setEmail(null);

		URI uri = new URI("/clientes");
		String json = om.writeValueAsString(CreateRequestClienteDTO.toDto(cliente));

		mock.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	@Order(9)
	public void DeveRetornarStatus400QuandoCNPJJaCadastrado() throws Exception {

		Cliente cliente = new Cliente("Luis Perez", TipoCliente.JURIDICO, "00.394.460/0058-87", "(99)99999-9999",
				"luis@gmail.com");
		cliente.setCodigo(4);
		cliente.setEmail(null);

		URI uri = new URI("/clientes");
		String json = om.writeValueAsString(CreateRequestClienteDTO.toDto(cliente));
		mock.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON));

		mock.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	@Order(10)
	public void DeveRetornarStatus400QuandoCNPJInvalido() throws Exception {

		Cliente cliente = new Cliente("Luis Boada", TipoCliente.JURIDICO, "00.000.000/0000-00", "(99)99999-9999",
				"luis@gmail.com");
		cliente.setCodigo(5);
		cliente.setEmail(null);

		URI uri = new URI("/clientes");
		String json = om.writeValueAsString(CreateRequestClienteDTO.toDto(cliente));

		mock.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	@Order(11)
	public void DeveRetornarStatus200QuandoClienteAtualizadoComSucesso() throws Exception {

		UpdateRequestClienteDTO dto = new UpdateRequestClienteDTO("(99)99999-9999", "josep@gmail.com");
		String json = om.writeValueAsString(dto);
		URI uri = new URI("/clientes/2");

		String msg = mock.perform(MockMvcRequestBuilders.put(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200)).andReturn().getResponse().getContentAsString();

		Assertions.assertEquals(msg, "Cliente atualizado com sucesso!!");
	}

	@Test
	@Order(12)
	public void DeveRetornarStatus400QuandoDadosParaAtualizarDoClienteForamInvalidos() throws Exception {

		UpdateRequestClienteDTO dto = new UpdateRequestClienteDTO(null, "josep@gmail.com");
		String json = om.writeValueAsString(dto);
		URI uri = new URI("/clientes/2");

		mock.perform(MockMvcRequestBuilders.put(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	@Order(13)
	public void DeveRetornarStatus400QuandoIdParaAtualizarOClienteEstiverInvalido() throws Exception {

		UpdateRequestClienteDTO dto = new UpdateRequestClienteDTO("(99)99999-9999", null);
		String json = om.writeValueAsString(dto);
		URI uri = new URI("/clientes/R");

		mock.perform(MockMvcRequestBuilders.put(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	@Order(14)
	public void DeveRetornarStatus200QuandoClienteExcluidoComSucesso() throws Exception {

		URI uri = new URI("/clientes/1");

		String msg = mock.perform(MockMvcRequestBuilders.delete(uri)).andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn().getResponse().getContentAsString();

		Assertions.assertEquals(msg, "Cliente excluido com sucesso!!");
	}

	@Test
	@Order(15)
	public void DeveRetornarStatus400QuandoIdDoClienteParaExcluirEstiverInavlido() throws Exception {

		URI uri = new URI("/clientes/1");
		mock.perform(MockMvcRequestBuilders.put(uri)).andExpect(MockMvcResultMatchers.status().is(400));
	}
}