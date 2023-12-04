package uniamerica.com.inversion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uniamerica.com.inversion.entity.Carteira;
import uniamerica.com.inversion.entity.Investimento;
import uniamerica.com.inversion.entity.Usuario;
import uniamerica.com.inversion.repository.InvestimentoRepository;
import uniamerica.com.inversion.repository.UsuarioRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class InvestimentoService {

    @Autowired
    private InvestimentoRepository investimentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Investimento findById(Long id, Usuario usuario){
        return this.investimentoRepository.findByIdAndUsuario(id, usuario).orElse(new Investimento());
    }

    public Page<Investimento> listAll(Pageable pageable, Usuario usuario){
        return this.investimentoRepository.findByUsuario(usuario, pageable);
    }

    @Transactional
    public Investimento insert(Investimento investimento, Usuario usuario) {
        if (this.validarRequest(investimento) &&
            this.isInvestimentoExist(investimento, usuario)) {
            this.investimentoRepository.save(investimento);
            return investimento;
        } else {
            throw new RuntimeException("Falha ao cadastrar o investimento");
        }
    }

    @Transactional
    public void update (Long id, Investimento investimento, Usuario usuario) {
        if (checarDono(investimento, usuario)) {
            
            if (id == investimento.getId() && this.validarRequest(investimento)) {
                this.investimentoRepository.save(investimento);
            } else {
                throw new RuntimeException("Falha ao Atualizar o investimento");
            }
        }else {
            throw new RuntimeException("Voce nao tem acesso a atualizar este Investimento");
        }
    }

    @Transactional
    public void desativar (Long id, Investimento investimento, Usuario usuario) {
        if (checarDono(investimento, usuario)) {
            if (id == investimento.getId() && this.validarRequest(investimento)) {
                this.investimentoRepository.save(investimento);
            } else {
                throw new RuntimeException("Falha ao Desativar o investimento");
            }
        }else {
            throw new RuntimeException("Voce nao tem acesso a desativa este Investimento");
        }
    }

    //** VALIDAÇÕES INVESTIMENTO **//

    public Boolean checarDono(Investimento investimento, Usuario usuario) {
        Optional<Investimento> investimentoAux = this.investimentoRepository.findById(investimento.getId());
        return investimentoAux.isPresent() && investimentoAux.get().getUsuario().getId().equals(usuario.getId());
    }

    public Boolean isValorCaracter(Investimento investimento) {
        char[] charSearch = {'[', '@', '_', '!', '#', '$', '%', '^', '&', '*', '(', ')', '<', '>', '?', '/', '|', '}', '{', '~', ':', ']'};
        for (int i = 0; i < investimento.getValorInvestimento().toString().length(); i++) {
            char chr = investimento.getValorInvestimento().toString().charAt(i);
            for (int j = 0; j < charSearch.length; j++) {
                if (charSearch[j] == chr) {
                    throw new RuntimeException("O valor inserido não é válido, favor insira um valor sem caracter especial.");
                }
            }
        }
        return true;
    }

//    public Boolean isInvestimentoNotNull(Investimento investimento) {
//        if (investimento.getNomeInvestimento() == null || investimento.getNomeInvestimento().isEmpty()) {
//            throw new RuntimeException("O nome do investimento não foi fornecido, favor inserir um nome.");
//        } else {
//            return true;
//        }
//    }


// teste push

    public Boolean isInvestimentoExist(Investimento investimento, Usuario usuario) {
        if (investimento.getNomeInvestimento() == null || investimento.getNomeInvestimento().isEmpty()) {
            throw new RuntimeException("O nome do investimento não foi fornecido, favor inserir um nome.");
        } else {
            // Verificar se já existe um investimento com o mesmo nome
            Investimento investimentoExistente = investimentoRepository.findByNomeInvestimento(investimento.getNomeInvestimento(), usuario.getId());

            if (investimentoExistente != null) {
                // Verificar se o investimento existente está ativo
                if (investimentoExistente.isAtivo()) {
                    throw new RuntimeException("Já existe um investimento ativo com o mesmo nome.");
                } else {
                    // Permitir a inserção caso o investimento existente esteja inativo
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    public Boolean validarRequest(Investimento investimento){
        return this.isValorCaracter(investimento);
    }

}
