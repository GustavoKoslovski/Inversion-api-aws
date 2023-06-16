package uniamerica.com.inversion.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@ToString
@Table(name = "rendimento", schema = "public")
public class Rendimento extends AbstractEntity {

    @Getter @Setter
    @JoinColumn(name= "idPapel")
    @ManyToOne(fetch = FetchType.EAGER)
    private Papel papel;

    @Getter
    @Setter
    @Column(name = "preco_un")
    private Double preco_un;

    @Getter
    @Setter
    @Column(name = "quantidade")
    private Double quantidade;

    @Getter
    @Setter
    @Column(name = "data")
    private LocalDate data;

    @Getter
    @Setter
    @Column(name = "descricao")
    private String descricao;

    public Rendimento(Double preco_un, Double quantidade, LocalDate data, String descricao, Papel papel) {
        this.preco_un = preco_un;
        this.quantidade = quantidade;
        this.data = data;
        this.descricao = descricao;
    }
}
