/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
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
    @Column(length = 10000)
    private String teksti;
    private LocalDateTime julkaisuaika;
    private int klikkaukset = 0;
    @ManyToMany(mappedBy = "uutiset")
    private List<Writer> kirjoittajat = new ArrayList();
    @ManyToMany
    private List<Category> kategoriat = new ArrayList();
    
}