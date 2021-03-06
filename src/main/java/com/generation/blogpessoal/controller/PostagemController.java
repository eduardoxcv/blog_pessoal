package com.generation.blogpessoal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {
	
@Autowired
private PostagemRepository postagemRepository;


@Autowired
private TemaRepository temaRepository;


	@GetMapping // esse metodo nunca dá erro - metodo para visão geral das postagens 
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll());
		
	}
	@GetMapping("/{id}") // metodo para visualizar somente o id 1 ou postagem 1
	public ResponseEntity<Postagem> getById(@PathVariable Long id){
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))// caso ele coloque um id que não existe vai responder com um erro
				.orElse(ResponseEntity.notFound().build());

	}
	@GetMapping ("/titulo/{titulo}") // esse medoto serve para criar uma lista com os titulos 
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	@PostMapping
	public ResponseEntity<Postagem> postPostagem (@Valid @RequestBody Postagem postagem){

		if (temaRepository.existsById(postagem.getTema().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
	
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
				
	} // nessa linha estamos salvando algo no banco
	
	@PutMapping
	public ResponseEntity<Postagem> putPostagem (@Valid @RequestBody Postagem postagem){
		
		if (postagemRepository.existsById(postagem.getId())){
		
		if (temaRepository.existsById(postagem.getTema().getId()))
			return ResponseEntity.status(HttpStatus.OK)
					.body(postagemRepository.save(postagem));
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}			
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        //realiza se a resposta for nula
    
	/*@PutMapping ("{id}")
	public ResponseEntity<Postagem> putPostagem(@Valid @RequestBody Postagem postagem, @PathVariable Long id ){
		//return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem)); // essa linha serve para atualizar uma postagem
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem)))// caso ele coloque um id que não existe vai responder com um erro
				.orElse(ResponseEntity.notFound().build());*/
	}
	
	
	 @DeleteMapping("/{id}")
	    public ResponseEntity<Object> deletePostagem(@PathVariable Long id) {
	        return postagemRepository.findById(id)
	                .map(resposta -> {
	                    postagemRepository.deleteById(id);
	                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	        })
	                .orElse(ResponseEntity.notFound().build());
		
		
			
		}
}

