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
package com.kazuki43zoo.jpetstore.ui;

import com.kazuki43zoo.jpetstore.domain.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de regressao do carrinho criados no Plano de Evolucao da A3.
 *
 * <p>Cobrem os defeitos BUG-003 (NullPointerException ao alterar item ausente)
 * e BUG-004 (quantidade negativa aceita pelo carrinho).</p>
 */
class CartTest {

	private static Item item(String itemId, String listPrice) {
		Item item = new Item();
		item.setItemId(itemId);
		item.setListPrice(new BigDecimal(listPrice));
		return item;
	}

	@Test
	@DisplayName("CT-201: incrementar item inexistente nao lanca NullPointerException")
	void incrementarItemInexistenteNaoLancaExcecao() {
		Cart cart = new Cart();

		assertDoesNotThrow(() -> cart.incrementQuantityByItemId("EST-INEXISTENTE"));
		assertTrue(cart.isEmpty());
	}

	@Test
	@DisplayName("CT-202: alterar quantidade de item inexistente nao lanca NullPointerException")
	void alterarQuantidadeDeItemInexistenteNaoLancaExcecao() {
		Cart cart = new Cart();

		assertDoesNotThrow(() -> cart.setQuantityByItemId("EST-INEXISTENTE", 5));
		assertTrue(cart.isEmpty());
	}

	@Test
	@DisplayName("CT-203: quantidade negativa e rejeitada")
	void quantidadeNegativaERejeitada() {
		Cart cart = new Cart();
		cart.addItem(item("EST-1", "10.00"), true);

		assertThrows(IllegalArgumentException.class,
				() -> cart.setQuantityByItemId("EST-1", -1));
	}

	@Test
	@DisplayName("CT-204: quantidade zero e aceita (valor limite)")
	void quantidadeZeroEAceita() {
		Cart cart = new Cart();
		cart.addItem(item("EST-1", "10.00"), true);

		cart.setQuantityByItemId("EST-1", 0);

		assertEquals(BigDecimal.ZERO.setScale(2), cart.getSubTotal().setScale(2));
	}

	@Test
	@DisplayName("CT-205: subtotal soma corretamente itens distintos")
	void subtotalSomaItensDistintos() {
		Cart cart = new Cart();
		cart.addItem(item("EST-1", "10.00"), true);
		cart.addItem(item("EST-2", "5.50"), true);
		cart.setQuantityByItemId("EST-1", 3);

		assertEquals(new BigDecimal("35.50"), cart.getSubTotal());
	}

	@Test
	@DisplayName("CT-206: adicionar o mesmo item duas vezes incrementa a quantidade")
	void adicionarMesmoItemIncrementaQuantidade() {
		Cart cart = new Cart();
		Item est1 = item("EST-1", "10.00");
		cart.addItem(est1, true);
		cart.addItem(est1, true);

		assertEquals(1, cart.getCartItems().size());
		assertEquals(2, cart.getCartItems().get(0).getQuantity());
	}

	@Test
	@DisplayName("CT-207: remover item deixa o carrinho vazio e consistente")
	void removerItemDeixaCarrinhoVazio() {
		Cart cart = new Cart();
		cart.addItem(item("EST-1", "10.00"), true);

		cart.removeItemById("EST-1");

		assertTrue(cart.isEmpty());
		assertEquals(0, cart.getCartItems().size());
		assertEquals(BigDecimal.ZERO, cart.getSubTotal());
	}

}
