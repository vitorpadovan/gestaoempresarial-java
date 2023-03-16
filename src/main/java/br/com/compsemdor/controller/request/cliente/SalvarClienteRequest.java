package br.com.compsemdor.controller.request.cliente;

import java.sql.Date;

import br.com.compsemdor.model.enums.TipoDocumento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalvarClienteRequest {

	private String nomeCliente;

	@NotNull
	@NotEmpty
	@Size(min = 11, max = 18, message = "Tamanho do documento deve estar entre 11 e 18")
	private String CpfCnpj;

	private Date dataNascimento;

	private String cep;

	@Min(value = 1, message = "Numero do endereço inválido")
	@NotNull
	private Integer numero;

	private boolean clienteAtivo = false;

	private TipoDocumento tipoDeDocumento;
}
