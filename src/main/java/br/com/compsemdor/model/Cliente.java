package br.com.compsemdor.model;

import java.sql.Date;

import br.com.compsemdor.model.enums.TipoDocumento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(
		uniqueConstraints = { @UniqueConstraint(name = "cpfCnpj", columnNames = "cpfCnpj") }
)
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

	@Id
	@GeneratedValue(generator = "cliente_seq")
	@SequenceGenerator(name = "cliente_seq", sequenceName = "cliente_seq", allocationSize = 1)
	private int codCliente;

	private String nomeCliente;

	@Column(nullable = false, length = 15)
	private String cpfCnpj;

	@Column(nullable = true)
	private Date dataNascimento;

	private String cep;

	@Column(nullable = true)
	private Integer numero;

	private Boolean clienteAtivo = false;

	private TipoDocumento tipoDeDocumento;
}
