package br.com.compsemdor.controller.response.cliente;

import java.sql.Date;

import br.com.compsemdor.business.utils.StringUtils;
import br.com.compsemdor.controller.exceptions.AplicationException;
import br.com.compsemdor.model.enums.TipoDocumento;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {

	private int codCliente;

	private String nomeCliente;

	@Getter(AccessLevel.NONE)
	private String cpfCnpj;

	private Date dataNascimento;

	@Getter(AccessLevel.NONE)
	private String cep;

	private Integer numero;

	@Builder.Default
	private Boolean clienteAtivo = false;

	private TipoDocumento tipoDeDocumento;


	public String getCpfCnpj() {
		switch (tipoDeDocumento) {
			case CNPJ:
				return StringUtils.formatMask(this.cpfCnpj, "###.###.###-##");
			case CPF:
				return StringUtils.formatMask(this.cpfCnpj, "###.###.###-##");
			default:
				throw new AplicationException("Problema de conversão",
						String.format("Cliente COD: %d, %s não possui um tipo de documento definido para o documento de número %s ",
								this.codCliente,
								this.getNomeCliente(), this.cpfCnpj));
		}
	}

	public String getCep() {
		return StringUtils.formatMask(this.cep, "##.###-###");
	}
}
