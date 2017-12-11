/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wad.domain.Writer;
import wad.repository.WriterRepository;

/**
 *
 * @author mmohamud
 */
@Controller
@Transactional
public class WriterController {

    @Autowired
    private WriterRepository writerRepository;

    @PostMapping("/addWriter")
    public String addWriter(@RequestParam String nimi) {
        Writer writer = new Writer();
        nimi.toLowerCase();
        nimi.trim();
        writer.setNimi(nimi);
        writerRepository.save(writer);
        return "redirect:/";
    }

    
}
