package br.com.compsemdor.controller.request.cliente;

import java.sql.Date;

import br.com.compsemdor.model.Cliente;
import br.com.compsemdor.model.enums.TipoDocumento;
import br.com.compsemdor.model.interfaces.Convertable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalvarClienteRequest implements Convertable<Cliente> {

	private String nomeCliente;

	@NotNull
	@NotEmpty
	@Size(min = 11, max = 18, message = "Tamanho do documento deve estar entre 11 e 18")
	private String cpfCnpj;

	private Date dataNascimento;

	@NotNull(message = "Cep deve ser obrigatório")
	private String cep;

	@Min(value = 1, message = "Numero do endereço inválido")
	@NotNull(message = "Número deve ser obrigatório")
	private Integer numero;

	@Builder.Default
	private boolean clienteAtivo = false;

	@NotNull(message = "Obrigatório informar o tipo de documento")
	private TipoDocumento tipoDeDocumento;


	@Override
	public Cliente convert() {
		Cliente response = new Cliente();
		response.setCep(this.cep);
		response.setClienteAtivo(this.clienteAtivo);
		response.setCpfCnpj(this.cpfCnpj);
		response.setDataNascimento(this.dataNascimento);
		response.setNumero(numero);
		response.setNomeCliente(nomeCliente);
		response.setTipoDeDocumento(tipoDeDocumento);
		return response;
	}
}
