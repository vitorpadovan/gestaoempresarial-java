package br.com.compsemdor.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
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
import br.com.compsemdor.controller.response.cliente.ClienteResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

	private ClienteBusiness business;

	private ModelMapper modelMapper;


	public ClienteController(
			ClienteBusiness business) {
		super();
		this.business = business;
		this.modelMapper = new ModelMapper();
	}

	@GetMapping
	public List<ClienteResponse> getClientes() {
		return business.getItens().stream().map(c -> modelMapper.map(c, ClienteResponse.class))
				.toList();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponse> getCliente(@PathVariable int id) {
		return ResponseEntity.ok(modelMapper.map(business.getItem(id), ClienteResponse.class));
	}

	@PostMapping
	public ResponseEntity<ClienteResponse> salvarCliente(@Valid @RequestBody SalvarClienteRequest cliente) {
		var resultado = business.salvar(cliente);
		ClienteResponse r = modelMapper.map(resultado, ClienteResponse.class);
		try {
			return ResponseEntity.created(new URI("/api/cliente/" + resultado.getCodCliente())).body(r);
		} catch (URISyntaxException e) {
			throw new AplicationException("Erro ao criar conteúdo", e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletarCliente(@PathVariable int id) {
		business.deletar(id);
		return ResponseEntity.ok(null);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ClienteResponse> atualizarCliente(@PathVariable int id,
			@RequestBody Map<String, Object> campos) {
		var resposta = modelMapper.map(business.atualizarPorCampo(id, campos), ClienteResponse.class);
		return ResponseEntity.ok(resposta);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteResponse> atualizarCliente(@PathVariable int id,
			@RequestBody SalvarClienteRequest cliente) {
		var resposta = modelMapper.map(business.atualizarItem(id, cliente), ClienteResponse.class);
		return ResponseEntity.ok(resposta);
	}
}
