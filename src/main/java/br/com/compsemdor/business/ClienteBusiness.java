package br.com.compsemdor.business;

import java.time.Instant;

import org.springframework.stereotype.Service;

import br.com.compsemdor.business.utils.StringUtils;
import br.com.compsemdor.business.utils.ValidacaoDocumento;
import br.com.compsemdor.controller.exceptions.AplicationException;
import br.com.compsemdor.controller.exceptions.NotFoundException;
import br.com.compsemdor.model.Cliente;
import br.com.compsemdor.model.enums.TipoDocumento;
import br.com.compsemdor.repo.ClienteRepo;

@Service
public class ClienteBusiness extends BaseBusiness<Cliente, Integer, ClienteRepo> {

	private static final String ERRO_VALIDACAO_DOCUMENTO = "Documento inválido";


	public ClienteBusiness(
			ClienteRepo repo) {
		super(repo);
	}

	@Override
	protected NotFoundException getMesagemNaoEncontrado(Integer cod) {
		return new NotFoundException("Cliente não encontrado",
				String.format("Cliente de código %d não foi encontrado", cod));
	}

	@Override
	protected void validacoesExtrasSalvar(Cliente item) {
		item.setCep(StringUtils.removeSymbols(item.getCep()));
		item.setCpfCnpj(StringUtils.removeSymbols(item.getCpfCnpj()));
		validaDocumentoValido(item);
		validaDataNascimento(item);
		validaDuplicidadeDocumento(item);
	}

	@Override
	protected void validacoesExtrasSalvar(Integer cod, Cliente item) {
		item.setCep(StringUtils.removeSymbols(item.getCep()));
		item.setCpfCnpj(StringUtils.removeSymbols(item.getCpfCnpj()));
		var dbResponse = repo.findByCpfCnpj(item.getCpfCnpj());
		if (dbResponse.isPresent()) {
			var cliente = dbResponse.get();
			if (cliente.getCodCliente() != cod)
				throw new AplicationException("Erro ao cadastrar cliente",
						String.format("Cliente %s já está usando o CPF/CNPJ %s ", item.getNomeCliente(), item.getCpfCnpj()));
		}
	}

	@Override
	protected void atualizar(Cliente origem, Cliente destino) {
		destino.update(origem);
	}

	private void validaDocumentoValido(Cliente cliente) {
		if (!documentoValido(cliente))
			throw new AplicationException(ERRO_VALIDACAO_DOCUMENTO,
					"Documento " + cliente.getCpfCnpj() + " inválido ");
	}

	private void validaDataNascimento(Cliente cliente) {
		if (!dataNascimentoValida(cliente))
			throw new AplicationException(ERRO_VALIDACAO_DOCUMENTO, "Data de nascimento inválida");
	}

	private boolean documentoValido(Cliente documento) {
		switch (documento.getTipoDeDocumento()) {
			case CNPJ:
				return ValidacaoDocumento.validaCnpj(documento.getCpfCnpj());
			case CPF:
				return ValidacaoDocumento.validaCPF(documento.getCpfCnpj());
			default:
				return false;
		}
	}

	private boolean dataNascimentoValida(Cliente cliente) {
		if (cliente.getTipoDeDocumento() == TipoDocumento.CPF && cliente.getDataNascimento() == null)
			return false;
		if (cliente.getTipoDeDocumento() == TipoDocumento.CNPJ && cliente.getDataNascimento() == null)
			return true;
		return cliente.getDataNascimento().before(java.util.Date.from(Instant.now()));
	}

	private void validaDuplicidadeDocumento(Cliente cliente) {
		var clienteTemp = repo.findByCpfCnpj(cliente.getCpfCnpj());
		if (clienteTemp.isPresent())
			throw new AplicationException(ERRO_VALIDACAO_DOCUMENTO,
					"CPF/CNPJ já cadastrado com o nome '" + clienteTemp.get().getNomeCliente() + "'");
	}
}
