package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RepoRepository repoRepository;

    @RequestMapping("/")
    public String Home(Model model /*,String login*/) {
        model.addAttribute("users",userRepository.findAll());
        //model.addAttribute("counter",userRepository.countByLogin(login));
//user's count
        return "homepage";
    }
    @RequestMapping("/userDetail/{login}")
    public String showDetail(@PathVariable("login") String login, Model model, String name) {
        model.addAttribute("user",userRepository.findByLogin(login));
        // model.addAttribute("user",userRepository.findById(id));
//user's count
        model.addAttribute("repo",repoRepository.findByName(name) );
        model.addAttribute("repos",repoRepository.findAll());
        return "userpage";
    }


    /*@RequestMapping("/repopage")
    public String repoPage(Model model *//*,String login*//*) {
        model.addAttribute("repos",repoRepository.findAll());
        //model.addAttribute("counter",userRepository.countByLogin(login));
//user's count
        return "repopage";
    }*/

    @RequestMapping("/repoDetail/{name}")
    public String showDetail(@PathVariable("name") String name, Model model) {
        model.addAttribute("repo",repoRepository.findByName(name) );

        return "repodetails";
    }

}
