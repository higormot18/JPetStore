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
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kazuki Shimizu
 */
@SessionScope
@Component
public class Cart implements Serializable {

	private static final long serialVersionUID = 8329559983943337176L;

	private final Map<String, CartItem> itemMap = new ConcurrentHashMap<>();
	private final List<CartItem> itemList =
			Collections.synchronizedList(new ArrayList<>());

	public List<CartItem> getCartItems() {
		return Collections.unmodifiableList(new ArrayList<>(itemList));
	}

	public boolean containsByItemId(String itemId) {
		return itemMap.containsKey(itemId);
	}

	public synchronized void addItem(Item item, boolean isInStock) {
		CartItem cartItem = itemMap.get(item.getItemId());
		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setItem(item);
			cartItem.setQuantity(0);
			cartItem.setInStock(isInStock);
			itemMap.put(item.getItemId(), cartItem);
			itemList.add(cartItem);
		}
		cartItem.incrementQuantity();
	}

	public synchronized void removeItemById(String itemId) {
		Optional.ofNullable(itemMap.remove(itemId))
				.ifPresent(itemList::remove);
	}

	public synchronized void incrementQuantityByItemId(String itemId) {
		Optional.ofNullable(itemMap.get(itemId))
				.ifPresent(CartItem::incrementQuantity);
	}

	public synchronized void setQuantityByItemId(
			String itemId, int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException(
					"A quantidade nao pode ser negativa: " + quantity);
		}
		Optional.ofNullable(itemMap.get(itemId))
				.ifPresent(item -> item.setQuantity(quantity));
	}

	public BigDecimal getSubTotal() {
		return getCartItems().stream()
				.map(x -> x.getItem().getListPrice()
						.multiply(BigDecimal.valueOf(x.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public boolean isEmpty() {
		return itemList.isEmpty();
	}

	public void clear() {
		itemMap.clear();
		itemList.clear();
	}

}
