package com.trabalhorodolfo.picpayrh.controller;

import com.trabalhorodolfo.picpayrh.entity.Funcionario;
import com.trabalhorodolfo.picpayrh.entity.Status;
import com.trabalhorodolfo.picpayrh.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }
    @PostMapping
    public ResponseEntity<Funcionario> cadastrar(@RequestBody Funcionario funcionario) {
        Funcionario criado = funcionarioService.cadastrarFuncionario(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) Status status) {

        return ResponseEntity.ok(funcionarioService.buscarFuncionarios(nome, cargo, status));
    }

    @GetMapping("/indicadores")
    public ResponseEntity<Map<String, Long>> indicadores() {
        return ResponseEntity.ok(funcionarioService.gerarIndicadores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return funcionarioService.listarUmFuncionarios(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> naoEncontrado(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        return funcionarioService.atualizarFuncionario(id, funcionario)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> naoEncontrado(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        return funcionarioService.atualizarParcialFuncionario(id, funcionario)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> naoEncontrado(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        if (funcionarioService.deletarFuncionario(id)) {
            return ResponseEntity.ok(Map.of("mensagem", "Funcionário " + id + " excluído com sucesso."));
        }
        return naoEncontrado(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }

    private ResponseEntity<Map<String, String>> naoEncontrado(Long id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", "Funcionário com id " + id + " não foi encontrado."));
    }
}
