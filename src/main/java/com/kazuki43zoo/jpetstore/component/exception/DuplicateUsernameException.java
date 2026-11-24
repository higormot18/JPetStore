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
 * Lancada quando ha tentativa de cadastrar um nome de usuario ja existente.
 *
 * <p>Criada durante o Plano de Evolucao da A3 para corrigir o defeito BUG-002
 * (cadastro duplicado resultava em violacao de chave primaria e erro HTTP 500
 * em vez de uma mensagem de validacao para o usuario).</p>
 */
@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateUsernameException extends RuntimeException {

	/** Identificador de serializacao da excecao. */
	private static final long serialVersionUID = 7712094431880654321L;

	/** Nome de usuario que ja existe na base. */
	private final String username;

	/**
	 * Cria a excecao para um nome de usuario duplicado.
	 *
	 * @param name nome de usuario informado no cadastro
	 */
	public DuplicateUsernameException(final String name) {
		super("O nome de usuario informado ja esta em uso: " + name);
		this.username = name;
	}

}
