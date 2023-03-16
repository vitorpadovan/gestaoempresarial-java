package br.com.compsemdor.controller.exceptions;

import java.util.ArrayList;
import java.util.List;

import br.com.compsemdor.model.ErrorApi;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<ErrorApi> erros = new ArrayList<ErrorApi>();


	public AplicationException(
			ErrorApi e) {
		super(e.getDescricao());
		this.erros.add(e);
	}

	public AplicationException(
			String titulo,
			String descricao) {
		super(descricao);
		this.erros.add(new ErrorApi(titulo, descricao));
	}
}
