package wad.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import wad.domain.Category;
import wad.domain.FileObject;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.CategoryRepository;
import wad.repository.FileObjectRepository;
import wad.repository.NewsRepository;
import wad.repository.WriterRepository;

@Controller
@Transactional
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private FileObjectRepository fileRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WriterRepository writerRepository;

    @GetMapping("/category")

    public String newsByCategory(@RequestParam String category, Model model) {
        model.addAttribute("kategoriat", categoryRepository.findAll());
        Category category1 = categoryRepository.findByNimi(category);
        model.addAttribute("uutisetKategorianMukaan", newsRepository.findBykategoriat(category1));
        return "newsOrdered";
    }

    @GetMapping("/")
    public String frontPage(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "Julkaisuaika");
        model.addAttribute("viimeisimmatUutiset", newsRepository.findAll(pageable));
        model.addAttribute("kategoriat", categoryRepository.findAll());
        return "index";
    }

    @GetMapping("/news")
    public String allNews(Model model) {
        Pageable pageable = PageRequest.of(0, 50, Sort.Direction.DESC, "Julkaisuaika");
        model.addAttribute("kaikkiUutiset", newsRepository.findAll(pageable));
        model.addAttribute("kategoriat", categoryRepository.findAll());
        return "allNews";
    }

    @GetMapping("/news/{Id}")
    public String newsPage(Model model, @PathVariable Long Id) {
        News uutinen = newsRepository.getOne(Id);
        uutinen.setKlikkaukset(uutinen.getKlikkaukset() + 1);
        newsRepository.save(uutinen);
        model.addAttribute("uutinen", uutinen);
        model.addAttribute("kategoriat", categoryRepository.findAll());
        return "news";
    }

    @GetMapping("/news/{Id}/edit")
    public String editNews(Model model, @PathVariable Long Id) {
        News uutinen = newsRepository.getOne(Id);
        List<Category> kategoriat = categoryRepository.findAll();
        List<Writer> kirjoittajat = writerRepository.findAll();
        model.addAttribute("uutinen", uutinen);
        model.addAttribute("kategoriat", kategoriat);
        model.addAttribute("kirjoittajat", kirjoittajat);
        return "editNews";
    }

    public List<String> validaattori(Model model, String otsikko, String ingressi, String teksti, MultipartFile img, List<Long> kategoriat, List<Long> kirjoittajat) {
        ArrayList<String> errors = new ArrayList();

        if (otsikko.isEmpty() || otsikko.length() > 255) {
            errors.add("Otsikon pituuden tulee olla välillä 0-255");
        }
        if (ingressi.isEmpty() || ingressi.length() > 255) {
            errors.add("Ingressin pituuden tulee olla välillä 0-255");
        }
        if (teksti.isEmpty() || teksti.length() > 10000) {
            errors.add("Tekstin pituuden tulee olla välillä 0-10000");
        }
        if (kategoriat.isEmpty()) {
            errors.add("Uutisella tulee olla kategoria!");
        }
        if (kirjoittajat.isEmpty()) {
            errors.add("Uutisella tulee olla kirjoittaja!");
        }

        return errors;
    }

    @PostMapping("/news/{Id}/edit")
    @Transactional
    public String editNews(@PathVariable Long Id, Model model, @RequestParam String otsikko, @RequestParam String ingressi, @RequestParam String teksti, @RequestParam("img") MultipartFile img, @RequestParam(value = "kategoriat", required = true) List<Long> kategoriat, @RequestParam(value = "kirjoittajat", required = true) List<Long> kirjoittajat) throws IOException {
        ArrayList<String> messages = new ArrayList();
        List validaatiot = validaattori(model, otsikko, ingressi, teksti, img, kategoriat, kirjoittajat);

        if (validaatiot.size() > 0) {
            model.addAttribute("errors", validaatiot);
            return "editNews";
        }
        News uutinen = newsRepository.getOne(Id);
        uutinen.setOtsikko(otsikko);
        uutinen.setIngressi(ingressi);
        uutinen.setTeksti(teksti);
        uutinen.setKlikkaukset(0);
        uutinen.setJulkaisuaika(LocalDateTime.now());
        uutinen.getKategoriat().clear();;
        uutinen.getKategoriat().clear();

        uutinen.getKategoriat().clear();
        for (Long kategoria : kategoriat) {
            categoryRepository.getOne(kategoria).getUutiset().remove(uutinen);
        }
        uutinen.getKirjoittajat().clear();
        for (Long kirjoittaja : kirjoittajat) {
            writerRepository.getOne(kirjoittaja).getUutiset().remove(uutinen);
        }

        for (Long kategoria : kategoriat) {
            uutinen.getKategoriat().add(categoryRepository.getOne(kategoria));
            categoryRepository.getOne(kategoria).getUutiset().add(uutinen);
        }

        for (Long kirjoittaja : kirjoittajat) {
            writerRepository.getOne(kirjoittaja).getUutiset().add(uutinen);
            uutinen.getKirjoittajat().add(writerRepository.getOne(kirjoittaja));
        }

        FileObject kuva = fileRepository.findByuutiset(uutinen);
        kuva.setSisalto(img.getBytes());
        kuva.setNimi(img.getName());
        kuva.setKoko(img.getSize());
        kuva.setMediaTyyppi(img.getContentType());
        kuva.setUutiset(uutinen);

        newsRepository.save(uutinen);
        fileRepository.save(kuva);
        messages.add("Uutinen päivitetty!");
        model.addAttribute("message", messages);
        return "redirect:/news/{Id}";
    }

    @GetMapping("/addNews")
    public String addNews(Model model) {
        model.addAttribute("kirjoittajat", writerRepository.findAll());
        model.addAttribute("kategoriat", categoryRepository.findAll());
        return "addNews";
    }

    @PostMapping("/addNews")
    @Transactional
    public String addNews(Model model, @RequestParam String otsikko, @RequestParam String ingressi, @RequestParam String teksti, @RequestParam("img") MultipartFile img, @RequestParam(value = "kategoriat", required = true) List<Long> kategoriat, @RequestParam(value = "kirjoittajat", required = true) List<Long> kirjoittajat) throws IOException {
        List validaatiot = validaattori(model, otsikko, ingressi, teksti, img, kategoriat, kirjoittajat);
        if (validaatiot.size() > 0) {
            model.addAttribute("errors", validaatiot);
            model.addAttribute("kategoriat", categoryRepository.findAll());
            model.addAttribute("kirjoittajat", writerRepository.findAll());
            return "addNews";
        }
        ArrayList<String> messages = new ArrayList();
        News news = new News();
        news.setOtsikko(otsikko);
        news.setIngressi(ingressi);
        news.setTeksti(teksti);
        news.setKlikkaukset(0);
        news.setJulkaisuaika(LocalDateTime.now());

        for (Long kategoria : kategoriat) {
            news.getKategoriat().add(categoryRepository.getOne(kategoria));
            categoryRepository.getOne(kategoria).getUutiset().add(news);
        }

        for (Long kirjoittaja : kirjoittajat) {
            writerRepository.getOne(kirjoittaja).getUutiset().add(news);
            news.getKirjoittajat().add(writerRepository.getOne(kirjoittaja));
        }

        newsRepository.save(news);

        FileObject kuva = new FileObject();
        kuva.setSisalto(img.getBytes());
        kuva.setNimi(img.getName());
        kuva.setKoko(img.getSize());
        kuva.setMediaTyyppi(img.getContentType());
        kuva.setUutiset(news);

        fileRepository.save(kuva);
        messages.add("Uutinen lisätty!");
        model.addAttribute("message", messages);
        model.addAttribute("kategoriat", categoryRepository.findAll());
        return "redirect:/";
    }

    @DeleteMapping("/news/{Id}/delete")
    public String deleteNews(@PathVariable Long Id) {
        News uutinen = newsRepository.getOne(Id);
        FileObject kuva = fileRepository.findByuutiset(uutinen);
        for (Writer writer : writerRepository.findAll()) {
            if (writer.getUutiset().contains(uutinen)) {
                writer.getUutiset().remove(uutinen);
                writerRepository.save(writer);
            }
        }
        newsRepository.delete(uutinen);
        fileRepository.delete(kuva);
        return "redirect:/news";
    }
    
    @GetMapping("/addCategory")
    public String addCategory(Model model) {
        model.addAttribute("kategoriat", categoryRepository.findAll());
        model.addAttribute("kirjoittajat", writerRepository.findAll());
        return "addCategory";
    }
    @GetMapping("/addWriter")
    public String addWriter(Model model) {
        model.addAttribute("kategoriat", categoryRepository.findAll());
        model.addAttribute("kirjoittajat", writerRepository.findAll());
        return "addWriter";
    }

}
