package com.trabalhorodolfo.picpayrh.service;

import com.trabalhorodolfo.picpayrh.entity.Funcionario;
import com.trabalhorodolfo.picpayrh.entity.Status;
import com.trabalhorodolfo.picpayrh.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionariorepo;

    public FuncionarioService(FuncionarioRepository funcionariorepo) {
        this.funcionariorepo = funcionariorepo;
    }

    public Funcionario cadastrarFuncionario(Funcionario funcionario) {
        validarObrigatorios(funcionario);
        return funcionariorepo.salvar(funcionario);
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionariorepo.findAll();
    }

    public Optional<Funcionario> listarUmFuncionarios(Long id) {
        return funcionariorepo.findById(id);
    }

    public Optional<Funcionario> atualizarFuncionario(Long id, Funcionario dados) {
        return funcionariorepo.findById(id).map(f -> {
            f.setNome(dados.getNome());
            f.setEmail(dados.getEmail());
            f.setTelefone(dados.getTelefone());
            f.setCargo(dados.getCargo());
            f.setDepartamento(dados.getDepartamento());
            f.setSalario(dados.getSalario());
            f.setCidade(dados.getCidade());
            f.setStatus(dados.getStatus());
            return f;
        });
    }

    public Optional<Funcionario> atualizarParcialFuncionario(Long id, Funcionario dados) {
        return funcionariorepo.findById(id).map(f -> {
            if (dados.getNome() != null)         f.setNome(dados.getNome());
            if (dados.getEmail() != null)        f.setEmail(dados.getEmail());
            if (dados.getTelefone() != null)     f.setTelefone(dados.getTelefone());
            if (dados.getCargo() != null)        f.setCargo(dados.getCargo());
            if (dados.getDepartamento() != null) f.setDepartamento(dados.getDepartamento());
            if (dados.getSalario() != null)      f.setSalario(dados.getSalario());
            if (dados.getCidade() != null)       f.setCidade(dados.getCidade());
            if (dados.getStatus() != null)       f.setStatus(dados.getStatus());
            return f;
        });
    }

    public boolean deletarFuncionario(Long id) {
        return funcionariorepo.deleteById(id);
    }

    public List<Funcionario> buscarFuncionarios(String nome, String cargo, Status status) {
        return funcionariorepo.findAll().stream()
                .filter(f -> nome == null || nome.isBlank()
                        || f.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(f -> cargo == null || cargo.isBlank()
                        || f.getCargo().toLowerCase().contains(cargo.toLowerCase()))
                .filter(f -> status == null || status.equals(f.getStatus()))
                .toList();
    }

    public Map<String, Long> gerarIndicadores() {
        List<Funcionario> todos = funcionariorepo.findAll();
        Map<String, Long> indicadores = new LinkedHashMap<>();
        indicadores.put("total", (long) todos.size());
        indicadores.put("emAnalise", contarPorStatus(todos, Status.EM_ANALISE));
        indicadores.put("aprovados", contarPorStatus(todos, Status.APROVADO));
        indicadores.put("reprovados", contarPorStatus(todos, Status.REPROVADO));
        indicadores.put("contratados", contarPorStatus(todos, Status.CONTRATADO));
        return indicadores;
    }

    private long contarPorStatus(List<Funcionario> lista, Status status) {
        return lista.stream().filter(f -> status.equals(f.getStatus())).count();
    }

    private void validarObrigatorios(Funcionario funcionario) {
        if (funcionario.getNome() == null || funcionario.getNome().isBlank()) {
            throw new IllegalArgumentException("O campo 'nome' é obrigatório.");
        }
        if (funcionario.getEmail() == null || funcionario.getEmail().isBlank()) {
            throw new IllegalArgumentException("O campo 'email' é obrigatório.");
        }
        if (funcionario.getCargo() == null || funcionario.getCargo().isBlank()) {
            throw new IllegalArgumentException("O campo 'cargo' é obrigatório.");
        }
    }
}
