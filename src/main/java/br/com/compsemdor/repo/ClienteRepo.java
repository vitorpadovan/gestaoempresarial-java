package br.com.compsemdor.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.compsemdor.model.Cliente;

public interface ClienteRepo extends JpaRepository<Cliente, Integer> {

	Optional<Cliente> findByCpfCnpj(String cpfCnpj);
}
