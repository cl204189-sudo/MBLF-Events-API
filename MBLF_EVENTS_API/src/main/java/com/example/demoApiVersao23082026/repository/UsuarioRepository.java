
package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmailHash(String emailHash);

    boolean existsByEmailHash(String emailHash);

}
