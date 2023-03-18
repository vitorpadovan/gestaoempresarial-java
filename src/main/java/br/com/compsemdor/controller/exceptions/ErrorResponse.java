package br.com.compsemdor.controller.exceptions;

import java.util.SortedMap;
import java.util.TreeMap;

import lombok.Data;

@Data
public class ErrorResponse {

	private SortedMap<String, String> mapa = new TreeMap<>();


	public ErrorResponse(
			AplicationException app) {
		for (var t : app.getErros()) {
			mapa.put((app.getErros().indexOf(t) + 1) + ". " + t.getErro(), t.getDescricao());
		}
	}

	public ErrorResponse(
			RuntimeException app) {
		mapa.put("Erro desconhecido", app.getMessage());
	}

	public ErrorResponse() {
	}

	public ErrorResponse addErrors(String titulo, String error) {
		mapa.put(titulo, error);
		return this;
	}
}
