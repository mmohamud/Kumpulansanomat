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
public class LoginController {

    @Autowired
    private WriterRepository writerRepository;

    @GetMapping("/register")
    public String getREgisterPage() {
        return "register";
    }
    
    @GetMapping("login")
    public String getLogin() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(Model model, @RequestParam String kayttajatunnus, @RequestParam String salasana, HttpSession session) {
        ArrayList<String> messages = new ArrayList();
        Writer writer = writerRepository.findBykayttajatunnus(kayttajatunnus);
        if (writer != null && writer.getKayttajatunnus().equals(kayttajatunnus) && writer.getSalasana().equals(salasana)) {
            messages.add("Tervetuloa");
            model.addAttribute("messages", messages);
            session.setAttribute("writer", writer);
            
            return "redirect:/";
            
        } 
        messages.add("Väärä käyttäjätunnus tai salasana");
        model.addAttribute("messages", messages);
        return "login";        
    } 
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute("writer", null);
        return "redirect:/";
    }
    
    @PostMapping("/register")
    public String addWriter(@RequestParam String nimi, @RequestParam String kayttajatunnus, @RequestParam String salasana, HttpSession session, Model model) {
        ArrayList<String> messages = new ArrayList();
        if (nimi.isEmpty() || kayttajatunnus.isEmpty() || salasana.isEmpty() ) {
            messages.add("Täytä kaikki kentät!");
            model.addAttribute("messages", messages);
            return "login";
        }        
        Writer writer = new Writer();
        writer.setNimi(nimi);
        writer.setKayttajatunnus(kayttajatunnus);
        writer.setSalasana(salasana);
        writerRepository.save(writer);
        messages.add("Tervetuloa " + writer.getNimi());
        model.addAttribute("messages", messages);
        session.setAttribute("writer", writer);
        return "redirect:/";
    }
}

