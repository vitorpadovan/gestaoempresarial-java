package br.com.compsemdor.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorApi implements Serializable {

	private static final long serialVersionUID = 1L;

	private String erro;

	private String descricao;
}
