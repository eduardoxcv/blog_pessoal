package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generation.blogpessoal.model.Postagem;

@Repository
public interface PostagemRepository extends JpaRepository <Postagem, Long>{

	List <Postagem> findAllByTituloContainingIgnoreCase(String titulo);
	
	// a linha 14 "LIST" descreve o codigo abaixo do MySQL
	// é a mesma coisa que o: select * from tb_postagem where titulo like %titulo%;
	
}
