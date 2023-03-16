package br.com.compsemdor.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.compsemdor.business.ClienteBusiness;
import br.com.compsemdor.controller.exceptions.AplicationException;
import br.com.compsemdor.controller.request.cliente.SalvarClienteRequest;
import br.com.compsemdor.model.Cliente;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

	private ClienteBusiness _business;


	public ClienteController(
			ClienteBusiness _business) {
		super();
		this._business = _business;
	}

	@GetMapping
	public List<Cliente> getClientes() {
		return _business.getClientes();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> getCliente(@PathVariable int id) {
		return ResponseEntity.ok(_business.getCliente(id));
	}

	@PostMapping
	public ResponseEntity<Cliente> salvarCliente(@Valid @RequestBody SalvarClienteRequest cliente) {
		var resultado = _business.salvar(cliente);
		try {
			return ResponseEntity.created(new URI("/api/cliente/" + resultado.getCodCliente())).body(resultado);
		} catch (URISyntaxException e) {
			throw new AplicationException("Erro ao criar conteúdo", e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletarCliente(@PathVariable int id) {
		_business.deletar(id);
		return ResponseEntity.ok(null);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Cliente> atualizarCliente(@PathVariable int id, @RequestBody Map<String, Object> fields) {
		var resposta = _business.atualizarClientePorCampos(id, fields);
		return ResponseEntity.ok(resposta);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizarCliente(@PathVariable int id, @RequestBody SalvarClienteRequest fields) {
		var resposta = _business.atualizarCliente(id, fields);
		return ResponseEntity.ok(resposta);
	}
}
