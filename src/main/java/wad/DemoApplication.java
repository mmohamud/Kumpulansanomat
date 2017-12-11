package wad;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import wad.domain.Category;
import wad.domain.Writer;
import wad.repository.CategoryRepository;
import wad.repository.WriterRepository;

@SpringBootApplication
public class DemoApplication {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WriterRepository writerRepository;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
        
        @Bean
        InitializingBean sendDataBaseCategorys() {
            return () -> {
                Category kotimaa = new Category();
        kotimaa.setNimi("kotimaa");
        categoryRepository.save(kotimaa);
        Category ulkomaat = new Category();
        ulkomaat.setNimi("ulkomaat");
        categoryRepository.save(ulkomaat);
        Category viihde = new Category();
        viihde.setNimi("viihde");
        categoryRepository.save(viihde);
        Category urheilu = new Category();
        urheilu.setNimi("urheilu");
        categoryRepository.save(urheilu);
            };
        }
        
        @Bean
        InitializingBean sendDataBaseWriters() {
            return () -> {
            Writer writer = new Writer();
            writer.setNimi("Testi Kayttaja");
            writer.setKayttajatunnus("ouzii");
            writer.setSalasana("12345");
            writerRepository.save(writer);
            };
        }
}
