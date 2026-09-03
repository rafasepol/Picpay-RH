package com.trabalhorodolfo.picpayrh.repository;

import com.trabalhorodolfo.picpayrh.entity.Funcionario;
import com.trabalhorodolfo.picpayrh.entity.Status;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FuncionarioRepository {

    private final List<Funcionario> listFuncionario = new ArrayList<>();
    private long proximoId = 1;

    public List<Funcionario> findAll() {
        return List.copyOf(listFuncionario);
    }

    public Optional<Funcionario> findById(Long id) {
        return listFuncionario.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();
    }

    public Funcionario salvar(Funcionario funcionario) {
        funcionario.setId(proximoId++);
        listFuncionario.add(funcionario);
        return funcionario;
    }

    public boolean deleteById(Long id) {
        return listFuncionario.removeIf(f -> f.getId().equals(id));
    }

    @PostConstruct
    public void carregarDadosFicticios() {
        salvar(new Funcionario(null, "Ana Souza", "ana.souza@email.com", "(11) 98888-1111",
                "Desenvolvedora Backend", "Tecnologia", 8500.0, "São Paulo", Status.EM_ANALISE));
        salvar(new Funcionario(null, "Bruno Lima", "bruno.lima@email.com", "(21) 97777-2222",
                "Analista de Dados", "Tecnologia", 7200.0, "Rio de Janeiro", Status.APROVADO));
        salvar(new Funcionario(null, "Carla Mendes", "carla.mendes@email.com", "(31) 96666-3333",
                "Designer UX", "Produto", 6800.0, "Belo Horizonte", Status.CONTRATADO));
        salvar(new Funcionario(null, "Diego Rocha", "diego.rocha@email.com", "(41) 95555-4444",
                "Analista de RH", "Recursos Humanos", 5400.0, "Curitiba", Status.REPROVADO));
        salvar(new Funcionario(null, "Eduarda Alves", "eduarda.alves@email.com", "(51) 94444-5555",
                "Desenvolvedora Frontend", "Tecnologia", 7900.0, "Porto Alegre", Status.EM_ANALISE));
    }
}
