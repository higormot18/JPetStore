/*
 *    Copyright 2016-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.kazuki43zoo.jpetstore.component.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Lancada quando um pedido tenta consumir mais itens do que ha em estoque.
 *
 * <p>Criada durante o Plano de Evolucao da A3 para corrigir o defeito BUG-001
 * (estoque podia ficar negativo porque o UPDATE de inventario nao validava a
 * quantidade disponivel).</p>
 */
@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class InsufficientStockException extends RuntimeException {

	/** Identificador de serializacao da excecao. */
	private static final long serialVersionUID = 4821766132873451187L;

	/** Identificador do item sem estoque suficiente. */
	private final String itemId;

	/** Quantidade que o pedido tentou consumir. */
	private final int requestedQuantity;

	/**
	 * Cria a excecao para um item sem estoque suficiente.
	 *
	 * @param item identificador do item
	 * @param requestedQty quantidade solicitada pelo pedido
	 */
	public InsufficientStockException(final String item, final int requestedQty) {
		super("Estoque insuficiente para o item " + item
				+ " (quantidade solicitada: " + requestedQty + ").");
		this.itemId = item;
		this.requestedQuantity = requestedQty;
	}

}
