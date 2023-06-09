package br.com.compsemdor.controller.exceptions;

import br.com.compsemdor.model.ErrorApi;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AplicationException extends BaseException {

	private static final long serialVersionUID = 1L;


	public AplicationException(
			ErrorApi e) {
		super(e);
	}

	public AplicationException(
			String titulo,
			String descricao) {
		super(titulo, descricao);
	}
}
