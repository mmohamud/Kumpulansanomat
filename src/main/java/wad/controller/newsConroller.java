/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author mmohamud
 */
@Controller
public class newsConroller {
    
    
    @GetMapping("/")
    public String frontPage() {
        return "frontpage";
    }
}
