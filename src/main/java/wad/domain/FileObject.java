/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.domain;

import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
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
public class FileObject extends AbstractPersistable<Long>{

    private String nimi;
    private String mediaTyyppi;
    private Long koko;
    private LocalDateTime julkaisuaika;
    @OneToOne
    private News uutiset;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] sisalto;
}
