package br.com.compsemdor.controller.exceptions;

import br.com.compsemdor.model.ErrorApi;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NotFoundException extends BaseException {

	private static final long serialVersionUID = 1L;


	public NotFoundException(
			ErrorApi e) {
		super(e);
	}

	public NotFoundException(
			String titulo,
			String descricao) {
		super(titulo, descricao);
	}
}
