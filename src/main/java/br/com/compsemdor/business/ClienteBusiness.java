package br.com.compsemdor.business;

import java.lang.reflect.Field;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import br.com.compsemdor.business.utils.StringUtils;
import br.com.compsemdor.controller.exceptions.AplicationException;
import br.com.compsemdor.controller.request.cliente.SalvarClienteRequest;
import br.com.compsemdor.model.Cliente;
import br.com.compsemdor.model.enums.TipoDocumento;
import br.com.compsemdor.repo.ClienteRepo;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ClienteBusiness {

	private ClienteRepo repo;


	public ClienteBusiness(
			ClienteRepo repo) {
		super();
		this.repo = repo;
	}

	public Cliente getCliente(int cod) {
		var cliente = repo.findById(cod);
		if (cliente.isPresent())
			return cliente.get();
		throw new AplicationException("Cliente não encontrado", "Cliente de código " + cod + " não encontrado");
		// TODO adicionar um erro 404
	}

	public void deletar(int cod) {
		var cliente = repo.findById(cod);
		if (cliente.isPresent())
			log.info("Deletando o cliente " + cliente.get().toString());
		else
			throw new AplicationException("Cliente não encontrado", "Cliente de código " + cod + " não encontrado");
		repo.deleteById(cod);
	}

	public Cliente atualizarClientePorCampos(int id, Map<String, Object> campos) {
		var existingProduct = repo.findById(id);
		if (!existingProduct.isPresent())
			throw new AplicationException("Erro ao atualizar cliente", "Cliente não encontrado para ser atualizado");
		campos.forEach((chave, valor) -> {
			Field campo = ReflectionUtils.findField(Cliente.class, chave);
			campo.setAccessible(true);
			ReflectionUtils.setField(campo, existingProduct.get(), valor);
		});
		return repo.save(existingProduct.get());
	}

	public List<Cliente> getClientes() {
		return repo.findAll();
	}

	public Cliente salvar(SalvarClienteRequest cliente) {
		cliente.setCpfCnpj(StringUtils.removeSymbols(cliente.getCpfCnpj()));
		validaDocumentoValido(cliente);
		validaDataNascimento(cliente);
		validaDuplicidadeDocumento(cliente);
		validaDocumentoAtivo(cliente);
		return repo.save(this.convertToCliente(cliente));
	}

	private void validaDocumentoAtivo(SalvarClienteRequest cliente) {
	}

	private void validaDocumentoValido(SalvarClienteRequest cliente) {
		if (!documentoValido(cliente))
			throw new AplicationException("Erro de validação de documento",
					"Documento " + cliente.getCpfCnpj() + " inválido ");
	}

	private void validaDataNascimento(SalvarClienteRequest cliente) {
		if (!dataNascimentoValida(cliente))
			throw new AplicationException("Erro de validação de documento", "Data de nascimento inválida");
	}

	private void validaDuplicidadeDocumento(SalvarClienteRequest cliente) {
		var clienteTemp = repo.findByCpfCnpj(cliente.getCpfCnpj());
		if (clienteTemp.isPresent())
			throw new AplicationException("Erro de validação de documento",
					"CPF/CNPJ já cadastrado com o nome '" + clienteTemp.get().getNomeCliente() + "'");
	}

	private Cliente convertToCliente(SalvarClienteRequest cliente) {
		Cliente result = Cliente.builder().nomeCliente(cliente.getNomeCliente())
				.cep(cliente.getCep())
				.cpfCnpj(cliente.getCpfCnpj())
				.dataNascimento(cliente.getDataNascimento())
				.clienteAtivo(cliente.isClienteAtivo())
				.tipoDeDocumento(cliente.getTipoDeDocumento())
				.build();
		return result;
	}

	private boolean dataNascimentoValida(SalvarClienteRequest cliente) {
		if (cliente.getTipoDeDocumento() == TipoDocumento.CPF && cliente.getDataNascimento() == null)
			return false;
		if (cliente.getTipoDeDocumento() == TipoDocumento.CNPJ && cliente.getDataNascimento() == null)
			return true;
		return cliente.getDataNascimento().before(Date.from(Instant.now()));
	}

	private boolean documentoValido(SalvarClienteRequest documento) {
		switch (documento.getTipoDeDocumento()) {
			case CNPJ:
				return ClienteBusiness.validaCnpj(documento.getCpfCnpj());
			case CPF:
				return ClienteBusiness.ValidaCPF(documento.getCpfCnpj());
			default:
				return false;
		}
	}

	private static boolean ValidaCPF(String vrCPF) {
		String valor = vrCPF.replace(".", "");
		valor = valor.replace("-", "");
		if (valor.length() != 11)
			return false;
		boolean igual = true;
		for (int i = 1; i < 11 && igual; i++)
			if (valor.charAt(i) != valor.charAt(0))
				igual = false;
		if (igual || valor.equals("12345678909"))
			return false;
		int[] numeros = new int[11];
		try {
			for (int i = 0; i < 11; i++)
				numeros[i] = Integer.parseInt(
						Character.toString(valor.charAt(i)));
		} catch (NumberFormatException ex) {
			return false;
		}
		int soma = 0;
		for (int i = 0; i < 9; i++)
			soma += (10 - i) * numeros[i];
		int resultado = soma % 11;
		if (resultado == 1 || resultado == 0) {
			if (numeros[9] != 0)
				return false;
		} else if (numeros[9] != 11 - resultado)
			return false;
		soma = 0;
		for (int i = 0; i < 10; i++)
			soma += (11 - i) * numeros[i];
		resultado = soma % 11;
		if (resultado == 1 || resultado == 0) {
			if (numeros[10] != 0)
				return false;
		} else if (numeros[10] != 11 - resultado)
			return false;
		return true;
	}

	private static boolean validaCnpj(String vrCNPJ) {
		String CNPJ = vrCNPJ.replace(".", "");
		CNPJ = CNPJ.replace("/", "");
		CNPJ = CNPJ.replace("-", "");
		int[] digitos, soma, resultado;
		int nrDig;
		String ftmt;
		boolean[] CNPJOk;
		ftmt = "6543298765432";
		digitos = new int[14];
		soma = new int[2];
		soma[0] = 0;
		soma[1] = 0;
		resultado = new int[2];
		resultado[0] = 0;
		resultado[1] = 0;
		CNPJOk = new boolean[2];
		CNPJOk[0] = false;
		CNPJOk[1] = false;
		try {
			for (nrDig = 0; nrDig < 14; nrDig++) {
				digitos[nrDig] = Integer.parseInt(
						CNPJ.substring(nrDig, 1));
				if (nrDig <= 11)
					soma[0] += (digitos[nrDig] *
							Integer.parseInt(ftmt.substring(
									nrDig + 1, 1)));
				if (nrDig <= 12)
					soma[1] += (digitos[nrDig] *
							Integer.parseInt(ftmt.substring(
									nrDig, 1)));
			}
			for (nrDig = 0; nrDig < 2; nrDig++) {
				resultado[nrDig] = (soma[nrDig] % 11);
				if ((resultado[nrDig] == 0) || (resultado[nrDig] == 1))
					CNPJOk[nrDig] = (digitos[12 + nrDig] == 0);
				else
					CNPJOk[nrDig] = (digitos[12 + nrDig] == (11 - resultado[nrDig]));
			}
			return (CNPJOk[0] && CNPJOk[1]);
		} catch (Exception ex) {
			return false;
		}
	}

	public Cliente atualizarCliente(int id, SalvarClienteRequest fields) {
		// TODO melhorar tratamento deste caso. Necessário fazer tratamento c/
		// verificação se o cliente existe
		var cliente = convertToCliente(fields);
		return repo.save(cliente);
	}
}
