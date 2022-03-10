package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start(){

		/**
		 * Apaga todos os registros do banco de dados antes de iniciar os testes
		 */
		usuarioRepository.deleteAll();
		
	}

	@Test
	@Order(1)
	@DisplayName("Cadastrar Um Usuário")
	public void deveCriarUmUsuario() {

		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, 
			"Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg"));

		 
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());

		
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());

		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}

	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {

		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}

	@Test
	@Order(3)
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario() {

		
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "https://i.imgur.com/yDRVeK7.jpg"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), 
			"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123" , "https://i.imgur.com/yDRVeK7.jpg");
		
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> corpoResposta = testRestTemplate
			.withBasicAuth("root", "root")
			.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

		/**
		 *  Verifica se a requisição retornou o Status Code OK (200) 
		 * Se for verdadeira, o teste passa, se não, o teste falha.
		 */
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());

		/**
		 * Verifica se o Atributo Nome do Objeto da Classe Usuario retornado no Corpo da Requisição 
		 * é igual ao Atributo Nome do Objeto da Classe Usuario Retornado no Corpo da Resposta
		 * Se for verdadeiro, o teste passa, senão o teste falha.
		 */
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());

		
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}

	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {

		/**
		 * Persiste dois objetos diferentes da Classe Usuario no Banco de dados através do Objeto da Classe UsuarioService
		 */
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));

		
		ResponseEntity<String> resposta = testRestTemplate
			.withBasicAuth("root", "root")
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class);

		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}

}