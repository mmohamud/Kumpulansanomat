/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.domain;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author mmohamud
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class News extends AbstractPersistable<Long> {
    
    private String otsikko;
    private String ingressi;
    private byte[] kuva;
    private String teksti;
    private LocalDate julkaisuaika;
    private int klikkaukset;
    @ManyToMany(mappedBy = "uutiset")
    private List<Writer> kirjoittajat;
    @ManyToMany(mappedBy = "uutiset")
    private List<Category> kategoriat;
    
}