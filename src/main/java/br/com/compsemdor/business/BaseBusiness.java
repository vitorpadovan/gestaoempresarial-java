package br.com.compsemdor.business;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.ReflectionUtils;

import br.com.compsemdor.controller.exceptions.NotFoundException;
import br.com.compsemdor.model.Cliente;
import br.com.compsemdor.model.interfaces.Convertable;

public abstract class BaseBusiness<T, D, I extends JpaRepository<T, D>> {

	protected I repo;


	protected BaseBusiness(
			I repo) {
		super();
		this.repo = repo;
	}

	protected abstract NotFoundException getMesagemNaoEncontrado(D cod);

	public T getItem(D cod) {
		var item = repo.findById(cod);
		if (!item.isPresent())
			throw getMesagemNaoEncontrado(cod);
		return item.get();
	}

	public List<T> getItens() {
		return repo.findAll();
	}

	protected abstract void validacoesExtrasSalvar(T item);

	protected abstract void validacoesExtrasSalvar(D cod, T item);

	protected abstract void atualizar(T origem, T destino);

	protected T salvar(T item) {
		validacoesExtrasSalvar(item);
		return repo.save(item);
	}

	public T salvar(Convertable<T> item) {
		return this.salvar(item.convert());
	}

	public boolean deletar(D cod) {
		getItem(cod);
		repo.deleteById(cod);
		return true;
	}

	public T atualizarPorCampo(D cod, Map<String, Object> fields) {
		var item = this.getItem(cod);
		fields.forEach((chave, valor) -> {
			Field campo = ReflectionUtils.findField(Cliente.class, chave);
			// campo.setAccessible(true);
			ReflectionUtils.setField(campo, item, valor);
		});
		this.validacoesExtrasSalvar(cod, item);
		return repo.save(item);
	}

	public T atualizarItem(D id, Convertable<T> item) {
		return this.atualizarItem(id, item.convert());
	}

	public T atualizarItem(D id, T item) {
		var dbResponse = this.getItem(id);
		this.validacoesExtrasSalvar(id, item);
		this.atualizar(item, dbResponse);
		return repo.save(dbResponse);
	}
}
