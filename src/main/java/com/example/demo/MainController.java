package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RepoRepository repoRepository;

    @Autowired
    CollaboratorsRepository collaboratorsRepository;

    @Autowired
    RestTemplate restTemplate;

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
    public String reposDetail(@PathVariable("name") String name, Model model, String login) {

        String token = "a0d6a77c4e1f8a314e4a8ccbceec7961ee307ba8";

        User user = restTemplate.getForObject("https://api.github.com/users/bilu-Blen?access_token=" + token, User.class);

        user.setRepos_url(user.getRepos_url());
//        userRepository.save(user);


        //since it is an array what is returned use this method
        ResponseEntity<List<Repo>> repoResponse =
                restTemplate.exchange(user.getRepos_url() + "?access_token=" + token,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Repo>>() {
                        });
        List<Repo> repos = repoResponse.getBody();

//        for (Repo repo1 : repos) {
//            repoRepository.save(repo1);

            String collaborators = repoRepository.findByName(name).getCollaborators_url();
            //taking out the last part after { in, https://api.github.com/repos/bilu-Blen/ATMApp/collaborators{/collaborator}
            int index = collaborators.lastIndexOf('{');
            collaborators = collaborators.substring(0, index);

            ResponseEntity<List<Collaborators>> collaboratorsresponse =
                    restTemplate.exchange(collaborators + "?access_token=" + token,
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Collaborators>>() {
                            });

            List<Collaborators> collaboratorsList = collaboratorsresponse.getBody();

            for (Collaborators collaborator : collaboratorsList) {
                System.out.println("The collaborator/s is/are " + collaborator.getLogin());
                collaboratorsRepository.save(collaborator);
            }
       //getting unique cloners

        Clones clone = restTemplate.getForObject("https://api.github.com/repos/bilu-Blen/UsingGitHubAPI/traffic/clones?access_token=" + token, Clones.class);
        System.out.println("This is the unique visitor " + clone.getUniques());
        System.out.println("This is the repo url " + user.getRepos_url());
        System.out.println(repoRepository.findByName(name).getUrl()+"/contents?access_token=" + token);


        //getting unique views
        Views view = restTemplate.getForObject("https://api.github.com/repos/bilu-Blen/UsingGitHubAPI/traffic/clones?access_token=" + token, Views.class);

        //lets get the content
        ResponseEntity<List<Contents>> contentresponse =
                restTemplate.exchange(repoRepository.findByName(name).getUrl()+"/contents?access_token=" + token,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Contents>>() {
                        });
        System.out.println(repoRepository.findByName(name).getUrl()+"/contents?access_token=" + token);

        List<Contents> contentsList = contentresponse.getBody();

        for (Contents content : contentsList) {
            System.out.println("The content's of this repo are " + content.getName());
//            collaboratorsRepository.save(content);
        }
//        }

        model.addAttribute("clone", clone);
        model.addAttribute("view", view);

        model.addAttribute("collaboratorsList", collaboratorsList);
        model.addAttribute("repo", repoRepository.findByName(name));
        model.addAttribute("contentsList", contentsList);
        return "repodetails";
    }

}
