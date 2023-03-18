package br.com.compsemdor.controller.exceptions;

import java.util.ArrayList;
import java.util.List;

import br.com.compsemdor.model.ErrorApi;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final List<ErrorApi> erros = new ArrayList<>();


	protected BaseException(
			ErrorApi e) {
		super(e.getDescricao());
		this.erros.add(e);
	}

	protected BaseException(
			String titulo,
			String descricao) {
		super(descricao);
		this.erros.add(new ErrorApi(titulo, descricao));
	}
}
