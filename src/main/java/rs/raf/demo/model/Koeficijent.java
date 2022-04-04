package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Koeficijent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long koeficijentId;
    @Column
    private Double penzionoOsiguranje1;
    @Column
    private Double penzionoOsiguranje2;
    @Column
    private Double zdravstvenoOsiguranje1;
    @Column
    private Double zdravstvenoOsiguranje2;
    @Column
    private Double nezaposlenostNaTeretPoslodavca1;
    @Column
    private Double nezaposlenostNaTeretPoslodavca2;
    @Column
    private Double najnizaOsnovica;
    @Column
    private Double najvisaOsnovica;
    @Column
    private boolean status;
    @Column
    private Date date;

}
