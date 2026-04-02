package br.com.drky.gestor.service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.drky.gestor.dto.CreateRequestClienteDTO;
import br.com.drky.gestor.dto.UpdateRequestClienteDTO;
import br.com.drky.gestor.exception.ClientNotFoundException;
import br.com.drky.gestor.exception.InvalidClientDocumentException;
import br.com.drky.gestor.exception.InvalidObjectOnRequestException;
import br.com.drky.gestor.exception.RegisteredClientException;
import br.com.drky.gestor.model.Cliente;
import br.com.drky.gestor.model.enums.TipoCliente;
import br.com.drky.gestor.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	public void inserirCliente(CreateRequestClienteDTO dto) {

		System.out.println(dto.email());

		Cliente cliente = new Cliente(dto.nome(), TipoCliente.valueOf(dto.tipo().toUpperCase()), dto.cpfCnpj(),
				dto.telefone(), dto.email());

		TipoCliente tipo = TipoCliente.valueOf(dto.tipo().toUpperCase());
		String documento = dto.cpfCnpj();

		boolean esValido = (tipo == TipoCliente.FISICO) ? verificarCPF(documento) : verificarCNPJ(documento);

		if (!esValido) {
			throw new InvalidClientDocumentException(tipo == TipoCliente.FISICO ? "CPF inválido" : "CNPJ inválido");
		}

		Optional<Cliente> opt = repository.findByCpfCnpj(cliente.getCpfCnpj());
		if (!opt.isPresent()) {
			repository.save(cliente);
		} else {
			Cliente temp = opt.get();
			if (temp.getTipo() == TipoCliente.FISICO) {
				throw new RegisteredClientException("CPF já cadastrado");
			} else {
				throw new RegisteredClientException("CNPJ já cadastrado");
			}
		}
	}

	public void atualizarCliente(Integer id, UpdateRequestClienteDTO dto) {

		try {
			Optional<Cliente> byId = repository.findById(id);
			if (byId.isPresent()) {
				Cliente cliente = byId.get();
				if (dto.telefone() != null)
					cliente.setTelefone(dto.telefone());

				if (dto.email() != null)
					cliente.setEmail(dto.email());

				repository.save(cliente);
			} else {
				throw new ClientNotFoundException("Cliente não encontrado");
			}
		} catch (MethodArgumentTypeMismatchException e) {
			throw new InvalidObjectOnRequestException("Dados invalidos");
		}
	}

	public Cliente buscaClientePorId(Integer id) {

		try {
			Optional<Cliente> byId = repository.findById(id);
			if (byId.isPresent()) {
				return byId.get();
			} else {
				throw new ClientNotFoundException("Cliente não encontrado");
			}
		} catch (MethodArgumentTypeMismatchException e) {
			throw new InvalidObjectOnRequestException("Dados invalidos");
		}
	}

	public void excluirClientePorId(Integer id) {

		try {
			Optional<Cliente> byId = repository.findById(id);
			if (byId.isPresent()) {
				repository.deleteById(id);
			} else {
				throw new ClientNotFoundException("Cliente não encontrado");
			}
		} catch (MethodArgumentTypeMismatchException e) {
			throw new InvalidObjectOnRequestException("Dados invalidos");
		}
	}

	public List<Cliente> BuscaTodosOsClientes() {
		return repository.findAll();
	}

	public boolean verificarCPF(String CPF) {

		CPF = CPF.replace("-", "").replace(".", "");

		// considera-se erro CPF"s formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222")
				|| CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555")
				|| CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888")
				|| CPF.equals("99999999999") || (CPF.length() != 11))
			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere "0" no inteiro 0
				// (48 eh a posicao de "0" na tabela ASCII)
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48); // converte no respectivo caractere numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);

			// Verifica se os digitos calculados conferem com os digitos informados.
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	public boolean verificarCNPJ(String CNPJ) {

		CNPJ = CNPJ.replace("-", "").replace(".", "").replace("/", "");

		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222")
				|| CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555")
				|| CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888")
				|| CNPJ.equals("99999999999999") || (CNPJ.length() != 14))
			return (false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-ésimo caractere do CNPJ em um número:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posição de '0' na tabela ASCII)
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			// Verifica se os dígitos calculados conferem com os dígitos informados.
			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}
}