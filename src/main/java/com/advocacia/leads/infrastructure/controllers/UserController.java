package com.advocacia.leads.infrastructure.controllers;

import com.advocacia.leads.dto.RoleChangeDTO;
import com.advocacia.leads.dto.UsuarioDTO;
import com.advocacia.leads.infrastructure.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Listar usuários – apenas ADMIN pode listar todos
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = userService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // Atualizar perfil (o próprio usuário)
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarPerfil(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        userService.atualizar(id, usuarioDTO);
        return ResponseEntity.ok("Usuário atualizado com sucesso.");
    }

    // Desativar conta (pode ser por ADMIN ou pelo próprio usuário)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> desativarUsuario(@PathVariable Long id) {
        userService.desativarUsuario(id);
        return ResponseEntity.ok("Usuário desativado com sucesso.");
    }

    // Atribuir ou remover roles – apenas ADMIN
    @PostMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> alterarRoles(@RequestBody RoleChangeDTO dto) {
        userService.alterarRoles(dto);
        return ResponseEntity.ok("Roles atualizadas com sucesso.");
    }

    @PutMapping("/{id}/reativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reativarUsuario(@PathVariable Long id) {
        userService.reativarUsuario(id);
        return ResponseEntity.ok("Usuário reativado com sucesso.");
    }


}
